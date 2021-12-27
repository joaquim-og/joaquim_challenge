package com.joaquim.joaquim_teste.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem

class HomeEventViewModel : ViewModel() {

    private val _eventsInfo = MutableLiveData<LocalObjectBoxDbEventDetails>()
    val eventsInfo: LiveData<LocalObjectBoxDbEventDetails> get() = _eventsInfo

    private val _selectedEvent = MutableLiveData<LocalObjectBoxDbEventDetailsItem?>()
    val selectedEvent: LiveData<LocalObjectBoxDbEventDetailsItem?> get() = _selectedEvent


    fun searchEventsData() {
//        TODO("Not yet implemented")
    }

    fun setSelectedEventInfo(eventDetailsItem: LocalObjectBoxDbEventDetailsItem) {
        clearSelectedEventDetailsItemData()
        _selectedEvent.postValue(eventDetailsItem)
    }

    fun clearSelectedEventDetailsItemData() {
        _selectedEvent.postValue(null)
    }

}