/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

internal data class JwtRequest(
    val accountId: String,
    val card: Card,
    val sdk: Sdk = Sdk()
)