/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.cards

data class PaymentCard internal constructor(
    val number: String,
    val cvv: String?,
    val expDate: String,
    val flow3DS: String? = null
) {
    fun getCardLastDigits() = number.takeLast(CARD_NUMBER_LAST_DIGITS_TO_SHOW)

    companion object{
        private const val CARD_NUMBER_LAST_DIGITS_TO_SHOW = 4
    }
}