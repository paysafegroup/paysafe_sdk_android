/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe

/**
 * The available Paysafe API environments to target.
 */
enum class Environment constructor(val url: String) {

    /**
     * The Paysafe Production environment.
     */
    LIVE("https://api.paysafe.com"),

    /**
     * The Paysafe Merchant Sandbox environment.
     */
    TEST("https://api.test.paysafe.com")
}