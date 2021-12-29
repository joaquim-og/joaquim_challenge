package com.joaquim.joaquim_teste.data.model.localRegister

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.toInstant
import kotlinx.datetime.until

const val MIN_HOUR_TO_CHECK_SERVER_AGAIN = 4

@Entity
data class LocalObjectBoxDbTimeRegister(

    @Id
    var boxId: Long = 0L,

    @NameInDb("lastUpdate")
    var lastUpdate: String =  Clock.System.now().toString()

) {
    fun hasPassedMinimumIntervalToCheckServerAgain(): Boolean {
        val lastUpdateInInstant = lastUpdate.toInstant()

        return lastUpdateInInstant.until(Clock.System.now(), DateTimeUnit.HOUR) > MIN_HOUR_TO_CHECK_SERVER_AGAIN
    }
}