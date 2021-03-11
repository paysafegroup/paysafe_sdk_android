/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.phones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.paysafe.demo.R

object PhonesRepository {

    fun loadAllItems(): LiveData<List<Phone>> {
        val phones = MutableLiveData<List<Phone>>()
        phones.value = listOf(
            Phone(
                "Phone",
                "Phone description",
                100.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                200.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                300.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                400.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                500.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                600.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                700.0,
                R.drawable.phone
            ),
            Phone(
                "Phone",
                "Phone description",
                800.0,
                R.drawable.phone
            )
        )
        return phones
    }

}