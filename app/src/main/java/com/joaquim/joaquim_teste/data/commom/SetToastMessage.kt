package com.joaquim.joaquim_teste.data.commom

import android.widget.Toast
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext

class SetToastMessage {

    private val context = globalContext

    fun setToastMessage(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

}