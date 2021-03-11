/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import android.graphics.Color
import android.graphics.Typeface
import com.paysafe.PaysafeDsl

/**
 * Class for styling the text boxes
 */
class TextBoxStyle private constructor(
    textColor: Int,
    textFontName: String,
    textFontSize: Int,
    val borderColor: Int,
    val borderWidth: Int,
    val cornerRadius: Int
) : BaseStyle(textColor, textFontName, textFontSize) {

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textColor: Int = Color.parseColor("#000000")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textFontName: String = Typeface.DEFAULT.toString()

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textFontSize: Int = 24

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var borderColor: Int = Color.parseColor("#173DA3")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var borderWidth: Int = 1

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var cornerRadius: Int = 5

        /**
         * Sets text box text color
         * Used for better readability when building client in java code
         */
        fun withTextColor(textColor: Int) = also { this.textColor = textColor }

        /**
         * Sets text box font
         * Used for better readability when building client in java code
         */
        fun withTextFontName(textFontName: String) = also { this.textFontName = textFontName }

        /**
         * Sets text box font size
         * Used for better readability when building client in java code
         */
        fun withTextFontSize(textFontSize: Int) = also { this.textFontSize = textFontSize }

        /**text box border color
         * Used for better readability when building client in java code
         */
        fun withBorderColor(borderColor: Int) = also { this.borderColor = borderColor }

        /**
         * Sets text box border width
         * Used for better readability when building client in java code
         */
        fun withBorderWidth(borderWidth: Int) = also { this.borderWidth = borderWidth }

        /**
         * Sets text box corner radius
         * Used for better readability when building client in java code
         */
        fun withCornerRadius(cornerRadius: Int) = also { this.cornerRadius = cornerRadius }


        fun build() = TextBoxStyle(
            textColor,
            textFontName,
            textFontSize,
            borderColor,
            borderWidth,
            cornerRadius
        )

    }


}