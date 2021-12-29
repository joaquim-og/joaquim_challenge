package com.joaquim.joaquim_teste.data.commom.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * set new marker location within googleMap instance.
 */

private val MAPS_ZOOM_STREETS = 15F

fun GoogleMap.setMarkerLocation(
    googleMap: GoogleMap,
    eventLatLong: LatLng?,
) {
    googleMap.clear()

    if (eventLatLong != null) {

        googleMap.addMarker(
            MarkerOptions().position(eventLatLong)
        )

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                eventLatLong,
                MAPS_ZOOM_STREETS
            )
        )

    }
}