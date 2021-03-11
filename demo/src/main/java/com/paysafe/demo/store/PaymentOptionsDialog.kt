/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.store

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.paysafe.demo.R
import com.paysafe.demo.store.StoreFragment.OnPaymentOptionClickListener


class PaymentOptionsDialog : DialogFragment() {

    private var listener: OnPaymentOptionClickListener? = null
    private val paymentOptions =
        PaymentOption.values().map { it.optionName }
            .toTypedArray()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.select_payment_option))
        builder.setItems(paymentOptions) { _, which ->
            when (which) {
                0 -> listener?.on3DSSelected()
                1 -> listener?.onCustomerVaultSelected()
                2 -> listener?.onGooglePaySelected()
                else -> dismiss()
            }
        }
        return builder.create()
    }

    fun setListener(listener: OnPaymentOptionClickListener) {
        this.listener = listener
    }

    companion object {
        fun newInstance() = PaymentOptionsDialog()
    }
}