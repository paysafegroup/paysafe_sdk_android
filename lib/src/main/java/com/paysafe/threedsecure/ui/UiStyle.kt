/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import com.paysafe.Mockable
import com.paysafe.PaysafeDsl

/**
 * Class that is responsible for styling of the UI widgets
 */
@Mockable
class UiStyle internal constructor(
    var toolbarStyle: ToolbarStyle? = null,
    var labelStyle: LabelStyle? = null,
    var textBoxStyle: TextBoxStyle? = null,
    var cancelButtonStyle: ButtonStyle? = null,
    var nextButtonStyle: ButtonStyle? = null,
    var continueButtonStyle: ButtonStyle? = null,
    var resendButtonStyle: ButtonStyle? = null,
    var verifyButtonStyle: ButtonStyle? = null
) {

    @PaysafeDsl
    class Builder {

        private var toolbarStyle: ToolbarStyle? = null
        private var labelStyle: LabelStyle? = null
        private var textBoxStyle: TextBoxStyle? = null
        private var cancelButtonStyle: ButtonStyle? = null
        private var nextButtonStyle: ButtonStyle? = null
        private var continueButtonStyle: ButtonStyle? = null
        private var resendButtonStyle: ButtonStyle? = null
        private var verifyButtonStyle: ButtonStyle? = null

        @JvmSynthetic
        fun toolbarStyle(block: ToolbarStyle.Builder.() -> Unit) {
            toolbarStyle = ToolbarStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun labelStyle(block: LabelStyle.Builder.() -> Unit) {
            labelStyle = LabelStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun textBoxStyle(block: TextBoxStyle.Builder.() -> Unit) {
            textBoxStyle = TextBoxStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun cancelButtonStyle(block: ButtonStyle.Builder.() -> Unit) {
            cancelButtonStyle = ButtonStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun nextButtonStyle(block: ButtonStyle.Builder.() -> Unit) {
            nextButtonStyle = ButtonStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun continueButtonStyle(block: ButtonStyle.Builder.() -> Unit) {
            continueButtonStyle = ButtonStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun resendButtonStyle(block: ButtonStyle.Builder.() -> Unit) {
            resendButtonStyle = ButtonStyle.Builder().apply(block).build()
        }

        @JvmSynthetic
        fun verifyButtonStyle(block: ButtonStyle.Builder.() -> Unit) {
            verifyButtonStyle = ButtonStyle.Builder().apply(block).build()
        }

        fun withToolbarStyle(toolbarStyle: ToolbarStyle?) = also { this.toolbarStyle = toolbarStyle }

        fun withLabelStyle(labelStyle: LabelStyle?) = also { this.labelStyle = labelStyle }

        fun withTextBoxStyle(textBoxStyle: TextBoxStyle?) = also { this.textBoxStyle = textBoxStyle }

        fun withCancelButtonStyle(cancelButtonStyle: ButtonStyle?) = also { this.cancelButtonStyle = cancelButtonStyle }

        fun withNextButtonStyle(nextButtonStyle: ButtonStyle?) = also { this.nextButtonStyle = nextButtonStyle }

        fun withContinueButtonStyle(continueButtonStyle: ButtonStyle?) = also { this.continueButtonStyle = continueButtonStyle }

        fun withResendButtonStyle(resendButtonStyle: ButtonStyle?) = also { this.resendButtonStyle = resendButtonStyle }

        fun withVerifyButtonStyle(verifyButtonStyle: ButtonStyle?) = also { this.verifyButtonStyle = verifyButtonStyle }

        fun build() = UiStyle().apply {
            this@Builder.toolbarStyle?.let { toolbarStyle = it }
            this@Builder.labelStyle?.let { labelStyle = it }
            this@Builder.textBoxStyle?.let { textBoxStyle = it }
            this@Builder.cancelButtonStyle?.let { cancelButtonStyle = it }
            this@Builder.nextButtonStyle?.let { nextButtonStyle = it }
            this@Builder.continueButtonStyle?.let { continueButtonStyle = it }
            this@Builder.resendButtonStyle?.let { resendButtonStyle = it }
            this@Builder.verifyButtonStyle?.let { verifyButtonStyle = it }
        }

    }

}