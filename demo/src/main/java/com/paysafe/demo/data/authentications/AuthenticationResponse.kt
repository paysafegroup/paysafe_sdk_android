/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.authentications

data class AuthenticationResponse(
    val id: String,
    val sdkChallengePayload: String?,
    val status: String?
)