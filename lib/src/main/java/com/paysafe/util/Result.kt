/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

internal sealed class Result<out R, out E> {

    data class Success<out T>(internal val data: T) : Result<T, Nothing>()

    data class Failure<out E>(internal val error: E) : Result<Nothing, E>()

}