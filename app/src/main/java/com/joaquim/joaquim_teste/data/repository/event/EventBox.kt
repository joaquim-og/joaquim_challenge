package com.joaquim.joaquim_teste.data.repository.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.EVENT_ITEM_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.SERVER_DATA_ERROR
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.commom.extensions.addHttpsIfNeeded
import com.joaquim.joaquim_teste.data.model.event.EventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem_
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import com.joaquim.joaquim_teste.data.network.RemoteDataSourceEventInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventBox(
    private val remoteDataSourceEvent: RemoteDataSourceEventInfo
) : EventRepository {

    private val _localEvents = MutableLiveData<List<LocalObjectBoxDbEventDetails>?>()
    val localEvents: LiveData<List<LocalObjectBoxDbEventDetails>?> get() = _localEvents

    private val eventItemBox =
        ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventDetailsItem::class.java)
    private val eventsBox = ObjectBox.boxStore.boxFor(LocalObjectBoxDbEventDetails::class.java)

    override fun createLocalEvents(
        events: LocalObjectBoxDbEventDetails
    ) {

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

    override fun getEvents() {

        val localEventExists = getAllLocalEvents()

        if (localEventExists.isNullOrEmpty()) {
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

}