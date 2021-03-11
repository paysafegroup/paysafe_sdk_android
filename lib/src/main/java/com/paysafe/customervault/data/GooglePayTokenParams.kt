/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data

import com.google.gson.annotations.SerializedName
import com.paysafe.PaysafeDsl

fun googlePayTokenParams(block: GooglePayTokenParams.Builder.() -> Unit) =
    GooglePayTokenParams.Builder().apply(block).build()

data class GooglePayTokenParams internal constructor(
    @SerializedName("googlePayPaymentToken") val googlePayPaymentToken: GooglePayPaymentToken
) {

    data class GooglePayPaymentToken internal constructor(
        val signature: String,
        val protocolVersion: String,
        val signedMessage: String
    ) {

        @PaysafeDsl
        class Builder {

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            lateinit var signature: String

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            lateinit var protocolVersion: String

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            lateinit var signedMessage: String

            fun withSignature(signature: String) = apply {
                this.signature = signature
            }

            fun withProtocolVersion(protocolVersion: String) = apply {
                this.protocolVersion = protocolVersion
            }

            fun withSignedMessage(signedMessage: String) = apply {
                this.signedMessage = signedMessage
            }

            fun build(): GooglePayPaymentToken {
                check(::signature.isInitialized) { "Signature is required, but missing" }
                check(::protocolVersion.isInitialized) { "Protocol version is required, but missing" }
                check(::signedMessage.isInitialized) { "Signed message is required, but missing" }

                return GooglePayPaymentToken(signature, protocolVersion, signedMessage)
            }
        }

        companion object {

            const val PROTOCOL_EC_V1 = "ECv1"

        }

    }

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        private lateinit var token: GooglePayPaymentToken

        fun googlePayPaymentToken(block: GooglePayPaymentToken.Builder.() -> Unit) {
            token = GooglePayPaymentToken.Builder().apply(block).build()
        }

        fun withToken(googlePayPaymentToken: GooglePayPaymentToken) = apply {
            this.token = googlePayPaymentToken
        }

        @Deprecated(
            message = "Can't be used inside a googlePayTokenParams block",
            level = DeprecationLevel.ERROR
        )
        fun googlePayTokenParams(block: GooglePayTokenParams.Builder.() -> Unit): Nothing =
            error("Unsupported operation")

        fun build(): GooglePayTokenParams {
            check(::token.isInitialized) { "Google Pay Payment Token is required, but missing" }

            return GooglePayTokenParams(token)
        }

    }

}

