package com.joaquim.joaquim_teste.data.repository.checkin

import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser

interface CheckInRepository {

    fun createLocalUserCheckIn(
        eventToCheckIn: LocalObjectBoxDbEventDetailsItem,
        user: LocalObjectBoxDbUser
    )

    fun getLocalUserCheckIn(eventDetailUID: String?, userUid: String?): LocalObjectBoxDbEventCheckIn?

}