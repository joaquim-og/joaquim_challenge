package com.joaquim.joaquim_teste.data.repository.checkin

import android.util.Log
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.CHECKIN_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn_

class CheckInBox : CheckInRepository {

    private val checkInBox = ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventCheckIn::class.java)

    override fun createLocalUserCheckIn(
        eventDetailUID: String?,
        userUid: String?,
        userCheckedInEvent: (Boolean) -> Unit
    ) {
        val checkInExists = getLocalUserCheckIn(eventDetailUID, userUid)

        try {

            if (checkInExists == null) {
                checkInBox.put(
                    LocalObjectBoxDbEventCheckIn(
                        checkInUid = "${eventDetailUID}_${userUid}",
                        userUid = userUid,
                        eventUid = eventDetailUID)
                )
                userCheckedInEvent(true)
            }

        } catch (e: Error) {
            Log.d(CHECKIN_NOT_CREATED, "Here why -> ${e.localizedMessage}")
            userCheckedInEvent(false)
        }
    }

    override fun deleteLocalEventCheckIn(eventUID: String?, userUid: String?): Boolean {
        val checkInToDelete = getLocalUserCheckIn(eventUID, userUid)

        return try {
            if (checkInToDelete != null) {
                checkInBox.remove(checkInToDelete)
                true
            } else {
                false
            }
        } catch (e: Error) {
            Log.d("Appointment Delete FAIL", "Here why -> ${e.localizedMessage}")
            false
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