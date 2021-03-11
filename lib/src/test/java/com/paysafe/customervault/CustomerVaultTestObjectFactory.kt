/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault

import com.paysafe.common.Error
import com.paysafe.customervault.data.*

internal fun newSingleUseTokenParams() =
    singleUseTokenParams {
        card {
            holderName = "Danail Alexiev"
            cardNumber = "4000 0000 0000 1091"

            cardExpiry {
                month = 10
                year = 2022
            }
            billingAddress {
                country = "Bulgaria"
                zip = "1000"
            }
        }
    }

internal fun newSingleUseToken() =
    SingleUseToken(
        id = "id",
        paymentToken = "paymentToken",
        timeToLive = 100
    )

internal fun newGooglePayTokenParams() =
    googlePayTokenParams {
        googlePayPaymentToken {
            protocolVersion = GooglePayTokenParams.GooglePayPaymentToken.PROTOCOL_EC_V1
            signature = "signature"
            signedMessage = "message"
        }
    }

internal fun newGooglePaySingleUseToken() = GooglePaySingleUseToken(
    id = "123",
    paymentToken = "paymentToken",
    timeToLive = 1
)

internal fun newError() = Error(
    code = "123",
    message = "Error message"
)
