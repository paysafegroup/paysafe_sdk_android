/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.googlepay

import com.google.android.gms.wallet.*
import org.json.JSONArray
import org.json.JSONObject

object GooglePayRequestHelper {

    fun createPaymentDataRequest(totalPrice: Int?): PaymentDataRequest {
        val request = PaymentDataRequest.newBuilder()
            .setTransactionInfo(createTransactionInfo(totalPrice))
            .addAllowedPaymentMethods(
                listOf(
                    WalletConstants.PAYMENT_METHOD_CARD,
                    WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
                )
            )
            .setCardRequirements(createCardRequirements())
        request.setPaymentMethodTokenizationParameters(getPaymentTokenizationParameters())
        return request.build()
    }

    fun getGooglePayBaseConfigurationJson() =
        JSONObject().apply {
            put("apiVersion", GOOGLE_PAY_API_VERSION)
            put("apiVersionMinor", GOOGLE_PAY_API_VERSION_MINOR)
            put("allowedPaymentMethods", JSONArray().put(getBaseCardPaymentMethod()))
        }
            .toString()

    private fun createTransactionInfo(totalPrice: Int?) = TransactionInfo.newBuilder()
        .setTotalPriceStatus(
            WalletConstants.TOTAL_PRICE_STATUS_FINAL
        )
        .setTotalPrice(totalPrice.toString())
        .setCurrencyCode(PAYMENT_CURRENCY)
        .build()

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

    private fun getPaymentTokenizationParameters() =
        PaymentMethodTokenizationParameters.newBuilder()
            .setPaymentMethodTokenizationType(
                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY
            )
            .addParameter("gateway", PAYMENT_REQUEST_GATEWAY)
            .addParameter("gatewayMerchantId", PAYMENT_REQUEST_GATEWAY_MERCHANT_ID
            )
            .build()

    private const val GOOGLE_PAY_API_VERSION = 2
    private const val GOOGLE_PAY_API_VERSION_MINOR = 0
    private const val PAYMENT_CURRENCY = "USD"
    private const val PAYMENT_REQUEST_GATEWAY = "paysafe"
    private const val PAYMENT_REQUEST_GATEWAY_MERCHANT_ID = "<api-key-id>"

}