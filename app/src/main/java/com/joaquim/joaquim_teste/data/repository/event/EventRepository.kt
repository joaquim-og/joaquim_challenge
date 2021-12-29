package com.joaquim.joaquim_teste.data.repository.event

import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser

interface EventRepository {

    fun createLocalEvents(
        events: LocalObjectBoxDbEventDetails
    )

    fun getLocalEvent(eventId: String?): LocalObjectBoxDbEventDetailsItem?

    fun getEvents()

    fun getAllLocalEvents(): List<LocalObjectBoxDbEventDetails>?

    fun sendServerCheckIn(
        event: LocalObjectBoxDbEventDetailsItem?,
        localUser: LocalObjectBoxDbUser?,
        eventChecked: (Boolean) -> Unit
    )

}