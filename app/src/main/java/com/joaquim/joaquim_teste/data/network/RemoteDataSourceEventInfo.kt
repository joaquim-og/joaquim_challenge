package com.joaquim.joaquim_teste.data.network

import com.joaquim.joaquim_teste.data.model.event.EventDetails
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceEventInfo {

    private val retrofitClient: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://5f5a8f24d44d640016169133.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val endpoint = retrofitClient.create(EventInfoApi::class.java)

    fun getEventGeneralInfo(): Call<EventDetails> =
        endpoint.getEventGeneralInfo()

    fun postEventCheckIn(user: LocalObjectBoxDbUser, eventId: String): Call<EventDetails> =
        endpoint.postEventCheckIn(
            eventId,
            user.userEmail,
            user.userName,
            user.userUid
        )

}