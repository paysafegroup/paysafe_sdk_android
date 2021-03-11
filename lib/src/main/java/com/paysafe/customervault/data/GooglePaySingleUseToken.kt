/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data

import com.google.gson.annotations.SerializedName

data class GooglePaySingleUseToken internal constructor(
    @SerializedName("id") val id: String = "",
    @SerializedName("paymentToken") val paymentToken: String = "",
    @SerializedName("timeToLiveSeconds") val timeToLive: Int = 0,
    @SerializedName("googlePayPaymentToken") val googlePayPaymentToken: GooglePayPaymentToken? = null,
    @SerializedName("card") val card: Card? = null
) {

    data class GooglePayPaymentToken internal constructor(
        @SerializedName("ephemeralPublicKey") val ephemeralPublicKey: String = "",
        @SerializedName("tag") val tag: String = "",
        @SerializedName("paymentMethod") val paymentMethod: String? = null,
        @SerializedName("messageId") val messageId: String? = null,
        @SerializedName("messageExpiration") val messageExpiration: String? = null
    )

    data class Card internal constructor(
        @SerializedName("status") val status: String,
        @SerializedName("cardBin") val cardBin: String,
        @SerializedName("lastDigits") val lastDigits: String,
        @SerializedName("cardType") val cardType: String
    )
}
