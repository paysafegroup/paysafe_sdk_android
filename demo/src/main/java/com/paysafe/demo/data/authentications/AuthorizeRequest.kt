/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.authentications

data class AuthorizeRequest(
    val card: AuthorizeCard,
    val merchantRefNum: String = twelveDigitRandomAlphanumeric(),
    val amount: Int,
    val billingDetails: BillingAddress
)

data class AuthorizeCard(
    val paymentToken: String
)