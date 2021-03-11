/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.store

enum class PaymentOption(val optionName: String) {
    S3D("3DS Payment"), CUSTOMER_VAULT("Customer Vault"), GOOGLE_PAY("Google Pay")
}