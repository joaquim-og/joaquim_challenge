package com.joaquim.joaquim_teste.data.commom.extensions

import java.util.*

fun Long.toDate(): String = Date(this).toString()