/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

import android.util.Base64
import java.nio.charset.Charset

internal fun String.base64Decode(charset: Charset = Charsets.UTF_8) = String(Base64.decode(this, Base64.DEFAULT), charset)

internal fun String.base64Encode(charset: Charset = Charsets.UTF_8) = String(Base64.encode(this.toByteArray(charset), Base64.DEFAULT), charset)