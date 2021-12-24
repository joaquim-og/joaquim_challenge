package com.joaquim.joaquim_teste.data.model.user

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import java.util.*

@Entity
data class LocalObjectBoxDbUser(

    @Id
    var boxId: Long = 0L,

    @NameInDb("userUid")
    var userUid: String? = null,

    @NameInDb("userName")
    var userName: String? = null,

    @NameInDb("userEmail")
    var userEmail: String? = null

)