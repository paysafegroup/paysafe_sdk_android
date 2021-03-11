/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe

internal data class ApiRequest(
    internal val path: String,
    internal val body: Any,
    internal var headers: Map<String, String> = emptyMap()
)