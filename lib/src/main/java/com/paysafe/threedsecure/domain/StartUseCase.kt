/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.paysafe.Mockable
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.JwtResponse
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.util.toJson
import com.paysafe.util.Result

@Mockable
internal class StartUseCase(private val api: NetbanxApi, private val cardinal: Cardinal) {

    internal operator fun invoke(
        cardBin: String,
        callback: (Result<String, ThreeDSecureError>) -> Unit
    ) {
        api.jwt(cardBin) {
            when (it) {
                is Result.Success -> cardinal.init(
                    it.data.jwt,
                    object : CardinalInitService {
                        override fun onSetupCompleted(consumerSessionId: String?) {
                            onSetupCompleted(callback, it)
                        }

                        override fun onValidated(
                            validatedResponse: ValidateResponse?,
                            serverJwt: String?
                        ) {
                            onValidated(validatedResponse, callback)
                        }

                    }
                )

                is Result.Failure -> {
                    api.log(EventType.INTERNAL_SDK_ERROR, "/jwt call failed")
                    callback(Result.Failure(it.error))
                }
            }
        }
    }

    private fun onSetupCompleted(
        callback: (Result<String, ThreeDSecureError>) -> Unit,
        jwtResult: Result.Success<JwtResponse>
    ) {
        api.log(
            EventType.SUCCESS,
            "DeviceFingerPrinting completed successfully"
        )
        callback(Result.Success(jwtResult.data.deviceFingerprintId))
    }

    private fun onValidated(
        validatedResponse: ValidateResponse?,
        callback: (Result<String, ThreeDSecureError>) -> Unit
    ) {
        validatedResponse?.let {
            api.log(
                EventType.INTERNAL_SDK_ERROR,
                "DeviceFingerPrinting failed with: ${validatedResponse.toJson()}"
            )

            callback(
                Result.Failure(
                    ThreeDSecureError(
                        code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                        detailedMessage = "The flow could not be started because of an internal exception " +
                                "${it.errorNumber} : ${it.errorDescription}."
                    )
                )
            )
        }
    }
}