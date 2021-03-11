/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.util

internal class Event<out T>(private val value: T) {

    private var isHandled = false

    internal fun getValueIfNotHandled(): T? =
        if (isHandled) {
            null
        } else {
            isHandled = true
            value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        if (value != other.value) return false
        if (isHandled != other.isHandled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + isHandled.hashCode()
        return result
    }

    override fun toString(): String {
        return "Event(value=$value, isHandled=$isHandled)"
    }

}