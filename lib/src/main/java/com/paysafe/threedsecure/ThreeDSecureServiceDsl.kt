/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

fun threeDSecureService(block: ThreeDSecureService.Builder.() -> Unit) =
    ThreeDSecureService.Builder().apply(block).build()
