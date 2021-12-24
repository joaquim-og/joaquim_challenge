package com.joaquim.joaquim_teste.data.commom.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.hide(){
    this.visibility = View.INVISIBLE
    this.isVisible = false
}

fun View.show(){
    this.visibility = View.VISIBLE
    this.isVisible = true
}