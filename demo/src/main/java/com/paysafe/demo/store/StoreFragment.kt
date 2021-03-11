/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paysafe.demo.R
import com.paysafe.demo.data.phones.Phone
import com.paysafe.demo.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private var adapter = ItemsAdapter(object : OnBuyClickListener {
        override fun onBuyClicked(totalPrice: Double) {
            val dialog = PaymentOptionsDialog.newInstance().apply {
                setListener(object : OnPaymentOptionClickListener {
                    override fun on3DSSelected() {
                        findNavController().navigate(R.id.previewFragment, Bundle().apply {
                            putDouble(TOTAL_AMOUNT_KEY, totalPrice)
                            putString(PAYMENT_METHOD, PaymentOption.S3D.name)
                        })
                    }

                    override fun onCustomerVaultSelected() {
                        findNavController().navigate(R.id.previewFragment, Bundle().apply {
                            putDouble(TOTAL_AMOUNT_KEY, totalPrice)
                            putString(PAYMENT_METHOD, PaymentOption.CUSTOMER_VAULT.name)
                        })
                    }

                    override fun onGooglePaySelected() {
                        findNavController().navigate(R.id.googlePayFragment, Bundle().apply {
                            putDouble(TOTAL_AMOUNT_KEY, totalPrice)
                        })
                    }

                })
            }
            dialog.show(this@StoreFragment.requireFragmentManager(), PAYMENT_OPTION_DIALOG_TAG)
        }
    })
    private lateinit var binding: FragmentStoreBinding
    private lateinit var itemsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this)[StoreViewModel::class.java]
        viewModel.phones.observe(this, Observer<List<Phone>> { items ->
            items?.let {
                adapter.setItems(it)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_store, container, false
        )
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        itemsRecyclerView = binding.rvItems
        itemsRecyclerView.layoutManager = LinearLayoutManager(context)
        itemsRecyclerView.adapter = adapter
    }

    interface OnBuyClickListener {
        fun onBuyClicked(totalPrice: Double)
    }

    interface OnPaymentOptionClickListener{
        fun on3DSSelected()
        fun onGooglePaySelected()
        fun onCustomerVaultSelected()
    }

    companion object {
        const val TOTAL_AMOUNT_KEY = "total_amount"
        const val PAYMENT_METHOD = "payment_method"
        private const val PAYMENT_OPTION_DIALOG_TAG = "payment_option_dialog"
    }

}