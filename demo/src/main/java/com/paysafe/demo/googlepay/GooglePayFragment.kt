/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.googlepay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.paysafe.demo.R
import com.paysafe.demo.databinding.FragmentGooglePayBinding
import com.paysafe.demo.store.StoreFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.wallet.AutoResolveHelper
import com.paysafe.demo.BaseDialog
import com.paysafe.demo.PreviewFragment
import com.paysafe.demo.data.authentications.PaymentStatus
import com.paysafe.demo.util.isEmulator


class GooglePayFragment : Fragment() {

    private lateinit var binding: FragmentGooglePayBinding
    private lateinit var viewModel: GooglePayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_google_pay, container, false
        )
        binding.btnGooglePay.setOnClickListener {
            AutoResolveHelper.resolveTask(
                viewModel.paymentsClient.loadPaymentData(
                    GooglePayRequestHelper.createPaymentDataRequest(
                        viewModel.totalPrice.value
                    )
                ),
                requireActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())[GooglePayViewModel::class.java]
        binding.viewModel = viewModel
        arguments?.getDouble(StoreFragment.TOTAL_AMOUNT_KEY)?.let {
            viewModel.setTotalPrice(it.toInt())
        }

        if (isEmulator()) {
            showAuthenticationDialog(getString(R.string.google_pay_not_available_emulator), true)
        } else {
            viewModel.initializePaymentsClient(requireActivity())

            viewModel.startIsReadyToPayTask()

            addObservers()
        }

    }

    private fun addObservers() {
        viewModel.isReadyToPay.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.btnGooglePay.visibility = View.VISIBLE
            } else {
                showAuthenticationDialog(getString(R.string.google_pay_not_available), true)
            }
        })
        viewModel.paymentData.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.also { paymentData ->
                viewModel.pay(paymentData)
            }
        })
        viewModel.transactionStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { status ->
                when (status) {
                    PaymentStatus.COMPLETED.name -> showAuthenticationDialog(
                        this.getString(R.string.transaction_successful),
                        true
                    )
                    PaymentStatus.CANCELED.name -> findNavController().popBackStack()
                    else -> showAuthenticationDialog(status, true)
                }
            }
        })
        viewModel.googlePaySingleUseTokenStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { token ->
                showAuthenticationDialog(
                    this.getString(R.string.token_id) + token.id, true)
            }
        })

    }

    private fun showAuthenticationDialog(message: String, positive: Boolean = false) {
        BaseDialog.newInstance().apply {
            setMessage(message)
            setPositiveButtonText("OK")
            if (positive) {
                setPositiveButtonListener(View.OnClickListener {
                    findNavController().popBackStack()
                })
            }
            show(
                this@GooglePayFragment.parentFragmentManager,
                PreviewFragment.AUTHENTICATION_DIALOG_TAG
            )
        }
    }

    companion object {
        const val LOAD_PAYMENT_DATA_REQUEST_CODE = 11
    }

}