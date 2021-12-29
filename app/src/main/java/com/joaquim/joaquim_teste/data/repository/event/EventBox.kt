package com.joaquim.joaquim_teste.data.repository.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.CHECKIN_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.EVENT_DETAILS_ERROR
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.EVENT_ITEM_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.SERVER_DATA_ERROR
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.commom.extensions.addHttpsIfNeeded
import com.joaquim.joaquim_teste.data.model.checkIn.EventCheckIn
import com.joaquim.joaquim_teste.data.model.checkIn.EventCheckInResponse
import com.joaquim.joaquim_teste.data.model.event.*
import com.joaquim.joaquim_teste.data.model.localRegister.LocalObjectBoxDbTimeRegister
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import com.joaquim.joaquim_teste.data.network.RemoteDataSourceEventInfo
import com.joaquim.joaquim_teste.data.repository.checkin.CheckInBox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventBox(
    private val remoteDataSourceEvent: RemoteDataSourceEventInfo,
    private val checkInBox: CheckInBox
) : EventRepository {

    private val _localEvents = MutableLiveData<List<LocalObjectBoxDbEventDetails>?>()
    val localEvents: LiveData<List<LocalObjectBoxDbEventDetails>?> get() = _localEvents

    private val eventItemBox =
        ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventDetailsItem::class.java)
    private val eventsBox = ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventDetails::class.java)
    private val lastTimeGetServerDataBox =
        ObjectBox.boxStore.boxFor(LocalObjectBoxDbTimeRegister::class.java)

    override fun getEvents() {

        val localEventExists = getAllLocalEvents()

        if (localEventExists.isNullOrEmpty() || isTimeToUpdateLocalData()) {
            getRemoteEventInfo { eventDetails ->
                _localEvents.postValue(eventDetails)
            }
        } else {
            _localEvents.postValue(localEventExists)
        }

    }

    override fun getLocalEvent(eventId: String?): LocalObjectBoxDbEventDetailsItem? {
        val query =
            eventItemBox.query(LocalObjectBoxDbEventDetailsItem_.eventDetailUID.equal(eventId))
                .build()

        return query.findUnique()
    }

    override fun getAllLocalEvents(): List<LocalObjectBoxDbEventDetails>? = eventsBox.all

    private fun getLocalEventDetails(eventDetailItemId: String?): LocalObjectBoxDbEventDetails? {
        val query =
            eventsBox.query(LocalObjectBoxDbEventDetails_.eventDetailsUID.equal(eventDetailItemId))
                .build()

        return query.findUnique()
    }

    override fun createLocalEvents(
        events: LocalObjectBoxDbEventDetails
    ) {

        val eventDetailsExists = getLocalEventDetails(events.eventDetailsUID)

        try {
            if (eventDetailsExists == null) {
                eventsBox.put(events)
            }
        } catch (e: Error) {
            Log.d(EVENT_DETAILS_ERROR, "Here why -> ${e.localizedMessage}")
        }

        events.eventDetailsInfos.forEach { eventDetailItem ->

            val eventExists = getLocalEvent(eventDetailItem.eventDetailId)

            try {
                if (eventExists == null) {
                    eventItemBox.put(eventDetailItem)
                }
            } catch (e: Error) {
                Log.d(EVENT_ITEM_NOT_CREATED, "Here why -> ${e.localizedMessage}")
            }

        }

    }

    private fun getRemoteEventInfo(eventsDetails: (List<LocalObjectBoxDbEventDetails>?) -> Unit) {
        val getRemoteInfoQueue = remoteDataSourceEvent.getEventGeneralInfo()

        getRemoteInfoQueue.enqueue(object : Callback<EventDetails> {

            override fun onResponse(
                call: Call<EventDetails>,
                response: Response<EventDetails>
            ) {

                try {
                    if (response.isSuccessful) {

                        val responseJSON = Gson().toJson(response.body())

                        if (!responseJSON.isNullOrEmpty()) {

                            val responseData = GsonBuilder().create()
                                .fromJson(responseJSON, EventDetails::class.java)

                            val localEventsDetails =
                                mutableListOf<LocalObjectBoxDbEventDetails>()

                            responseData.forEach { eventDetailsItem ->

                                val localEventDetails = LocalObjectBoxDbEventDetails()

                                val localEventDetailsItem = LocalObjectBoxDbEventDetailsItem(
                                    eventDetailDate = eventDetailsItem.date.toString(),
                                    eventDetailTitle = eventDetailsItem.title,
                                    eventDetailDescription = eventDetailsItem.description,
                                    eventDetailId = eventDetailsItem.id,
                                    eventDetailImage = eventDetailsItem.image.addHttpsIfNeeded(),
                                    eventDetailLat = eventDetailsItem.latitude.toString(),
                                    eventDetailLng = eventDetailsItem.longitude.toString(),
                                    eventDetailPrice = eventDetailsItem.price.toString()
                                )

                                eventDetailsItem.people.forEach { user ->
                                    localEventDetailsItem.eventDetailPeople.add(
                                        LocalObjectBoxDbUser(
                                            userEmail = user.userEmail,
                                            userName = user.userName,
                                            userUid = user.userUid
                                        )
                                    )
                                }

                                localEventDetails.eventDetailsInfos.add(localEventDetailsItem)

                                localEventsDetails.add(localEventDetails)
                                createLocalEvents(localEventDetails)
                                registerTimeLocalDataUpdated()
                            }

                            eventsDetails(localEventsDetails)
                        } else {
                            //JSON EMPTY
                            Log.d(
                                SERVER_DATA_ERROR,
                                "error on api response, this why -> ${response.errorBody()}"
                            )
                            eventsDetails(null)
                        }
                    } else {
                        //RESPONSE ERROR
                        Log.d(
                            SERVER_DATA_ERROR,
                            "error on api response, this why -> ${response.errorBody()}"
                        )
                        eventsDetails(null)
                    }
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                    Log.d(
                        SERVER_DATA_ERROR,
                        "error on api response, this why -> ${exception.localizedMessage} | ${exception.stackTrace}"
                    )
                }
            }

            override fun onFailure(call: Call<EventDetails>, e: Throwable) {
                e.printStackTrace()
                Log.d(
                    SERVER_DATA_ERROR,
                    "error on api response, this why -> ${e.localizedMessage} | ${e.stackTrace}"
                )
                eventsDetails(null)
            }

        })

    }

    private fun registerTimeLocalDataUpdated() =
        lastTimeGetServerDataBox.put(LocalObjectBoxDbTimeRegister())

    private fun isTimeToUpdateLocalData(): Boolean {
        val lastUpdated = lastTimeGetServerDataBox.all.last()

        return lastUpdated.hasPassedMinimumIntervalToCheckServerAgain()
    }

    override fun sendServerCheckIn(
        event: LocalObjectBoxDbEventDetailsItem?,
        localUser: LocalObjectBoxDbUser?,
        eventChecked: (Boolean) -> Unit
    ) {
        val postRemoteCheckQueue =
            remoteDataSourceEvent.postEventCheckIn(EventCheckIn(
                userName = localUser?.userName ?: "",
                userEmail = localUser?.userEmail ?: "",
                eventId = event?.eventDetailId ?: ""
            ))

        postRemoteCheckQueue.enqueue(object : Callback<EventCheckInResponse> {

            override fun onResponse(
                call: Call<EventCheckInResponse>,
                response: Response<EventCheckInResponse>
            ) {

                try {
                    if (response.isSuccessful) {

                        val responseJSON = Gson().toJson(response.body())

                        if (responseJSON != null) {

                            checkInBox.createLocalUserCheckIn(
                                event?.eventDetailUID,
                                localUser?.userUid
                            ) { checkInCreated ->
                                eventChecked(checkInCreated)
                            }
                        } else {
                            //JSON EMPTY
                            Log.d(
                                CHECKIN_NOT_CREATED,
                                "error on api response, this why -> ${response.errorBody()}"
                            )
                            eventChecked(false)
                        }
                    } else {
                        //RESPONSE ERROR
                        Log.d(
                            CHECKIN_NOT_CREATED,
                            "error on api response, this why -> ${response.errorBody()}"
                        )
                        eventChecked(false)
                    }
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                    Log.d(
                        CHECKIN_NOT_CREATED,
                        "error on api response, this why -> ${exception.localizedMessage} | ${exception.stackTrace}"
                    )
                }
            }

            override fun onFailure(call: Call<EventCheckInResponse>, e: Throwable) {
                e.printStackTrace()
                Log.d(
                    CHECKIN_NOT_CREATED,
                    "error on api response, this why -> ${e.localizedMessage} | ${e.stackTrace}"
                )
                eventChecked(false)
            }

        })
    }


}