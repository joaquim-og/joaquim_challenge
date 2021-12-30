package com.joaquim.joaquim_teste.objectBoxTests

import com.google.common.truth.Truth.assertThat
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn_
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails_
import com.joaquim.joaquim_teste.data.model.localRegister.LocalObjectBoxDbTimeRegister
import com.joaquim.joaquim_teste.testModels.*
import io.objectbox.Box
import kotlinx.datetime.Clock
import org.junit.Before
import org.junit.Test

class EventInBoxTest : BaseObjectBoxTest() {

    private lateinit var eventsBox: Box<LocalObjectBoxDbEventDetails>

    @Before
    fun setup() {
        eventsBox = store?.boxFor(LocalObjectBoxDbEventDetails::class.java)!!
    }


    @Test
    fun `when have event assert that it was saved locally`() {
        // Given mocked event
        val eventToSave = LocalObjectBoxDbEventDetails()
        eventToSave.eventDetailsInfos.add(eventDetailsItem1)
        eventToSave.eventDetailsInfos.add(eventDetailsItem2)

        // when save local func is called
        eventsBox.put(eventToSave)
        val eventSaved = checkIfHasEvent(eventToSave.eventDetailsUID)

        //assert that
        //event was saved
        assertThat(eventSaved).isNotNull()

        //event details were also saved
        assertThat(eventSaved?.eventDetailsInfos).isNotNull()
        assertThat(eventSaved?.eventDetailsInfos).hasSize(2)
        assertThat(eventsBox.all).hasSize(1)
        assertThat(eventsBox.all).isNotEmpty()
        assertThat(eventsBox.all).isNotNull()
    }

    @Test
    fun `check if last saved local event database has more than 4 hours`() {
        // Given mocked past instant
        val localTimeRegisterWithMoreThan4Hours = lastSavedEventTimeRegister

        // when saves local TimeRegister time verification is called
        // assert true to flag new need to update local data
        assertThat(localTimeRegisterWithMoreThan4Hours.hasPassedMinimumIntervalToCheckServerAgain()).isTrue()
    }

    private fun checkIfHasEvent(eventUid: String?): LocalObjectBoxDbEventDetails? {
        val query = eventsBox.query(
            LocalObjectBoxDbEventDetails_.eventDetailsUID.equal(
                eventUid
            )
        )?.build()

        return query?.findUnique()
    }

}