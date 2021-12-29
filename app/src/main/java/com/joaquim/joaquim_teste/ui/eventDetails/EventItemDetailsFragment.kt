package com.joaquim.joaquim_teste.ui.eventDetails

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.switchmaterial.SwitchMaterial
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.OSIntentsApi
import com.joaquim.joaquim_teste.data.commom.SetToastMessage
import com.joaquim.joaquim_teste.data.commom.extensions.*
import com.joaquim.joaquim_teste.databinding.FragmentEventItemDetailsBinding
import com.joaquim.joaquim_teste.ui.HomeEventViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EventItemDetailsFragment : Fragment() {

    private val homeViewModel: HomeEventViewModel by sharedViewModel()
    private val osIntentsApi: OSIntentsApi by inject()
    private val toastMessage: SetToastMessage by inject()

    private var _binding: FragmentEventItemDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapGoogle: GoogleMap
    private lateinit var eventLatLng: LatLng
    private lateinit var mapView: MapView

    private lateinit var eventItemDetailsTitle: TextView
    private lateinit var eventItemDetailsDate: TextView
    private lateinit var eventItemDetailsPrice: TextView
    private lateinit var eventItemDetailsDescription: TextView
    private lateinit var eventItemDetailsImageView: ImageView
    private lateinit var eventItemDetailsCheckInSwitch: SwitchMaterial

    private val callback = OnMapReadyCallback { googleMap ->
        mapGoogle = googleMap

        val mapOptions = googleMap.uiSettings
        mapOptions.isZoomControlsEnabled = true
        mapOptions.isCompassEnabled = true

        homeViewModel.selectedEvent.observe(viewLifecycleOwner, Observer { selectedEvent ->
            selectedEvent?.let { eventDetails ->
                eventLatLng = LatLng(
                    eventDetails.eventDetailLat?.toDouble() ?: 0.0,
                    eventDetails.eventDetailLng?.toDouble() ?: 0.0
                )

                mapGoogle.setMarkerLocation(
                    googleMap,
                    eventLatLng
                )
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventItemDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupBindingVariables()
        setupViews()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapUiConfig(savedInstanceState)

        if (homeViewModel.userHasCheckedEvent()){
            eventItemDetailsCheckInSwitch.setSwitchChecked()
        }

        eventItemDetailsCheckInSwitch.setOnCheckedChangeListener { buttonView, _ ->
                if (buttonView.isChecked) {
                    homeViewModel.registerUserCheck { appointmentRegistered ->
                        if (appointmentRegistered) {
                            toastMessage.setToastMessage(R.string.successfully_register_event_checkin)
                            vibratePhone()
                        } else {
                            unCheckSwitch()
                            toastMessage.setToastMessage(R.string.error_register_event_checkin)
                        }
                    }
                } else {
                    homeViewModel.deleteRegisterUserCheck { registerDeleted ->
                        if (registerDeleted) {
                            toastMessage.setToastMessage(R.string.successfully_deleted_register_event_checkin)
                            vibratePhone()
                        } else {
                            checkSwitch()
                            toastMessage.setToastMessage(R.string.error_on_delete_register_event_checkin)
                        }
                    }
                }
            }
    }

    private fun setupBindingVariables() {
        with(binding) {
            eventItemDetailsTitle = fragmentEventDetailsTitle
            eventItemDetailsDate = fragmentEventDetailsDate
            eventItemDetailsPrice = fragmentEventDetailsPrice
            eventItemDetailsDescription = fragmentEventDetailsDescription
            eventItemDetailsImageView = fragmentEventDetailsImage
            mapView = fragmentEventDetailsMapView
            eventItemDetailsCheckInSwitch = fragmentEventDetailsCheckInSwitch
        }
    }

    private fun setupViews() {
        observeSelectedEventDetails()
    }

    private fun observeSelectedEventDetails() {
        homeViewModel.selectedEvent.observe(viewLifecycleOwner, Observer { selectedEvent ->
            selectedEvent?.let { eventDetails ->

                eventItemDetailsTitle.setOnClickListener {
                    osIntentsApi.openOSShare(context, selectedEvent) {
                        toastMessage.setToastMessage(R.string.generic_error_on_share_event)
                    }
                }

                with(eventDetails) {
                    eventItemDetailsTitle.text = eventDetailTitle
                    eventItemDetailsDate.text = eventDetailDate?.let {
                        getString(
                            R.string.general_text_place_holder_text
                        ).replace("#Placeholder_Text", it.toDate())
                    }
                    eventItemDetailsPrice.text = getString(
                        R.string.general_text_place_holder_price_text,
                        eventDetailPrice?.changeSeparator()
                    )

                    eventItemDetailsDescription.text =
                        eventDetailDescription?.fromHtml(globalContext, eventItemDetailsDescription)

                    Glide.with(globalContext)
                        .load(eventDetailImage)
                        .placeholder(R.drawable.ic_baseline_downloading_24)
                        .error(R.drawable.ic_baseline_image_not_supported_24)
                        .into(eventItemDetailsImageView)
                }

            }
        })
    }

    private fun setMapUiConfig(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            MapsInitializer.initialize(globalContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync(callback)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun unCheckSwitch() {
        eventItemDetailsCheckInSwitch.setSwitchUnChecked()
    }

    private fun checkSwitch() {
        eventItemDetailsCheckInSwitch.setSwitchChecked()
    }

    override fun onDestroyView() {
        homeViewModel.clearSelectedEventDetailsItemData()
        _binding = null
        super.onDestroyView()
    }
}