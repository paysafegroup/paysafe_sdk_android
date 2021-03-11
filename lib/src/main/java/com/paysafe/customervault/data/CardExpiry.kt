/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data

import com.google.gson.annotations.SerializedName
import com.paysafe.PaysafeDsl

data class CardExpiry internal constructor(
    @SerializedName("month") val month: Int,
    @SerializedName("year") val year: Int
) {

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var month: Int = -1

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var year: Int = -1

        fun withMonth(month: Int) = apply {
            this.month = month
        }

        fun withYear(year: Int) = apply {
            this.year = year
        }

        fun build() : CardExpiry {
            check(month in 1..12) { "Month value must be between 1 and 12" }
            check(year >= 0) { "Year value must be non negative" }
            return CardExpiry(this.month, this.year)
        }
    }

}