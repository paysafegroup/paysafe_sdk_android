/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.common

data class ErrorResponse(
    val liveMode: Boolean = false,
    val error: Error? = null
)
