package com.joaquim.joaquim_teste.data.commom.extensions

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

fun String.changeSeparator(): String = this.replace(".", ",")

fun String.toDate(): String {
    val date = Date(this.toLong())
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}

fun String.fromHtml(context: Context, textView: TextView, localSource: String? = null): Spannable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this, Html.FROM_HTML_MODE_LEGACY) as Spannable
    } else {
        Html.fromHtml(this) as Spannable
    }
}

//Glide actual version only render images with https protocol
fun String.addHttpsIfNeeded(keepSameString: Boolean = true): String {
    return when {
        this.startsWith("https") -> this
        this.startsWith("http://") || this.contains("http://") -> this.replace("http", "https")
        else -> if (keepSameString) this else "https://$this"
    }
}