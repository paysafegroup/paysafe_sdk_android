/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.util

import com.google.android.material.textfield.TextInputLayout
import com.paysafe.demo.R

fun TextInputLayout.checkEmptyField(): Boolean {
    val text = this.editText?.text?.toString()
    return when (text.isNullOrEmpty()) {
        true -> {
            this.error = context.getString(R.string.empty_field_error)
            false
        }
        else -> {
            this.error = ""
            true
        }
    }
}