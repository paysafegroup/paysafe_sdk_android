/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.domain

import com.google.gson.JsonObject
import com.paysafe.Mockable
import com.paysafe.common.Error
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.api.CustomerVaultApi
import com.paysafe.util.Result
import org.json.JSONObject

@Mockable
internal class CreateGooglePaySingleUseTokenWithFullPayloadUseCase(private val api: CustomerVaultApi) {

    internal operator fun invoke(googlePayTokenParams: JsonObject, callback: (Result<GooglePaySingleUseToken, Error>) -> Unit) {
        val body = JsonObject()
        body.add("googlePayPaymentToken",googlePayTokenParams)
        api.createGooglePaySingleUseToken(body, callback)
    }

}