/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

internal data class ChallengeData(
    val validateResponse: String?,
    val accountId: String,
    val authenticationId: String,
    val serverJwt: String?,
    val finalizeStatus: FinalizeStatus
)