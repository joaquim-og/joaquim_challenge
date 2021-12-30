package com.joaquim.joaquim_teste.objectBoxTests

import com.google.common.truth.Truth.assertThat
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn
import com.joaquim.joaquim_teste.data.model.checkIn.LocalObjectBoxDbEventCheckIn_
import com.joaquim.joaquim_teste.testModels.*
import io.objectbox.Box
import org.junit.Before
import org.junit.Test

class CheckInBoxTest : BaseObjectBoxTest() {

    private lateinit var checkInBox: Box<LocalObjectBoxDbEventCheckIn>

    @Before
    fun setup() {
        checkInBox = store?.boxFor(LocalObjectBoxDbEventCheckIn::class.java)!!
    }


    @Test
    fun `when have checkIns assert that they were saved locally`() {
        // Given mocked checkInBox
        // when save local func is called
        val saveCheckIn1 = checkInBox.put(checkInTestModel1)
        val saveCheckIn2 = checkInBox.put(checkInTestModel2)

        //assert that
        // all checkins were created
        val user1CheckIn = checkIfHasCheckingRegister(checkInTestModel1.checkInUid)
        val user2CheckIn = checkIfHasCheckingRegister(checkInTestModel2.checkInUid)

        assertThat(user1CheckIn).isNotNull()
        assertThat(user2CheckIn).isNotNull()

        // correct values were saved
        assertThat(saveCheckIn1).isNotNull()
        assertThat(saveCheckIn2).isNotNull()
        assertThat(user1CheckIn?.eventUid).isEqualTo(eventDetailsItem1.eventDetailUID)
        assertThat(user1CheckIn?.userUid).isEqualTo(user1.userUid)
        assertThat(user2CheckIn?.eventUid).isEqualTo(eventDetailsItem2.eventDetailUID)
        assertThat(user2CheckIn?.userUid).isEqualTo(user2.userUid)
    }

    @Test
    fun `when user remove checkIns assert that they were deleted`() {
        // Given mocked checkInBox
        checkInBox.put(checkInTestModel3)
        checkInBox.put(checkInTestModel4)

        // when delete local func is called
        checkInBox.remove(checkInTestModel3)
        checkInBox.remove(checkInTestModel4)

        //assert that
        // all checkins were deleted
        val user1CheckIn = checkIfHasCheckingRegister(checkInTestModel3.checkInUid)
        val user2CheckIn = checkIfHasCheckingRegister(checkInTestModel4.checkInUid)

        assertThat(user1CheckIn).isNull()
        assertThat(user2CheckIn).isNull()
        assertThat(checkInBox.all).hasSize(0)
    }

    private fun checkIfHasCheckingRegister(checkInUid: String?): LocalObjectBoxDbEventCheckIn? {
        val query = checkInBox.query(
            LocalObjectBoxDbEventCheckIn_.checkInUid.equal(
                checkInUid
            )
        )?.build()

        return query?.findUnique()
    }

}