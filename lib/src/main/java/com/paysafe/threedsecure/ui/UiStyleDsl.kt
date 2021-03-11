/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

fun uiStyle(block: UiStyle.Builder.() -> Unit) = UiStyle.Builder().apply(block).build()