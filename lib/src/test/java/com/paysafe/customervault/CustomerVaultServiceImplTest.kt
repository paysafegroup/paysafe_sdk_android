/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

@file:Suppress("UNCHECKED_CAST")

package com.paysafe.customervault

import com.paysafe.common.Error
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.customervault.domain.CreateGooglePaySingleUseTokenUseCase
import com.paysafe.customervault.domain.CreateSingleUseTokenUseCase
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.util.Result
import com.paysafe.util.Scheduler
import com.paysafe.whenEver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder

@RunWith(JUnit4::class)
class CustomerVaultServiceImplTest {

    private val createSingleUseTokenUseCase = mock<CreateSingleUseTokenUseCase>()

    private val createGooglePaySingleUseTokenUseCase = mock<CreateGooglePaySingleUseTokenUseCase>()

    private val handlerCallback = object : Scheduler {
        override fun post(r: () -> Unit) {
            r();
        }
    }

    private val tested =
        CustomerVaultServiceImpl(
            createSingleUseTokenUseCase,
            createGooglePaySingleUseTokenUseCase,
            handlerCallback
        )

    @Test
    fun `createSingleUseToken returns token`() {
        // given
        val params = newSingleUseTokenParams()

        val token = newSingleUseToken()
        whenEver(
            createSingleUseTokenUseCase.invoke(
                safeAny(SingleUseTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<SingleUseToken, Error>) -> Unit) {
                this(Result.Success(token))
            }
        }

        val mockCallback = mock<CustomerVaultCallback<SingleUseToken>>()

        // when
        tested.createSingleUseToken(params, mockCallback)

        // then
        inOrder(createSingleUseTokenUseCase, mockCallback).apply {
            verify(createSingleUseTokenUseCase).invoke(safeEq(params), safeAny())
            verify(mockCallback).onSuccess(safeEq(token))
        }
    }

    @Test
    fun `createSingleUseToken returns failure`() {
        val params = newSingleUseTokenParams()

        val error = newError()

        whenEver(
            createSingleUseTokenUseCase.invoke(
                safeAny(SingleUseTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<SingleUseToken, Error>) -> Unit) {
                this(Result.Failure(error))
            }
        }

        val mockCallback = mock<CustomerVaultCallback<SingleUseToken>>()

        // when
        tested.createSingleUseToken(params, mockCallback)

        // then
        inOrder(createSingleUseTokenUseCase, mockCallback).apply {
            verify(createSingleUseTokenUseCase).invoke(safeEq(params), safeAny())
            verify(mockCallback).onError(safeEq(error))
        }
    }

    @Test
    fun `createGooglePaySingleUseToken return token`() {
        // given
        val params = newGooglePayTokenParams()

        val token = newGooglePaySingleUseToken()
        whenEver(
            createGooglePaySingleUseTokenUseCase.invoke(
                safeAny(GooglePayTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<GooglePaySingleUseToken, Error>) -> Unit) {
                this(Result.Success(token))
            }
        }

        val mockCallback = mock<CustomerVaultCallback<GooglePaySingleUseToken>>()

        // when
        tested.createGooglePayPaymentToken(params, mockCallback)

        // then
        inOrder(createGooglePaySingleUseTokenUseCase, mockCallback).apply {
            verify(createGooglePaySingleUseTokenUseCase).invoke(safeEq(params), safeAny())
            verify(mockCallback).onSuccess(safeEq(token))
        }
    }

    @Test
    fun `createGooglePaySingleUseToken returns failure`() {
        // given
        val params = newGooglePayTokenParams()

        val error = newError()
        whenEver(
            createGooglePaySingleUseTokenUseCase.invoke(
                safeAny(GooglePayTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<GooglePaySingleUseToken, Error>) -> Unit) {
                this(Result.Failure(error))
            }
        }

        val mockCallback = mock<CustomerVaultCallback<GooglePaySingleUseToken>>()

        // when
        tested.createGooglePayPaymentToken(params, mockCallback)

        // then
        inOrder(createGooglePaySingleUseTokenUseCase, mockCallback).apply {
            verify(createGooglePaySingleUseTokenUseCase).invoke(safeEq(params), safeAny())
            verify(mockCallback).onError(safeEq(error))
        }
    }

}