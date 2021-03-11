/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

import com.google.gson.annotations.SerializedName

internal data class JwtResponse(
    val id: String,
    @SerializedName("deviceFingerprintingId") val deviceFingerprintId: String,
    val jwt: String,
    val card: Card
)