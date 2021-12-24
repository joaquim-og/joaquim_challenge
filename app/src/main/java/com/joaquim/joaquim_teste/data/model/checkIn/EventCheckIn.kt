package com.joaquim.joaquim_teste.data.model.checkIn

data class EventCheckIn(
    val checkInUid: String,
    val userUid: String,
    val eventId: String,
    val dateUserCheckIn: String
)