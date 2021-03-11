/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.paysafe.demo.data.authentications.PaymentStatus
import com.paysafe.demo.googlepay.GooglePayFragment
import com.paysafe.demo.googlepay.GooglePayViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: GooglePayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(GooglePayViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GooglePayFragment.LOAD_PAYMENT_DATA_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val paymentData = PaymentData.getFromIntent(it)
                        viewModel.setPaymentData(Event(paymentData))
                    }
                }
                Activity.RESULT_CANCELED -> {
                    viewModel.setTransactionStatus(PaymentStatus.CANCELED.name)
                }
                AutoResolveHelper.RESULT_ERROR -> {
                    viewModel.setTransactionStatus(PaymentStatus.FAILED.name)
                }
            }
        }
    }
}
