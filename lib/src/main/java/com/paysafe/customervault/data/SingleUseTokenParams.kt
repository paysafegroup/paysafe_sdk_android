/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data

import com.google.gson.annotations.SerializedName
import com.paysafe.PaysafeDsl

fun singleUseTokenParams(block: SingleUseTokenParams.Builder.() -> Unit) =
    SingleUseTokenParams.Builder().apply(block).build()

data class SingleUseTokenParams internal constructor(
    @SerializedName("card") val card: Card
) {

    data class Card internal constructor(
        @SerializedName("holderName") val holderName: String,
        @SerializedName("cardNum") val cardNumber: String,
        @SerializedName("cvv") val cvv: String?,
        @SerializedName("cardExpiry") val cardExpiry: CardExpiry,
        @SerializedName("billingAddress") val billingAddress: BillingAddress
    ) {

        @PaysafeDsl
        class Builder {

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            lateinit var holderName: String

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            lateinit var cardNumber: String

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            private var cvv: String? = null

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            private lateinit var paramCardExpiry: CardExpiry

            @field:JvmSynthetic
            @get:JvmSynthetic
            @set:JvmSynthetic
            private lateinit var paramBillingAddress: BillingAddress

            fun withHolderName(holderName: String) = apply {
                this.holderName = holderName
            }

            fun withCardNumber(cardNumber: String) = apply {
                this.cardNumber = cardNumber
            }

            fun withCvv(cvv: String?) = apply {
                this.cvv = cvv
            }

            fun withCardExpiry(cardExpiry: CardExpiry) = apply {
                paramCardExpiry = cardExpiry
            }

            fun cardExpiry(block: CardExpiry.Builder.() -> Unit) {
                paramCardExpiry = CardExpiry.Builder().apply(block).build()
            }

            fun withBillingAddress(billingAddress: BillingAddress) = apply {
                this.paramBillingAddress = billingAddress
            }

            fun billingAddress(block: BillingAddress.Builder.() -> Unit) {
                paramBillingAddress = BillingAddress.Builder().apply(block).build()
            }

            fun build(): Card {
                check(::holderName.isInitialized) { "Holder name is required, but missing" }
                check(::cardNumber.isInitialized) { "Card number is required, but missing" }
                check(::paramCardExpiry.isInitialized) { "Card expiry is required, but missing" }
                check(::paramBillingAddress.isInitialized) { "Billing address is required, but missing" }

                return Card(holderName, cardNumber, cvv, paramCardExpiry, paramBillingAddress)
            }
        }

    }

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        private lateinit var singleUseTokenCard: Card

        fun card(block: Card.Builder.() -> Unit) {
            singleUseTokenCard = Card.Builder().apply(block).build()
        }

        fun withCard(card: Card) = apply {
            this.singleUseTokenCard = card
        }

        // This prevents the user to call the top - level DSL function inside the block
        @Deprecated(
            message = "Can't be used inside a singleUseTokenParams block",
            level = DeprecationLevel.ERROR
        )
        fun singleUseTokenParams(block: Builder.() -> Unit): Nothing =
            error("Unsupported operation")

        fun build(): SingleUseTokenParams {
            check(::singleUseTokenCard.isInitialized) { "Card is required, but missing" }

            return SingleUseTokenParams(singleUseTokenCard)
        }
    }

}

