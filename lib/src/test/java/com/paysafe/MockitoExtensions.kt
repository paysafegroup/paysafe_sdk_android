/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe

import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T: Any> whenEver(methodCall: T?): OngoingStubbing<T?> = Mockito.`when`(methodCall)

inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)

fun <T> safeAny(type: Class<T>): T {
    return Mockito.any<T>(type) ?: uninitialized()
}

fun <T> safeAny(): T {
    return Mockito.any() ?: uninitialized()
}

fun <T> safeEq(value: T) = ArgumentMatchers.eq(value) ?: value

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T