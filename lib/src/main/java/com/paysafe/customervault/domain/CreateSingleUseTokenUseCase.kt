/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.domain

import com.paysafe.Mockable
import com.paysafe.common.Error
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.customervault.data.api.CustomerVaultApi
import com.paysafe.util.Result

@Mockable
internal class CreateSingleUseTokenUseCase(private val api: CustomerVaultApi) {

    internal operator fun invoke(params: SingleUseTokenParams, callback: (Result<SingleUseToken, Error>) -> Unit) {
        api.createSingleUseToken(params, callback)
    }

}