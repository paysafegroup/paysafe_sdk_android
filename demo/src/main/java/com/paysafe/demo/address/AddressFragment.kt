/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.paysafe.demo.PreviewViewModel
import com.paysafe.demo.R
import com.paysafe.demo.data.Address
import com.paysafe.demo.databinding.FragmentAddressBinding
import com.paysafe.demo.util.checkEmptyField

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var viewModel: PreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_address, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())[PreviewViewModel::class.java]
        binding.addAddressBtn.setOnClickListener {
            view?.clearFocus()
            if (checkAllFieldsCorrect()) {
                viewModel.saveAddress(createAddress())
                findNavController().popBackStack()
            }
        }
    }

    private fun createAddress() =
        Address(
            binding.addressLine1Field.text.toString(),
            binding.addressLine2Field.text.toString(),
            binding.cityField.text.toString(),
            binding.stateField.text.toString(),
            binding.countryField.text.toString(),
            binding.postcodeField.text.toString()
        )

    private fun checkAllFieldsCorrect(): Boolean {
        var allFieldsCorrect = true

        if (!binding.addressLine1TextInputLayout.checkEmptyField()) {
            allFieldsCorrect = false
        }
        if (!binding.cityTextInputLayout.checkEmptyField()) {
            allFieldsCorrect = false
        }
        if (!binding.stateTextInputLayout.checkEmptyField()) {
            allFieldsCorrect = false
        }
        if (!binding.countryTextInputLayout.checkEmptyField()) {
            allFieldsCorrect = false
        }
        if (!binding.postcodeTextInputLayout.checkEmptyField()) {
            allFieldsCorrect = false
        }
        return allFieldsCorrect
    }

}