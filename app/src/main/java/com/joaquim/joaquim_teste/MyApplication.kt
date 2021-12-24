package com.joaquim.joaquim_teste

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.koinModules.commom.injectCommonClassesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : MultiDexApplication() {

    companion object {
        @get:Synchronized
        lateinit var globalContext: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = this

        initKoin()
        initObjectBox()

    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }

        injectCommonClassesModule()

    }

    private fun initObjectBox() {
        ObjectBox.init(this)
    }

}