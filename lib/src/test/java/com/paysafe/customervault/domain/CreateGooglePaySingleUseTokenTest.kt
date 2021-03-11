/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.domain

import com.paysafe.common.Error
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.api.CustomerVaultApi
import com.paysafe.customervault.data.googlePayTokenParams
import com.paysafe.customervault.newError
import com.paysafe.customervault.newGooglePaySingleUseToken
import com.paysafe.customervault.newGooglePayTokenParams
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.util.Result
import com.paysafe.whenEver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class CreateGooglePaySingleUseTokenTest {

    private val api = mock<CustomerVaultApi>()

    private val tested = CreateGooglePaySingleUseTokenUseCase(api)

    @Test
    fun `use case returns success`() {
        // given
        val params = newGooglePayTokenParams()

        val googlePaySingleUseToken = newGooglePaySingleUseToken()
        whenEver(
            api.createGooglePaySingleUseToken(
                safeAny(GooglePayTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<GooglePaySingleUseToken, Error>) -> Unit) {
                this(Result.Success(googlePaySingleUseToken))
            }
        }

        val mockCallback = mock<(Result<GooglePaySingleUseToken, Error>) -> Unit>()

        // when
        tested.invoke(params, mockCallback)

        // then
        Mockito.inOrder(api, mockCallback).apply {
            verify(api).createGooglePaySingleUseToken(safeEq(params), safeAny())
            verify(mockCallback).invoke(safeEq(Result.Success(googlePaySingleUseToken)))
        }
    }

    @Test
    fun `use case returns error`() {
        // given
        val params = newGooglePayTokenParams()

        val error = newError()

        whenEver(
            api.createGooglePaySingleUseToken(
                safeAny(GooglePayTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<GooglePaySingleUseToken, Error>) -> Unit) {
                this(Result.Failure(error))
            }
        }

        val mockCallback = mock<(Result<GooglePaySingleUseToken, Error>) -> Unit>()

        // when
        tested.invoke(params, mockCallback)

        // then
        Mockito.inOrder(api, mockCallback).apply {
            verify(api).createGooglePaySingleUseToken(safeEq(params), safeAny())
            verify(mockCallback).invoke(safeEq(Result.Failure(error)))
        }
    }
}