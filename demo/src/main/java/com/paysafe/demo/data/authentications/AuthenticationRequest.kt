/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.authentications

import java.security.SecureRandom

data class AuthenticationRequest(
    val amount: String,
    val currency: String = "USD",
    val merchantRefNum: String = twelveDigitRandomAlphanumeric(), //random string
    val merchantUrl: String = "https://merchantWebsite.com",
    val card: Card,
    val billingAddress: BillingAddress = BillingAddress(),
    val shippingDetails: ShippingDetails = ShippingDetails(),
    val profile: Profile = Profile(),
    val deviceFingerprintingId: String,
    val deviceChannel: String = "SDK",
    val requestorChallengePreference: String = "NO_PREFERENCE",
    val messageCategory: String = "PAYMENT",
    val transactionIntent: String = "GOODS_OR_SERVICE_PURCHASE",
    val authenticationPurpose: String = "PAYMENT_TRANSACTION",
    val maxAuthorizationsForInstalmentPayment: Int = 2,
    val initialPurchaseTime: String = "2019-07-08T14:47:31.540Z",
    val billingcycle: BillingCycle = BillingCycle(),
    val orderItemDetails: OrderItemDetails = OrderItemDetails(),
    val userAccountDetails: UserAccountDetails = UserAccountDetails()
)

data class Card(
    val cardNum: String,
    val PaymentToken: String = "PwgPaymentToken",
    val holderName: String = "Name ",
    val cardExpiry: CardExpiry
)

data class CardExpiry(
    val month: Int,
    val year: Int
)

data class BillingAddress(
    val city: String = "New York",
    val country: String = "US",
    val state: String = "AL",
    val street: String = "My street 1",
    val street2: String = "My street 2",
    val zip: String = "M5H 2N2"
)

data class ShippingDetails(
    val city: String = "New York",
    val country: String = "US",
    val state: String = "AL",
    val street: String = "My street 1",
    val street2: String = "My street 2",
    val zip: String = "CHY987",
    val shipMethod: String = "S"
)

data class Profile(
    val cellPhone: String = "+154657854697",
    val email: String = "example@example.com",
    val phone: String = "+154657854697"
)

data class BillingCycle(
    val endDate: String = "2014-01-26",
    val frequency: Int = 1
)

data class OrderItemDetails(
    val preOrderItemAvailabilityDate: String = "2014-01-26",
    val preOrderPurchaseIndicator: String = "MERCHANDISE_AVAILABLE",
    val reorderItemsIndicator: String = "FIRST_TIME_ORDER",
    val shippingIndicator: String = "SHIP_TO_BILLING_ADDRESS"
)

data class UserAccountDetails(
    val addCardAttemptsForLastDay: Int = 1,
    val changedDate: String = "2014-01-26",
    val changedRange: String = "DURING_TRANSACTION",
    val createdDate: String = "2014-01-26",
    val createdRange: String = "NO_ACCOUNT",
    val passwordChangedDate: String = "2014-01-26",
    val passwordChangedRange: String = "NO_CHANGE",
    val paymentAccountDetails: PaymentAccountDetails = PaymentAccountDetails(),
    // Use this only if you have valid prior 3DS Authentication data
    // val priorThreeDSAuthentication: PriorThreeDSAuthentication = PriorThreeDSAuthentication(),
    val shippingDetailsUsage: ShippingDetailsUsage = ShippingDetailsUsage(),
    val suspiciousAccountActivity: Boolean = true,
    val totalPurchasesSixMonthCount: Int = 1,
    val transactionCountForPreviousDay: Int = 1,
    val transactionCountForPreviousYear: Int = 3,
    val travelDetails: TravelDetails = TravelDetails(),
    val userLogin: UserLogin = UserLogin()
)

data class PaymentAccountDetails(
    val createdRange: String = "NO_ACCOUNT",
    val createdDate: String = "2014-01-26"
)

data class PriorThreeDSAuthentication(
    val data: String = "Some up to 2048 bytes undefined data",
    val method: String = "FRICTIONLESS_AUTHENTICATION",
    val id: String = "123e4567-e89b-12d3-a456-426655440000",
    val time: String = "2014-01-26T10:32:28Z"
)

data class ShippingDetailsUsage(
    val cardHolderNameMatch: Boolean = true,
    val initialUsageDate: String = "2014-01-26",
    val initialUsageRange: String = "CURRENT_TRANSACTION"
)


data class TravelDetails(
    val isAirTravel: Boolean = true,
    val airlineCarrier: String = "Wizz air",
    val departureDate: String = "2014-01-26",
    val destination: String = "BGN",
    val origin: String = "CHN",
    val passengerFirstName: String = "John",
    val passengerLastName: String = "Smith"
)

data class UserLogin(
    val authenticationMethod: String = "NO_LOGIN",
    val data: String = "Some up to 2048 bytes undefined data",
    val time: String = "2014-01-26T10:32:28Z"
)

fun twelveDigitRandomAlphanumeric(): String {
    val random = SecureRandom()
    return java.math.BigInteger(60, random).toString(32).toUpperCase()
}