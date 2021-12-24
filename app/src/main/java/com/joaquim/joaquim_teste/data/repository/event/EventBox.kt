package com.joaquim.joaquim_teste.data.repository.event

import android.util.Log
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.EVENT_ITEM_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem_

class EventBox : EventRepository {

    private val eventItemBox = ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventDetailsItem::class.java)

    override fun createLocalEvents(
        events: LocalObjectBoxDbEventDetails
    ) {

        events.eventDetailsInfos.forEach { eventDetailItem ->

            val eventExists = getLocalEvent(eventDetailItem.eventDetailId)

            try {
                if (eventExists == null) {
                    eventItemBox.put(eventDetailItem)
                }
            } catch (e: Error) {
                Log.d(EVENT_ITEM_NOT_CREATED, "Here why -> ${e.localizedMessage}")
            }

        }

    }

    override fun getLocalEvent(eventId: String?): LocalObjectBoxDbEventDetailsItem? {
        val query =
            eventItemBox.query(LocalObjectBoxDbEventDetailsItem_.eventDetailUID.equal(eventId))
                .build()

        return query.findUnique()
    }

}