/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe

fun paysafeApiClient(block: PaysafeApiClient.Builder.() -> Unit) = PaysafeApiClient.Builder().apply(block).build()
