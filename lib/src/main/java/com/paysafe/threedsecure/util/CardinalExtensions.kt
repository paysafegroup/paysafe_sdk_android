/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.util

import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import org.json.JSONObject

internal fun ValidateResponse?.toJson(): String {
    val jsonObject = JSONObject()
    this?.let {
        jsonObject.put("validated", isValidated)
        jsonObject.put("actionCode", actionCode)
        jsonObject.put("errorNumber", errorNumber)
        jsonObject.put("errorDescription", errorDescription)

        payment?.let {
            val paymentObject = JSONObject()
            paymentObject.put("type", it.type)
            paymentObject.put("processorTransactionId", it.processorTransactionId)

            jsonObject.put("payment", paymentObject)
        }
    }
    return jsonObject.toString()
}