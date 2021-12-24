package com.joaquim.joaquim_teste.data.commom

import android.content.Context
import com.joaquim.joaquim_teste.data.model.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }

}