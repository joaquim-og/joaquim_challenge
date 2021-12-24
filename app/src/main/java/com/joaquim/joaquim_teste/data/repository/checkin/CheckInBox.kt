package com.joaquim.joaquim_teste.data.repository.checkin

import android.util.Log
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.CHECKIN_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.EVENT_ITEM_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn_
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import io.objectbox.annotation.NameInDb

class CheckInBox : CheckInRepository {

    private val checkInBox = ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventCheckIn::class.java)

    override fun createLocalUserCheckIn(
        eventToCheckIn: LocalObjectBoxDbEventDetailsItem,
        user: LocalObjectBoxDbUser
    ) {
        val checkInExists = getLocalUserCheckIn(eventToCheckIn.eventDetailUID, user.userUid)

        try {

            if (checkInExists == null) {
                checkInBox.put(
                    LocalObjectBoxDbEventCheckIn(
                        checkInUid = "${eventToCheckIn.eventDetailUID}_${user.userUid}",
                        userUid = user.userUid,
                        eventUid = eventToCheckIn.eventDetailUID)
                )
            }

        } catch (e: Error) {
            Log.d(CHECKIN_NOT_CREATED, "Here why -> ${e.localizedMessage}")
        }
    }

    override fun getLocalUserCheckIn(
        eventDetailUID: String?,
        userUid: String?
    ): LocalObjectBoxDbEventCheckIn? {
        val query =
            checkInBox.query(LocalObjectBoxDbEventCheckIn_.checkInUid.equal("${eventDetailUID}_${userUid}"))
                .build()

        return query.findUnique()
    }

}