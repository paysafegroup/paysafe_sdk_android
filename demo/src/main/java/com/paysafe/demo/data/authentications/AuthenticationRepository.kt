/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.authentications

import android.util.Base64
import com.paysafe.demo.data.authentications.service.MerchantBackendService
import java.lang.Exception

class AuthenticationRepository {

    private val authService = MerchantBackendService.create()

    suspend fun getChallengePayload(
        amount: Int,
        cardNumber: String,
        expMonth: Int,
        expYear: Int,
        deviceFingerprint: String
    ): Result<AuthenticationResponse> {
        val response = authService.getChallengePayload(
            "$AUTHORIZATION_HEADER_START${getEncodedCredentials3DS()}",
            MERCHANT_ACCOUNT_NUMBER,
            body = AuthenticationRequest(
                amount = amount.toString(),
                deviceFingerprintingId = deviceFingerprint,
                card = Card(
                    cardNumber,
                    cardExpiry = CardExpiry(expMonth, expYear)
                )
            )
        )
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(Exception("Failed getting challengePayload!"))
        }
    }

    suspend fun authenticationsLookup(
        authenticationId: String
    ): Result<AuthenticationIdResponse> {
        val response = authService.authenticationsLookup(
            "$AUTHORIZATION_HEADER_START${getEncodedCredentials3DS()}",
            MERCHANT_ACCOUNT_NUMBER,
            authenticationId
        )

        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(Exception("Fail with authentication lookup!"))
        }
    }

    suspend fun authorize(
        token: String,
        amount: Int
    ): Result<AuthenticationIdResponse> {
        val response = authService.authorize(
            "$AUTHORIZATION_HEADER_START${getEncodedCredentialsCV()}",
            MERCHANT_ACCOUNT_NUMBER_CV,
            AuthorizeRequest(card = AuthorizeCard(token), amount = amount, billingDetails = BillingAddress())
        )

        return if (response.isSuccessful) {
            return Result.Success(response.body()!!)
        } else {
            Result.Error(Exception("Fail with card payment authorization!"))
        }
    }

    private fun getEncodedCredentials3DS() = Base64
        .encodeToString(
            ("$MERCHANT_API_KEY_ID_AUTH:$MERCHANT_API_KEY_PASSWORD_AUTH")
                .toByteArray(Charsets.UTF_8), Base64.NO_WRAP
        )

    private fun getEncodedCredentialsCV() = Base64
        .encodeToString(
            ("$MERCHANT_API_KEY_ID_AUTH_CV:$MERCHANT_API_KEY_PASSWORD_AUTH_CV")
                .toByteArray(Charsets.UTF_8), Base64.NO_WRAP
        )

    companion object {
        private const val AUTHORIZATION_HEADER_START = "Basic "

        //3DS merchant account credentials
        private const val MERCHANT_ACCOUNT_NUMBER = "<account-number>"
        private const val MERCHANT_API_KEY_ID_AUTH = "<api-key-id>"
        private const val MERCHANT_API_KEY_PASSWORD_AUTH =
            "<api-key-password>"

        //Customer Vault merchant account credentials
        private const val MERCHANT_ACCOUNT_NUMBER_CV = "<account-number>"
        private const val MERCHANT_API_KEY_ID_AUTH_CV = "<api-key-id>"
        private const val MERCHANT_API_KEY_PASSWORD_AUTH_CV =
            "<api-key-password>"
    }

}