package com.joaquim.joaquim_teste.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaquim.joaquim_teste.data.commom.SharedPrefs
import com.joaquim.joaquim_teste.data.commom.USER_UID
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import com.joaquim.joaquim_teste.data.repository.checkin.CheckInBox
import com.joaquim.joaquim_teste.data.repository.event.EventBox
import com.joaquim.joaquim_teste.data.repository.user.UserBox
import kotlinx.coroutines.*

class HomeEventViewModel(
    private val repositoryEvents: EventBox,
    private val userRepository: UserBox,
    private val checkInBox: CheckInBox
) : ViewModel() {

    private val _eventsInfo = repositoryEvents.localEvents
    val eventsInfo: LiveData<List<LocalObjectBoxDbEventDetails>?> get() = _eventsInfo

    private val _selectedEvent = MutableLiveData<LocalObjectBoxDbEventDetailsItem?>()
    val selectedEvent: LiveData<LocalObjectBoxDbEventDetailsItem?> get() = _selectedEvent


    fun searchEventsData() {

        CoroutineScope(Dispatchers.IO).launch {
            repositoryEvents.getEvents()
        }

    }

    fun setSelectedEventInfo(eventDetailsItem: LocalObjectBoxDbEventDetailsItem) {
        clearSelectedEventDetailsItemData()
        _selectedEvent.postValue(eventDetailsItem)
    }

    fun clearSelectedEventDetailsItemData() {
        _selectedEvent.postValue(null)
    }

    fun userHasCheckedEvent(): Boolean {
        val userEventChecked = checkInBox.getLocalUserCheckIn(
            _selectedEvent.value?.eventDetailUID,
            SharedPrefs.getUserData(USER_UID)
        )

        return userEventChecked != null
    }

    fun createLocalUser() {

        //Setting local user, since we don't have login
        val userToCreate = LocalObjectBoxDbUser(
            userUid = "user1234",
            userName = "userSicredi",
            userEmail = "userSicredi@sicred.com.br"
        )

        userRepository.createLocalUser(userToCreate)
    }

    fun registerUserCheck(userCheckedInEvent: (Boolean) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {

            repositoryEvents.sendServerCheckIn(
                _selectedEvent.value,
                userRepository.getLocalUser(SharedPrefs.getUserData(USER_UID))
            ) { hasChecked ->
                userCheckedInEvent(hasChecked)
            }
        }
    }

    fun deleteRegisterUserCheck(registerCheckedInDeleted: (Boolean) -> Unit) {
        registerCheckedInDeleted(
            checkInBox.deleteLocalEventCheckIn(
                _selectedEvent.value?.eventDetailUID,
                SharedPrefs.getUserData(USER_UID)
            )
        )
    }

}