/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.util.Result
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.whenEver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.inOrder

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class FinalizeUseCaseTest {

    private val api = mock<NetbanxApi>()

    private val tested = FinalizeUseCase(api)

    @Test
    fun `finalizeUseCase returns success`() {
        // given
        val accountId = "accountId"
        val authenticationId = "authenticationId"
        val serverJwt = "serverJwt"
        val finalizeStatus = FinalizeStatus.SUCCESSFUL

        val challengeResponse =
            ChallengeData(null, accountId, authenticationId, serverJwt, finalizeStatus)

        whenEver(
            api.finalize(
                safeEq(accountId),
                safeEq(authenticationId),
                safeEq(serverJwt),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[3] as (Result<Unit, ThreeDSecureError>) -> Unit) { this(Result.Success(Unit)) }
        }

        val mockedCallback = mock<(Result<Unit, ThreeDSecureError>) -> Unit>()

        // when
        tested(challengeResponse, mockedCallback)

        // then
        inOrder(api, mockedCallback).apply {
            verify(api).finalize(
                safeEq(accountId),
                safeEq(authenticationId),
                safeEq(serverJwt),
                safeAny()
            )
            verify(mockedCallback).invoke(safeEq(Result.Success(Unit)))
        }
    }

    @Test
    fun `finalizeUseCase returns failure`() {
        val accountId = "accountId"
        val authenticationId = "authenticationId"
        val serverJwt = "serverJwt"

        val finalizeStatus = FinalizeStatus.FAILED

        val challengeResponse =
            ChallengeData(null, accountId, authenticationId, serverJwt, finalizeStatus)

        val error =
            ThreeDSecureError(
                code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                detailedMessage = "message"
            )

        whenEver(
            api.finalize(
                safeEq(accountId),
                safeEq(authenticationId),
                safeEq(serverJwt),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[3] as (Result<Unit, ThreeDSecureError>) -> Unit) { this(Result.Failure(error)) }
        }

        val mockedCallback = mock<(Result<Unit, ThreeDSecureError>) -> Unit>()

        // when
        tested(challengeResponse, mockedCallback)

        // then
        inOrder(api, mockedCallback).apply {
            verify(api).finalize(
                safeEq(accountId),
                safeEq(authenticationId),
                safeEq(serverJwt),
                safeAny()
            )
            verify(api).log(
                    safeEq(EventType.INTERNAL_SDK_ERROR),
                    safeEq("Error occurred during challenge for authentication: " +
                            "${challengeResponse.authenticationId} failed with " +
                            "${challengeResponse.validateResponse}")
            )
            verify(api).log(
                safeEq(EventType.INTERNAL_SDK_ERROR),
                safeEq("/finalize call failed")
            )
            verify(mockedCallback).invoke(safeEq(Result.Failure(error)))
        }
    }

    @Test
    fun `finalizeUseCase passes null if serverJwt is blank`() {
        `finalizeUseCase passes null if serverJwt is`("")
    }

    @Test
    fun `finalizeUseCase passes null if serverJwt is null`() {
        `finalizeUseCase passes null if serverJwt is`(null)
    }

    private fun `finalizeUseCase passes null if serverJwt is`(serverJwt: String?) {
        // given
        val accountId = "accountId"
        val authenticationId = "authenticationId"
        val finalizeStatus = FinalizeStatus.SUCCESSFUL

        val challengeResponse =
            ChallengeData(null, accountId, authenticationId, serverJwt, finalizeStatus)

        whenEver(
            api.finalize(
                safeEq(accountId),
                safeEq(authenticationId),
                ArgumentMatchers.isNull(),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[3] as (Result<Unit, ThreeDSecureError>) -> Unit) { this(Result.Success(Unit)) }
        }

        val mockedCallback = mock<(Result<Unit, ThreeDSecureError>) -> Unit>()

        // when
        tested(challengeResponse, mockedCallback)

        // then
        inOrder(api, mockedCallback).apply {
            verify(api).finalize(
                safeEq(accountId),
                safeEq(authenticationId),
                ArgumentMatchers.isNull(),
                safeAny()
            )
            verify(mockedCallback).invoke(safeEq(Result.Success(Unit)))
        }
    }
}