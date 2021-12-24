package com.joaquim.joaquim_teste.data.model.checkIn

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import java.util.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity
data class LocalObjectBoxDbEventCheckIn(

    @Id
    var boxId: Long = 0L,

    @NameInDb("checkInUid")
    var checkInUid: String? = null,

    @NameInDb("userUid")
    var userUid: String? = null,

    @NameInDb("eventUid")
    var eventUid: String? = null,

    @NameInDb("dateUserCheckIn")
    var dateUserCheckIn: String? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()

)