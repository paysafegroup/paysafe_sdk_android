/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.googlepay

import com.google.android.gms.wallet.*
import org.json.JSONArray
import org.json.JSONObject

object GooglePayRequestHelper {

    fun createPaymentDataRequest(totalPrice: Int?): PaymentDataRequest {
        val request = PaymentDataRequest.fromJson(JSONObject().apply {
            put("apiVersion", GOOGLE_PAY_API_VERSION)
            put("apiVersionMinor", GOOGLE_PAY_API_VERSION_MINOR)
            put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod()))
            put("transactionInfo",createTransactionInfo(totalPrice))
        }.toString())
        return request;
    }

    private fun baseCardPaymentMethod(): JSONObject {
        return JSONObject().apply {

            val parameters = JSONObject().apply {
                put("allowedAuthMethods", allowedCardAuthMethods)
                put("allowedCardNetworks", allowedCardNetworks)
            }

            put("type", "CARD")
            put("parameters", parameters)
            put("tokenizationSpecification", getPaymentTokenizationParameters())
        }
    }

    private val allowedCardAuthMethods = JSONArray(listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS"))

    fun getGooglePayBaseConfigurationJson() =
        JSONObject().apply {
            put("apiVersion", GOOGLE_PAY_API_VERSION)
            put("apiVersionMinor", GOOGLE_PAY_API_VERSION_MINOR)
            put("allowedPaymentMethods", JSONArray().put(getBaseCardPaymentMethod()))
        }
            .toString()

    private fun createTransactionInfo(totalPrice: Int?) = JSONObject().apply {
        put("totalPrice", totalPrice.toString())
        put("totalPriceStatus", "FINAL")
        put("currencyCode", PAYMENT_CURRENCY)
    }

    private val allowedCardNetworks = JSONArray(listOf(
        "AMEX",
        "DISCOVER",
        "INTERAC",
        "MASTERCARD",
        "VISA"))

    private fun createCardRequirements() =
        CardRequirements.newBuilder()
            .addAllowedCardNetworks(
                listOf(
                    WalletConstants.CARD_NETWORK_AMEX,
                    WalletConstants.CARD_NETWORK_DISCOVER,
                    WalletConstants.CARD_NETWORK_VISA,
                    WalletConstants.CARD_NETWORK_MASTERCARD
                )
            )
            .build()

    private fun getBaseCardPaymentMethod() = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
        })
    }
    private fun getPaymentTokenizationParameters(): JSONObject {
        return JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", JSONObject(mapOf(
                "gateway" to PAYMENT_REQUEST_GATEWAY,
                "gatewayMerchantId" to PAYMENT_REQUEST_GATEWAY_MERCHANT_ID)))
        }
    }

    private const val GOOGLE_PAY_API_VERSION = 2
    private const val GOOGLE_PAY_API_VERSION_MINOR = 0
    private const val PAYMENT_CURRENCY = "USD"
    private const val PAYMENT_REQUEST_GATEWAY = "paysafe"
    private const val PAYMENT_REQUEST_GATEWAY_MERCHANT_ID = "<api-key-id>"

}