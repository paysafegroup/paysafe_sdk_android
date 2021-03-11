/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data.api

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.paysafe.ApiRequest
import com.paysafe.ApiResponse
import com.paysafe.Mockable
import com.paysafe.PaysafeApiClient
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.*
import com.paysafe.util.Result

@Mockable
internal class NetbanxApi constructor(
    private val apiClient: PaysafeApiClient,
    private val generateGUID: () -> String
) {

    @VisibleForTesting
    internal var correlationId = ""

    internal fun jwt(cardBin: String, callback: (Result<JwtResponse, ThreeDSecureError>) -> Unit) {
        correlationId = generateGUID()
        ApiRequest(
            JWT_URI,
            JwtRequest(apiClient.account, Card(cardBin)),
            mapOf(HEADER_CORRELATION_ID to correlationId)
        ).executeWith(callback)
    }

    internal fun finalize(
        accountId: String,
        authenticationId: String,
        serverJwt: String?,
        callback: (Result<Unit, ThreeDSecureError>) -> Unit
    ) =
        with(
            Uri.Builder()
                .appendPath(THREE_D_SECURE_ENDPOINT)
                .appendPath(VERSION)
                .appendPath(PATH_SEGMENT_ACCOUNTS)
                .appendPath(accountId)
                .appendPath(PATH_SEGMENT_AUTHENTICATIONS)
                .appendPath(authenticationId)
                .appendPath(PATH_SEGMENT_FINALIZE)
                .build()
                .toString()
        ) {
            ApiRequest(
                this,
                FinalizeRequest(serverJwt),
                mapOf(HEADER_CORRELATION_ID to correlationId)
            ).executeWith(callback)
        }

    internal fun log(
        evenType: EventType,
        message: String
    ) =
        apiClient.execute<Unit>(
            ApiRequest(
                LOG_URI,
                LogRequest(evenType, message),
                mapOf(HEADER_CORRELATION_ID to correlationId)
            )
        ) {}

    private inline fun <reified T> ApiRequest.executeWith(crossinline callback: (Result<T, ThreeDSecureError>) -> Unit) =
        apiClient.execute<T>(this) { callback(it.toResult()) }

    private fun <T> ApiResponse<T>.toResult(): Result<T, ThreeDSecureError> = when (this) {
        is ApiResponse.Success -> Result.Success(data)

        is ApiResponse.Failure.ConnectionFailed ->
            Result.Failure(
                ThreeDSecureError(
                    code = ThreeDSecureError.ERROR_CODE_CONNECTION_FAILED,
                    detailedMessage = "The HTTP request could not be executed due to connectivity issues, cancellation or a timeout"
                )
            )

        is ApiResponse.Failure.InvalidMerchantConfiguration ->
            Result.Failure(
                ThreeDSecureError(
                    code = ThreeDSecureError.ERROR_CODE_INVALID_MERCHANT_CONFIGURATION,
                    detailedMessage = "Invalid merchant configuration setup",
                    correlationId = correlationId
                )
            )

        is ApiResponse.Failure.InvalidApiKey ->
            Result.Failure(
                ThreeDSecureError(
                    code = ThreeDSecureError.ERROR_CODE_INVALID_API_KEY,
                    detailedMessage = "Invalid apiKey or apiSecret",
                    correlationId = correlationId
                )
            )

        is ApiResponse.Failure.InternalSdkError ->
            Result.Failure(
                ThreeDSecureError(
                    code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                    detailedMessage = "An unexpected error occurred",
                    correlationId = correlationId
                )
            )
    }

    companion object {

        private const val THREE_D_SECURE_ENDPOINT = "threedsecure"
        private const val VERSION = "v2"

        private const val JWT_URI = "$THREE_D_SECURE_ENDPOINT/$VERSION/jwt"
        private const val LOG_URI = "$THREE_D_SECURE_ENDPOINT/$VERSION/log"

        private const val PATH_SEGMENT_ACCOUNTS = "accounts"
        private const val PATH_SEGMENT_AUTHENTICATIONS = "authentications"
        private const val PATH_SEGMENT_FINALIZE = "finalize"

        @VisibleForTesting
        internal const val HEADER_CORRELATION_ID = "X-INTERNAL-CORRELATION-ID"

    }
}