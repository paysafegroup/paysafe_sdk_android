/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.domain

import com.paysafe.common.Error
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.customervault.data.api.CustomerVaultApi
import com.paysafe.customervault.newError
import com.paysafe.customervault.newSingleUseToken
import com.paysafe.customervault.newSingleUseTokenParams
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.util.Result
import com.paysafe.whenEver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class CreateSingleUseTokenUseCaseTest {

    private val api = mock<CustomerVaultApi>()

    private val tested = CreateSingleUseTokenUseCase(api)

    @Test
    fun `use case returns success`() {
        // given
        val params = newSingleUseTokenParams()

        val singleUseToken = newSingleUseToken()

        whenEver(
            api.createSingleUseToken(
                safeAny(SingleUseTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<SingleUseToken, Error>) -> Unit) {
                this(Result.Success(singleUseToken))
            }
        }

        val mockCallback = mock<(Result<SingleUseToken, Error>) -> Unit>()

        // when
        tested.invoke(params, mockCallback)

        // then
        inOrder(api, mockCallback).apply {
            verify(api).createSingleUseToken(safeEq(params), safeAny())
            verify(mockCallback).invoke(safeEq(Result.Success(singleUseToken)))
        }
    }

    @Test
    fun `use case returns error`() {
        // given
        val params = newSingleUseTokenParams()

        val error = newError()

        whenEver(
            api.createSingleUseToken(
                safeAny(SingleUseTokenParams::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<SingleUseToken, Error>) -> Unit) {
                this(Result.Failure(error))
            }
        }

        val mockCallback = mock<(Result<SingleUseToken, Error>) -> Unit>()

        // when
        tested.invoke(params, mockCallback)

        // then
        inOrder(api, mockCallback).apply {
            verify(api).createSingleUseToken(safeEq(params), safeAny())
            verify(mockCallback).invoke(safeEq(Result.Failure(error)))
        }
    }
}