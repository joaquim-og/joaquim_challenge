package com.joaquim.joaquim_teste.data.model.checkIn

import com.joaquim.joaquim_teste.data.model.user.User
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import java.util.*

@Entity
data class LocalObjectBoxDbEventCheckIn(

    @Id
    var boxId: Long = 0L,

    @NameInDb("userName")
    var userUid: String? = null,

    @NameInDb("dateUserCheckIn")
    var dateUserCheckIn: Long? = null
)