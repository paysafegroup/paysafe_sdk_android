/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

import com.paysafe.threedsecure.data.ChallengePayload

internal fun createChallenge() =
    ChallengePayload(
        "challengeAuthId",
        "challengeAccountId",
        "challengeTransactionId",
        "challengePayload",
        "2.1.0"
    )