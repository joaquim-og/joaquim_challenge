package com.joaquim.joaquim_teste.data.commom.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.changeSeparator(): String = this.replace(".", ",")

fun String.toDate(): String {
    val date = Date(this.toLong())
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}