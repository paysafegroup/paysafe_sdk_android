/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.store

import androidx.lifecycle.ViewModel
import com.paysafe.demo.data.phones.PhonesRepository

class StoreViewModel: ViewModel() {

    private val phonesRepository = PhonesRepository

    val phones by lazy {  phonesRepository.loadAllItems() }

}