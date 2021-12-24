package com.joaquim.joaquim_teste.data.model.event

import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import com.joaquim.joaquim_teste.data.model.user.User
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import io.objectbox.relation.ToMany
import java.util.*

@Entity
data class LocalObjectBoxDbEventDetails(

    @Id
    var boxId: Long = 0L,

    @NameInDb("eventDetailsUID")
    var eventDetailsUID: String? = UUID.randomUUID().toString()

) {
    var eventDetailsInfos: ToMany<LocalObjectBoxDbEventDetailsItem> = ToMany(this, LocalObjectBoxDbEventDetails_.eventDetailsInfos)
}