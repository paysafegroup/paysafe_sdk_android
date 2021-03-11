/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

internal data class LogRequest(
    val eventType: EventType,
    val eventMessage: String,
    val sdk: Sdk = Sdk()
)