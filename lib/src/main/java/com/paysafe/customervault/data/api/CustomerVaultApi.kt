/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data.api

import com.paysafe.ApiRequest
import com.paysafe.ApiResponse
import com.paysafe.Mockable
import com.paysafe.PaysafeApiClient
import com.paysafe.common.Error
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.util.Result

@Mockable
internal class CustomerVaultApi(private val apiClient: PaysafeApiClient) {

    internal fun createSingleUseToken(
        params: SingleUseTokenParams,
        callback: (Result<SingleUseToken, Error>) -> Unit
    ) = with(
        ApiRequest(
            path = SINGLE_USE_TOKEN_PATH,
            body = params
        )
    ) { executeWith(callback) }

    internal fun createGooglePaySingleUseToken(
        params: GooglePayTokenParams,
        callback: (Result<GooglePaySingleUseToken, Error>) -> Unit
    ) = with(
        ApiRequest(
            path = GOOGLE_PAY_SINGLE_USE_TOKEN_PATH,
            body = params
        )
    ) { executeWith(callback) }

    private inline fun <reified T> ApiRequest.executeWith(crossinline callback: (Result<T, Error>) -> Unit) {
        apiClient.execute<T>(this) {
            val result = when (it) {
                is ApiResponse.Success -> Result.Success(it.data)
                ApiResponse.Failure.ConnectionFailed -> Result.Failure(
                    Error(
                        code = "9001",
                        message = "The HTTP request could not be executed due to connectivity issues, cancellation or a timeout"
                    )
                )
                is ApiResponse.Failure.InvalidMerchantConfiguration -> Result.Failure(it.error!!)
                is ApiResponse.Failure.InvalidApiKey -> Result.Failure(it.error!!)
                is ApiResponse.Failure.InternalSdkError -> Result.Failure(it.error!!)
            }
            callback(result)
        }
    }

    companion object {

        private const val CUSTOMER_VAULT_ENDPOINT = "customervault"
        private const val VERSION = "v1"

        private const val SINGLE_USE_TOKEN_PATH =
            "$CUSTOMER_VAULT_ENDPOINT/$VERSION/singleusetokens"
        private const val GOOGLE_PAY_SINGLE_USE_TOKEN_PATH =
            "$CUSTOMER_VAULT_ENDPOINT/$VERSION/googlepaysingleusetokens"

    }
}