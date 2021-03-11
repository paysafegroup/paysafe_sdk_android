/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data

import com.google.gson.annotations.SerializedName
import com.paysafe.PaysafeDsl

data class BillingAddress internal constructor(
    @SerializedName("street") val street: String?,
    @SerializedName("street2") val street2: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String,
    @SerializedName("state") val state: String?,
    @SerializedName("zip") val zip: String
) {

    @PaysafeDsl
    class Builder {

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var street: String? = null

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var street2: String? = null

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var city: String? = null

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var country: String

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var state: String? = null

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var zip: String

        fun onStreet(street: String) = apply {
            this.street = street
        }

        fun onStreet2(street2: String?) = apply {
            this.street2 = street2
        }

        fun inCity(city: String) = apply {
            this.city = city
        }

        fun inCountry(country: String) = apply {
            this.country = country
        }

        fun inState(state: String?) = apply {
            this.state = state
        }

        fun withZip(zip: String) = apply {
            this.zip = zip
        }

        fun build(): BillingAddress {
            check(::country.isInitialized) { "Country is required, but missing" }
            check(::zip.isInitialized) { "ZIP is required, but missing" }

            return BillingAddress(
                this.street,
                this.street2,
                this.city,
                this.country,
                this.state,
                this.zip
            )
        }
    }

}