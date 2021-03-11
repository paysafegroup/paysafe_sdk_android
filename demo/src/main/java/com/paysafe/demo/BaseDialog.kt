/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

class BaseDialog : DialogFragment() {

    private var message: String? = null
    private var positiveButton: String? = null
    private var negativeButton: String? = null
    private var positiveButtonClickListener: View.OnClickListener? = null
    private var negativeButtonClickListener: View.OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setPositiveButton(positiveButton) { _, _ ->
                positiveButtonClickListener?.onClick(view)
            }
            .setNegativeButton(negativeButton) { _, _ ->
                negativeButtonClickListener?.onClick(view)
            }
        return builder.create()
    }

    fun setMessage(messageText: String) {
        this.message = messageText
    }

    fun setPositiveButtonText(positiveButtonText: String) {
        positiveButton = positiveButtonText
    }

    fun setNegativeButtonText(negativeButtonText: String) {
        negativeButton = negativeButtonText
    }

    fun setPositiveButtonListener(listener: View.OnClickListener) {
        positiveButtonClickListener = listener
        isCancelable = false
    }

    fun setNegativeButtonListener(listener: View.OnClickListener) {
        negativeButtonClickListener = listener
    }

    companion object {
        fun newInstance() = BaseDialog()
    }

}