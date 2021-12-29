package com.joaquim.joaquim_teste.data.commom

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.extensions.toDate
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem

class OSIntentsApi {

    fun openOSShare(
        context: Context?,
        eventDetails: LocalObjectBoxDbEventDetailsItem,
        errorOnHandleActivity: (activityError: ActivityNotFoundException) -> Unit
    ) {

        val mapsLocation = "https://www.google.com/maps?q=${eventDetails.eventDetailLat},${eventDetails.eventDetailLng}"

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND

            putExtra(
                Intent.EXTRA_TEXT,
                globalContext.getString(R.string.generic_text_share_intent_generic)
                    .replace("#event_title", eventDetails.eventDetailTitle ?: "")
                    .replace("#event_date", eventDetails.eventDetailDate?.toDate() ?: "")
                    .replace("#maps_url", mapsLocation)
            )
            type = "text/plain"
        }

        try {
            context?.packageManager?.let {
                shareIntent.resolveActivity(it)?.let {
                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            globalContext.getString(R.string.generic_text_share_intent_generic)
                        )
                    )
                }
            }
        } catch (activityError: ActivityNotFoundException) {
            errorOnHandleActivity(activityError)
        }

    }

}
