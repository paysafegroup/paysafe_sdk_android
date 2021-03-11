/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder

internal fun gson(block: GsonBuilder.() -> Unit) = GsonBuilder().apply(block).create()

internal fun GsonBuilder.dontSerialize(block: ExclusionStrategyBuilder.() -> Unit) {
    addSerializationExclusionStrategy(ExclusionStrategyBuilder().apply(block).build())
}

internal class ExclusionStrategyBuilder {

    private var fieldPredicate: (FieldAttributes) -> Boolean = { false }

    fun whenField(block: FieldAttributes.() -> Boolean) {
        fieldPredicate = block
    }

    fun build() = object : ExclusionStrategy {

        override fun shouldSkipClass(clazz: Class<*>?) = false

        override fun shouldSkipField(f: FieldAttributes?) = f?.let(fieldPredicate) ?: false

    }

}