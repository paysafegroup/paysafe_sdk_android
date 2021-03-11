/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import android.graphics.Color
import android.graphics.Typeface
import com.paysafe.PaysafeDsl

/**
 * Class for styling the text labels
 */
class LabelStyle private constructor(
    textColor: Int,
    textFontName: String,
    textFontSize: Int,
    val headingTextColor: Int,
    val headingTextFontName: String,
    val headingTextFontSize: Int
) : BaseStyle(textColor, textFontName, textFontSize) {

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textColor: Int = Color.parseColor("#414141")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textFontName: String = Typeface.DEFAULT.toString()

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var textFontSize: Int = 16

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var headingTextColor: Int = Color.parseColor("#414141")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var headingTextFontName: String = Typeface.DEFAULT.toString()

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var headingTextFontSize: Int = 24

        /**
         * Sets label text color
         * Used for better readability when building client in java code
         */
        fun withTextColor(textColor: Int) = also { this.textColor = textColor }

        /**
         * Sets label text font
         * Used for better readability when building client in java code
         */
        fun withTextFontName(textFontName: String) = also { this.textFontName = textFontName }

        /**
         * Sets label text size
         * Used for better readability when building client in java code
         */
        fun withTextFontSize(textFontSize: Int) = also { this.textFontSize = textFontSize }

        /**
         * Sets label heading text color
         * Used for better readability when building client in java code
         */
        fun withHeadingTextColor(headingTextColor: Int) = also { this.headingTextColor = headingTextColor }

        /**
         * Sets label heading text font
         * Used for better readability when building client in java code
         */
        fun withHeadingTextFontName(headingTextFontName: String) = also { this.headingTextFontName = headingTextFontName }

        /**
         * Sets label heading text font size
         * Used for better readability when building client in java code
         */
        fun withHeadingTextFontSize(headingTextFontSize: Int) = also { this.headingTextFontSize = headingTextFontSize }

        fun build() = LabelStyle(
            textColor,
            textFontName,
            textFontSize,
            headingTextColor,
            headingTextFontName,
            headingTextFontSize
        )

    }

}