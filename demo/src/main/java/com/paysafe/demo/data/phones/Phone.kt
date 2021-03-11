/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.phones

data class Phone internal constructor (val title: String,
                                       val description: String,
                                       val price: Double,
                                       val icon: Int)