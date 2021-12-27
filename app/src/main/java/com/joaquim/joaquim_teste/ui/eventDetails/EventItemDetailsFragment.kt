package com.joaquim.joaquim_teste.ui.eventDetails

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.ui.HomeEventViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EventItemDetailsFragment : Fragment() {

    private val homeViewModel: HomeEventViewModel by sharedViewModel()

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-30.037878, -51.2148497)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        homeViewModel.clearSelectedEventDetailsItemData()
//        _binding = null
        super.onDestroyView()
    }
}