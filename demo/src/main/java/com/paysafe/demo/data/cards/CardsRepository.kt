/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CardsRepository {

    private val list = mutableListOf(
        PaymentCard(
            "4000000000001091",
            "123",
            "01/26",
            "3DS 2.0"
        ),
        PaymentCard(
            "4000000000001000",
            "123",
            "01/26",
            "Frictionless"
        )
    )

    private val cards = MutableLiveData<List<PaymentCard>>().apply {
        value = list
    }

    fun loadAllCards() : LiveData<List<PaymentCard>> = cards

    fun addCard(card: PaymentCard) {
        list.add(card)
        cards.value =
            list
    }

}