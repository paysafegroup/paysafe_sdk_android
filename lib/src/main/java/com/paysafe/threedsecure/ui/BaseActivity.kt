/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

open class BaseActivity : AppCompatActivity() {

    internal lateinit var factory: ViewModelProvider.Factory

    internal inline fun <reified VM : BaseViewModel> getViewModel() =
        ViewModelProviders.of(this, factory).get(VM::class.java)

}