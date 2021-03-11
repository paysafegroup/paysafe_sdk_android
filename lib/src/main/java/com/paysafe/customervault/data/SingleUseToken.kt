/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data

import com.google.gson.annotations.SerializedName

data class SingleUseToken internal constructor(
    @SerializedName("id") val id: String = "",
    @SerializedName("paymentToken") val paymentToken: String = "",
    @SerializedName("timeToLiveSeconds") val timeToLive: Int = 0,
    @SerializedName("card") val card: Card? = null,
    @SerializedName("billingAddress") val billingAddress: BillingAddress? = null
) {

    data class Card internal constructor(
        @SerializedName("cardBin") val cardBin: String,
        @SerializedName("lastDigits") val lastDigits: String,
        @SerializedName("holderName") val holderName: String,
        @SerializedName("cardType") val cardType: String,
        @SerializedName("cardExpiry") val cardExpiry: CardExpiry
    )

}

