package com.joaquim.joaquim_teste.data.repository.event

import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem

interface EventRepository {

    fun createLocalEvents(
        events: LocalObjectBoxDbEventDetails
    )

    fun getLocalEvent(eventId: String?): LocalObjectBoxDbEventDetailsItem?

}