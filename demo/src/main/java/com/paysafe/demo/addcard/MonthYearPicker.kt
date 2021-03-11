/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.addcard

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.paysafe.demo.R
import com.paysafe.demo.databinding.MonthYearPickerBinding
import java.util.Calendar

class MonthYearPicker: DialogFragment() {

    private var listener: OnDateSelectedListener? = null

    private lateinit var binding: MonthYearPickerBinding

    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.month_year_picker, null, false
        )

        binding.monthPickerNp.apply {
            minValue = MIN_MONTH
            maxValue = MAX_MONTH
            value = calendar.get(Calendar.MONTH) + 1
        }

        binding.yearPickerNp.apply {
            minValue = calendar.get(Calendar.YEAR)
            maxValue = minValue + 10
        }

        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton(DIALOG_POSITIVE_BUTTON) { _, _ ->
                listener?.onDateSelected(
                    binding.monthPickerNp.value,
                    binding.yearPickerNp.value
                )
            }
            .create()
    }

    fun setListener(listener: OnDateSelectedListener){
        this.listener = listener
    }

    companion object {

        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12
        private const val DIALOG_POSITIVE_BUTTON = "OK"

        fun newInstance() = MonthYearPicker()
    }

    interface OnDateSelectedListener {
        fun onDateSelected(month: Int, year: Int)
    }


}