package com.joaquim.joaquim_teste.data.model.event

import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import com.joaquim.joaquim_teste.data.model.user.User
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import io.objectbox.relation.ToMany
import java.util.*

@Entity
data class LocalObjectBoxDbEventDetailsItem(

    @Id
    var boxId: Long = 0L,

    @NameInDb("eventDetailDate")
    var eventDetailDate: String? = null,

    @NameInDb("eventDetailTitle")
    var eventDetailTitle: String? = null,

    @NameInDb("eventDetailDescription")
    var eventDetailDescription: String? = null,

    @NameInDb("eventDetailId")
    var eventDetailId: String? = null,

    @NameInDb("eventDetailImage")
    var eventDetailImage: String? = null,

    @NameInDb("eventDetailLat")
    var eventDetailLat: String? = null,

    @NameInDb("eventDetailLng")
    var eventDetailLng: String? = null,

    @NameInDb("eventDetailPrice")
    var eventDetailPrice: String? = null,

    @NameInDb("eventDetailUID")
    var eventDetailUID: String? = "${eventDetailId}_${eventDetailTitle}",

) {

    var eventDetailPeople: ToMany<LocalObjectBoxDbUser> = ToMany(this, LocalObjectBoxDbEventDetailsItem_.eventDetailPeople)
//    domainObject.learningUnits.addAll(learningUnitDataMapper.toDomainList(it, responseObject.id))
}