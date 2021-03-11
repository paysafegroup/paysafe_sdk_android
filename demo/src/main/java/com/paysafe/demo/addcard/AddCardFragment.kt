/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.addcard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.paysafe.demo.PreviewViewModel
import com.paysafe.demo.R
import com.paysafe.demo.data.cards.PaymentCard
import com.paysafe.demo.databinding.FragmentAddCardBinding
import android.app.Activity
import android.view.inputmethod.InputMethodManager

class AddCardFragment : Fragment() {

    private lateinit var binding: FragmentAddCardBinding
    private lateinit var viewModel: PreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_card, container, false
        )

        binding.cardNumberField.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                //do nothing
            }

            override fun afterTextChanged(text: Editable) {
                val length = text.length
                if (text.isNotEmpty() && length % 5 == 0) {
                    if (text.last() == SPACE_CHAR) {
                        text.delete(length - 1, length)
                    } else if (Character.isDigit(text.last()) && length <= CARD_NUMBER_LENGTH_WITH_SPACES) {
                        text.insert(length - 1, SPACE_CHAR.toString())
                    }
                }
            }
        })

        binding.etExpDate.setOnFocusChangeListener { editText, focused ->
            if (focused) {
                fragmentManager?.let { fm ->
                    MonthYearPicker.newInstance().also {
                        it.setListener(object : MonthYearPicker.OnDateSelectedListener {
                            override fun onDateSelected(month: Int, year: Int) {
                                binding.etExpDate.setText(formatExpDate(month, year))
                                editText.clearFocus()
                            }
                        })
                        it.show(fm, MONTH_YEAR_DIALOG_TAG)
                    }

                }
            }

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())[PreviewViewModel::class.java]

        binding.addCardBtn.setOnClickListener {
            if (allFieldsCorrect()) {
                viewModel.saveNewCard(createCard())
                hideKeyboard()
                findNavController().popBackStack()
            }
        }
    }

    private fun allFieldsCorrect(): Boolean {
        var allFieldsValid = true
        if (!isValidCard()) allFieldsValid = false
        if (!isValidExpDate()) allFieldsValid = false
        if (!isValidCvv()) allFieldsValid = false
        return allFieldsValid
    }

    private fun isValidCard() =
        if (getCardNumberWithoutSpaces().length != CARD_NUMBER_LENGTH) {
            binding.cardNumberTextInputLayout.error = getString(R.string.error_card_number)
            false
        } else {
            binding.cardNumberTextInputLayout.error = ""
            true
        }

    private fun isValidExpDate() =
        if (binding.etExpDate.text.toString().isNullOrEmpty()) {
            binding.cardExpiryDate.error = getString(R.string.error_expiry_date)
            false
        } else {
            binding.cardExpiryDate.error = ""
            true
        }

    private fun isValidCvv() =
        if (binding.etCvv.text.toString().isNotEmpty() && binding.etCvv.text.toString().length != CVV_LENGTH) {
            binding.tiCvv.error = getString(R.string.error_cvv)
            false
        } else {
            binding.tiCvv.error = ""
            true
        }

    private fun createCard() =
        PaymentCard(
            getCardNumberWithoutSpaces(),
            binding.etCvv.text.toString(),
            binding.etExpDate.text.toString(),
            NEW_CARD_3DS_FLOW
        )

    private fun getCardNumberWithoutSpaces() =
        binding.cardNumberField.text.toString().replace(SPACE_CHAR.toString(), "")


    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

    }

    private fun formatExpDate(month: Int, year: Int): String =
        if (month < 10) "0$month/${year % 100}" else "$month/${year % 100}"

    companion object {
        private const val NEW_CARD_3DS_FLOW = "Unknown flow (New card)"
        private const val MONTH_YEAR_DIALOG_TAG = "MonthYearPickerDialog"
        private const val SPACE_CHAR = ' '
        private const val CARD_NUMBER_LENGTH = 16
        private const val CARD_NUMBER_LENGTH_WITH_SPACES = 19
        private const val CVV_LENGTH = 3
    }
}