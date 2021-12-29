package com.joaquim.joaquim_teste.koinModules.remoteData

import com.joaquim.joaquim_teste.data.network.RemoteDataSourceEventInfo
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectRemoteDataModule() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            RemoteEventsInfo
        )
    )
}

val RemoteEventsInfo: Module = module {
    single { RemoteDataSourceEventInfo() }
}
