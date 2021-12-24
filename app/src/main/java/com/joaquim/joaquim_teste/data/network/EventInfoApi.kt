package com.joaquim.joaquim_teste.data.network

import com.joaquim.joaquim_teste.data.model.event.EventDetails
import retrofit2.Call
import retrofit2.http.*

interface EventInfoApi {

    @GET("/events")
    fun getEventGeneralInfo(): Call<EventDetails>


    @Headers("Content-Type: application/json")
    @POST("/checkin?eventId")
    fun postEventCheckIn(
        @Body eventId: String?,
        @Body userEmail: String?,
        @Body userName: String?,
        @Body userUid: String?
    ): Call<EventDetails>

}