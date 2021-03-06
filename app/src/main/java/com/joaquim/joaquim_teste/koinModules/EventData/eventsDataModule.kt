package com.joaquim.joaquim_teste.koinModules.EventData

import com.joaquim.joaquim_teste.data.repository.checkin.CheckInBox
import com.joaquim.joaquim_teste.data.repository.event.EventBox
import com.joaquim.joaquim_teste.data.repository.user.UserBox
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectEventsDataModule() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            EventsInfo, UserInfo, EventCheckIn
        )
    )
}

val EventsInfo: Module = module {
    single { EventBox(get(), get()) }
}

val UserInfo: Module = module {
    single { UserBox() }
}

val EventCheckIn: Module = module {
    single { CheckInBox() }
}