package com.joaquim.joaquim_teste

import androidx.multidex.MultiDexApplication
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.commom.SharedPrefs
import com.joaquim.joaquim_teste.koinModules.EventData.injectEventsDataModule
import com.joaquim.joaquim_teste.koinModules.commom.injectCommonClassesModule
import com.joaquim.joaquim_teste.koinModules.home.injectHomeModule
import com.joaquim.joaquim_teste.koinModules.remoteData.injectRemoteDataModule
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
        initLoginPrefs()

    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }

        injectCommonClassesModule()
        injectHomeModule()
        injectEventsDataModule()
        injectRemoteDataModule()

    }

    private fun initObjectBox() {
        ObjectBox.init(this)
    }

    private fun initLoginPrefs() {
        SharedPrefs.getPrefs(globalContext)
    }

}