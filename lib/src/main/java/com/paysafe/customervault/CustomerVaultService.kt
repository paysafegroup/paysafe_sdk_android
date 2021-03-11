/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault

import com.paysafe.PaysafeApiClient
import com.paysafe.PaysafeDsl
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.customervault.data.api.CustomerVaultApi
import com.paysafe.customervault.domain.CreateGooglePaySingleUseTokenUseCase
import com.paysafe.customervault.domain.CreateSingleUseTokenUseCase

interface CustomerVaultService {

    fun createSingleUseToken(
        params: SingleUseTokenParams,
        callback: CustomerVaultCallback<SingleUseToken>
    )

    fun createGooglePayPaymentToken(
        params: GooglePayTokenParams,
        callback: CustomerVaultCallback<GooglePaySingleUseToken>
    )

    @PaysafeDsl
    class Builder {

        /**
         * The client used for connecting to the Paysafe APIs.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var apiClient: PaysafeApiClient

        /**
         * Sets customerVaultService api client
         * Used for better readability when building client in java code
         */
        fun withApiClient(apiClient: PaysafeApiClient) = also { this.apiClient = apiClient }

        fun build(): CustomerVaultService {
            check(::apiClient.isInitialized) { "API Client is required, but missing" }

            val api = CustomerVaultApi(apiClient)

            return CustomerVaultServiceImpl(
                CreateSingleUseTokenUseCase(api),
                CreateGooglePaySingleUseTokenUseCase(api)
            )
        }

    }

}