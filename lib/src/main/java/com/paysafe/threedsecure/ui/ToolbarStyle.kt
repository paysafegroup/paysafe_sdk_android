/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import com.paysafe.PaysafeDsl

/**
 * Class for styling the toolbar
 */
class ToolbarStyle private constructor(
    textColor: Int,
    textFontName: String,
    textFontSize: Int,
    val backgroundColor: Int,
    val buttonText: String,
    val headerText: String
) : BaseStyle(textColor, textFontName, textFontSize), Parcelable {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private constructor(parcel: Parcel) : this(
        textColor = parcel.readInt(),
        textFontName = parcel.readString()!!,
        textFontSize = parcel.readInt(),
        backgroundColor = parcel.readInt(),
        buttonText = parcel.readString()!!,
        headerText = parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(textColor)
        parcel.writeString(textFontName)
        parcel.writeInt(textFontSize)
        parcel.writeInt(backgroundColor)
        parcel.writeString(buttonText)
        parcel.writeString(headerText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToolbarStyle> {
        override fun createFromParcel(parcel: Parcel) = ToolbarStyle(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ToolbarStyle>(size)
    }

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
        var backgroundColor: Int = Color.parseColor("#F7F7F7")

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var buttonText: String = "CANCEL"

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var headerText: String = "Secure Checkout"

        /**
         * Sets toolbar text color
         * Used for better readability when building client in java code
         */
        fun withTextColor(textColor: Int) = also { this.textColor = textColor }

        /**
         * Sets toolbar font
         * Used for better readability when building client in java code
         */
        fun withTextFontName(textFontName: String) = also { this.textFontName = textFontName }

        /**
         * Sets toolbar font size
         * Used for better readability when building client in java code
         */
        fun withTextFontSize(textFontSize: Int) = also { this.textFontSize = textFontSize }

        /**
         * Sets toolbar background color
         * Used for better readability when building client in java code
         */
        fun withBackgroundColor(backgroundColor: Int) = also { this.backgroundColor = backgroundColor }

        /**
         * Sets toolbar button text
         * Used for better readability when building client in java code
         */
        fun withButtonText(buttonText: String) = also { this.buttonText = buttonText }

        /**
         * Sets toolbar header text
         * Used for better readability when building client in java code
         */
        fun withHeaderText(headerText: String) = also { this.headerText = headerText }

        fun build() = ToolbarStyle(
            textColor,
            textFontName,
            textFontSize,
            backgroundColor,
            buttonText,
            headerText
        )

    }
}
