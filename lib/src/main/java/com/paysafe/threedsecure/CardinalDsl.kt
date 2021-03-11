/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import android.content.Context
import android.graphics.Typeface
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.shared.models.enums.ButtonType
import com.cardinalcommerce.shared.userinterfaces.*
import com.paysafe.PaysafeDsl
import org.json.JSONArray

internal fun cardinal(block: CardinalBuilder.() -> Unit) = CardinalBuilder().apply(block).build()

@PaysafeDsl
internal class CardinalBuilder {

    lateinit var context: Context
    var environment = CardinalEnvironment.STAGING
    var timeout: Long = 0
    var renderTypes = listOf(
        CardinalRenderType.OTP,
        CardinalRenderType.HTML,
        CardinalRenderType.MULTI_SELECT,
        CardinalRenderType.SINGLE_SELECT,
        CardinalRenderType.OOB
    )
    var uiType = CardinalUiType.BOTH

    private var uiCustomization = UiCustomization()

    fun uiStyle(block: UiCustomizationBuilder.() -> Unit) {
        uiCustomization = UiCustomizationBuilder().apply(block).build()
    }

    internal fun build(): Cardinal {
        check(::context.isInitialized) { "Required context not set" }

        return Cardinal.getInstance().also { it.configure(context, createConfigurationParams()) }
    }

    private fun createConfigurationParams() =
        CardinalConfigurationParameters().apply {
            environment = this@CardinalBuilder.environment
            requestTimeout = this@CardinalBuilder.timeout.toInt()

            renderType = JSONArray().apply {
                renderTypes.forEach {
                    put(it)
                }
            }
            uiCustomization = this@CardinalBuilder.uiCustomization
            uiType = this@CardinalBuilder.uiType
            isEnableDFSync = true
            isEnableQuickAuth = true
        }

}

@PaysafeDsl
internal class UiCustomizationBuilder {

    private var toolbarCustomization: ToolbarCustomization? = null
    private var labelCustomization: LabelCustomization? = null
    private var textBoxCustomization: TextBoxCustomization? = null
    private var cancelButtonCustomization: ButtonCustomization? = null
    private var nextButtonCustomization: ButtonCustomization? = null
    private var continueButtonCustomization: ButtonCustomization? = null
    private var resendButtonCustomization: ButtonCustomization? = null
    private var verifyButtonCustomization: ButtonCustomization? = null

    fun toolbarStyle(block: ToolbarCustomizationBuilder.() -> Unit) {
        toolbarCustomization = ToolbarCustomizationBuilder().apply(block).build()
    }

    fun labelStyle(block: LabelCustomizationBuilder.() -> Unit) {
        labelCustomization = LabelCustomizationBuilder().apply(block).build()
    }

    fun textBoxStyle(block: TextBoxCustomizationBuilder.() -> Unit) {
        textBoxCustomization = TextBoxCustomizationBuilder().apply(block).build()
    }

    fun cancelButtonStyle(block: ButtonCustomizationBuilder.() -> Unit) {
        cancelButtonCustomization = ButtonCustomizationBuilder().apply(block).build()
    }

    fun nextButtonStyle(block: ButtonCustomizationBuilder.() -> Unit) {
        nextButtonCustomization = ButtonCustomizationBuilder().apply(block).build()
    }

    fun continueButtonStyle(block: ButtonCustomizationBuilder.() -> Unit) {
        continueButtonCustomization = ButtonCustomizationBuilder().apply(block).build()
    }

    fun resendButtonStyle(block: ButtonCustomizationBuilder.() -> Unit) {
        resendButtonCustomization = ButtonCustomizationBuilder().apply(block).build()
    }

    fun verifyButtonStyle(block: ButtonCustomizationBuilder.() -> Unit) {
        verifyButtonCustomization = ButtonCustomizationBuilder().apply(block).build()
    }

    internal fun build() = UiCustomization().apply {
        this@UiCustomizationBuilder.toolbarCustomization?.let { toolbarCustomization = it }
        this@UiCustomizationBuilder.labelCustomization?.let { labelCustomization = it }
        this@UiCustomizationBuilder.textBoxCustomization?.let { textBoxCustomization = it }
        this@UiCustomizationBuilder.cancelButtonCustomization?.let { setButtonCustomization(it, ButtonType.CANCEL) }
        this@UiCustomizationBuilder.nextButtonCustomization?.let { setButtonCustomization(it, ButtonType.NEXT) }
        this@UiCustomizationBuilder.continueButtonCustomization?.let { setButtonCustomization(it, ButtonType.CONTINUE) }
        this@UiCustomizationBuilder.resendButtonCustomization?.let { setButtonCustomization(it, ButtonType.RESEND) }
        this@UiCustomizationBuilder.verifyButtonCustomization?.let { setButtonCustomization(it, ButtonType.VERIFY) }
    }

}

internal abstract class CustomizationBuilder<T : Customization> {

    var textColor: String = ""
    var textFontName: String = Typeface.DEFAULT.toString()
    var textFontSize: Int = 0

    protected fun build(creator: () -> T): T {
        return creator().apply {
            textColor = this@CustomizationBuilder.textColor
            textFontName = this@CustomizationBuilder.textFontName
            textFontSize = this@CustomizationBuilder.textFontSize
        }
    }

}

@PaysafeDsl
internal class ButtonCustomizationBuilder : CustomizationBuilder<ButtonCustomization>() {

    var backgroundColor: String = ""
    var cornerRadius: Int = 0

    internal fun build() = build {
        ButtonCustomization().apply {
            backgroundColor = this@ButtonCustomizationBuilder.backgroundColor
            cornerRadius = this@ButtonCustomizationBuilder.cornerRadius
        }
    }

}

@PaysafeDsl
internal class ToolbarCustomizationBuilder : CustomizationBuilder<ToolbarCustomization>() {

    var backgroundColor: String = ""
    var buttonText: String = ""
    var headerText: String = ""

    internal fun build() = build {
        ToolbarCustomization().apply {
            backgroundColor = this@ToolbarCustomizationBuilder.backgroundColor
            buttonText = this@ToolbarCustomizationBuilder.buttonText
            headerText = this@ToolbarCustomizationBuilder.headerText
        }
    }

}

@PaysafeDsl
internal class LabelCustomizationBuilder : CustomizationBuilder<LabelCustomization>() {

    var headingTextColor: String = ""
    var headingTextFontName: String = Typeface.DEFAULT.toString()
    var headingTextFontSize: Int = 0

    internal fun build() = build {
        LabelCustomization().apply {
            headingTextColor = this@LabelCustomizationBuilder.headingTextColor
            headingTextFontName = this@LabelCustomizationBuilder.headingTextFontName
            headingTextFontSize = this@LabelCustomizationBuilder.headingTextFontSize
        }
    }

}

@PaysafeDsl
internal class TextBoxCustomizationBuilder : CustomizationBuilder<TextBoxCustomization>() {

    var borderColor: String = ""
    var borderWidth: Int = 0
    var cornerRadius: Int = 0

    internal fun build() = build {
        TextBoxCustomization().apply {
            borderColor = this@TextBoxCustomizationBuilder.borderColor
            borderWidth = this@TextBoxCustomizationBuilder.borderWidth
            cornerRadius = this@TextBoxCustomizationBuilder.cornerRadius
        }
    }

}
