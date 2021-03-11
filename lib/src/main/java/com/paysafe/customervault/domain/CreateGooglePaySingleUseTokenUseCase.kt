/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.domain

import com.paysafe.Mockable
import com.paysafe.common.Error
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.api.CustomerVaultApi
import com.paysafe.util.Result

@Mockable
internal class CreateGooglePaySingleUseTokenUseCase(private val api: CustomerVaultApi) {

    internal operator fun invoke(params: GooglePayTokenParams, callback: (Result<GooglePaySingleUseToken, Error>) -> Unit) {
        api.createGooglePaySingleUseToken(params, callback)
    }

}