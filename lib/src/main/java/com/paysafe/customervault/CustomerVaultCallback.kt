/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault

import com.paysafe.common.Error

interface CustomerVaultCallback<in T> {

    fun onSuccess(token: T)

    fun onError(error: Error)

}
