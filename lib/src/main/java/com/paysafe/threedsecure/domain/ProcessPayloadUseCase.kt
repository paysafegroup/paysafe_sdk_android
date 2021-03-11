/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.paysafe.Mockable
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.util.Result
import com.paysafe.threedsecure.data.parseChallengePayload
import com.paysafe.util.base64Decode

@Mockable
internal class ProcessPayloadUseCase {

    internal operator fun invoke(payload: String, callback: (Result<ChallengePayload, ThreeDSecureError>) -> Unit) {
        val result = try {
            Result.Success(payload.base64Decode().parseChallengePayload())
        } catch (e: Exception) {
            Result.Failure(
                ThreeDSecureError(
                    code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                    detailedMessage = "Invalid SDK challenge payload: $payload"
                )
            )
        }

        callback(result)
    }


}