/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe

import com.paysafe.common.Error

internal sealed class ApiResponse<out R>(open val headers: Map<String, String>?) {

    internal data class Success<out T>(internal val data: T, override val headers: Map<String, String>? = null) :
        ApiResponse<T>(headers)

    internal sealed class Failure(open val error: Error?, headers: Map<String, String>?) :
        ApiResponse<Nothing>(headers) {

        internal object ConnectionFailed : Failure(null, null)

        internal class InvalidMerchantConfiguration(override val error: Error?, override val headers: Map<String, String>? = null) :
            Failure(error, headers)

        internal class InvalidApiKey(override val error: Error?, override val headers: Map<String, String>? = null) :
            Failure(error, headers)

        internal class InternalSdkError(override val error: Error?, override val headers: Map<String, String>? = null) :
            Failure(error, headers)

    }

}