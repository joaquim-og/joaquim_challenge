package com.joaquim.joaquim_teste.data.model.event

import com.joaquim.joaquim_teste.data.model.user.User

data class EventDetailsItem(
    val date: Long,
    val description: String,
    val id: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val people: List<User>,
    val price: Double,
    val title: String
)