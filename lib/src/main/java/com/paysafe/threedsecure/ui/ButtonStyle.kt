/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import android.graphics.Color
import android.graphics.Typeface
import com.paysafe.PaysafeDsl

/**
 * Class for styling a button
 */
class ButtonStyle private constructor(
    textColor: Int,
    textFontName: String,
    textFontSize: Int,
    val backgroundColor: Int,
    val cornerRadius: Int
) : BaseStyle(textColor, textFontName, textFontSize) {

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textColor: Int = Color.parseColor("#FFFFFF")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textFontName: String = Typeface.DEFAULT.toString()

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textFontSize: Int = 15

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var backgroundColor: Int = Color.parseColor("#173DA3")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var cornerRadius: Int = 1

        /**
         * Sets button text color
         * Used for better readability when building client in java code
         */
        fun withTextColor(textColor: Int) = also { this.textColor = textColor }

        /**
         * Sets button font
         * Used for better readability when building client in java code
         */
        fun withTextFontName(textFontName: String) = also { this.textFontName = textFontName }

        /**
         * Sets button text size
         * Used for better readability when building client in java code
         */
        fun withTextFontSize(textFontSize: Int) = also { this.textFontSize = textFontSize }

        /**
         * Sets button background color
         * Used for better readability when building client in java code
         */
        fun withBackgroundColor(backgroundColor: Int) = also { this.backgroundColor = backgroundColor }

        /**
         * Sets button corner radius
         * Used for better readability when building client in java code
         */
        fun withCornerRadius(cornerRadius: Int) = also { this.cornerRadius = cornerRadius }

        fun build() = ButtonStyle(
            textColor,
            textFontName,
            textFontSize,
            backgroundColor,
            cornerRadius
        )

    }
}