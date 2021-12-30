package com.joaquim.joaquim_teste.data.repository.checkin

import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn

interface CheckInRepository {

    fun createLocalUserCheckIn(
        eventDetailUID: String?,
        userUid: String?,
        userCheckedInEvent: (Boolean) -> Unit
    )

    fun getLocalUserCheckIn(eventDetailUID: String?, userUid: String?): LocalObjectBoxDbEventCheckIn?

    fun deleteLocalEventCheckIn(eventUID: String?, userUid: String?): Boolean

}