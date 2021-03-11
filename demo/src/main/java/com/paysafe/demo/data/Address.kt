/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data

data class Address(
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val country: String,
    val postCode: String
)
