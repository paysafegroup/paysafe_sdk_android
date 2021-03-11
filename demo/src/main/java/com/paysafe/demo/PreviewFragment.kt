/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.paysafe.demo.databinding.FragmentPreviewBinding
import com.paysafe.demo.data.authentications.PaymentStatus
import com.paysafe.demo.store.StoreFragment.Companion.PAYMENT_METHOD
import com.paysafe.demo.store.StoreFragment.Companion.TOTAL_AMOUNT_KEY
import com.paysafe.threedsecure.data.ChallengeResult.Companion.toChallengeResult

class PreviewFragment : Fragment() {

    private lateinit var binding: FragmentPreviewBinding
    private lateinit var viewModel: PreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_preview, container, false
        )
        binding.btnAddCard.setOnClickListener {
            findNavController().navigate(R.id.cardsFragment)
        }
        binding.btnAddAddress.setOnClickListener {
            findNavController().navigate(R.id.addressFragment)
        }
        binding.btnPay.setOnClickListener {
            if (viewModel.selectedCardAndAddress()) {
                viewModel.pay()
            } else {
                Toast.makeText(context, R.string.select_card_and_address, Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())[PreviewViewModel::class.java]
        binding.viewModel = viewModel
        getArgumentsFromBundle()
        addObservers()
    }

    private fun getArgumentsFromBundle(){
        arguments?.getDouble(TOTAL_AMOUNT_KEY)?.let {
            viewModel.setTotalPrice(it.toInt())
        }
        arguments?.getString(PAYMENT_METHOD)?.let {
            viewModel.setPaymentMethod(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHALLENGE_REQUEST_ID) {
            val challengeResult = data?.toChallengeResult()
            if (challengeResult?.isSuccessful == true) {
                challengeResult.authenticationId?.let {
                    viewModel.lookup(it)
                }
            } else {
                showAuthenticationDialog(getString(R.string.something_went_wrong))
            }
        }
    }

    private fun addObservers() {
        viewModel.card.observe(this, Observer {
            it?.let {
                binding.tvCardNumber.visibility = View.VISIBLE
            }
        })
        viewModel.address.observe(this, Observer {
            it?.let {
                binding.ivAddressCheckmark.visibility = View.VISIBLE
            }
        })

        viewModel.challengeResolution.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.startForResult(
                this@PreviewFragment,
                CHALLENGE_REQUEST_ID
            )
        })

        viewModel.transactionStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { status ->
                if (status == PaymentStatus.COMPLETED.name) {
                    showAuthenticationDialog(this@PreviewFragment.getString(R.string.transaction_successful), true)
                } else {
                    showAuthenticationDialog(this@PreviewFragment.getString(R.string.something_went_wrong))
                }
            }

        })
    }

    private fun showAuthenticationDialog(message: String, positive: Boolean = false){
        BaseDialog.newInstance().apply {
            setMessage(message)
            setPositiveButtonText("OK")
            if(positive){
                setPositiveButtonListener(View.OnClickListener {
                    findNavController().popBackStack(R.id.storeFragment, true)
                })
            }
            show(this@PreviewFragment.parentFragmentManager, AUTHENTICATION_DIALOG_TAG)
        }
    }

    companion object {
        private const val CHALLENGE_REQUEST_ID = 10
        const val AUTHENTICATION_DIALOG_TAG = "authentication_tag"
    }

}