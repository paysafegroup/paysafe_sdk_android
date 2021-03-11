/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault

fun customerVaultService(block: CustomerVaultService.Builder.() -> Unit) =
    CustomerVaultService.Builder().apply(block).build()
