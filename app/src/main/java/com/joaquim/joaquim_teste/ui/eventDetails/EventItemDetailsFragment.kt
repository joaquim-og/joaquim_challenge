package com.joaquim.joaquim_teste.ui.eventDetails

import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.extensions.changeSeparator
import com.joaquim.joaquim_teste.data.commom.extensions.setMarkerLocation
import com.joaquim.joaquim_teste.data.commom.extensions.toDate
import com.joaquim.joaquim_teste.databinding.FragmentEventItemDetailsBinding
import com.joaquim.joaquim_teste.ui.HomeEventViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EventItemDetailsFragment : Fragment() {

    private val homeViewModel: HomeEventViewModel by sharedViewModel()

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
    }

    private fun setupBindingVariables() {
        with(binding) {
            eventItemDetailsTitle = fragmentEventDetailsTitle
            eventItemDetailsDate = fragmentEventDetailsDate
            eventItemDetailsPrice = fragmentEventDetailsPrice
            eventItemDetailsDescription = fragmentEventDetailsDescription
            eventItemDetailsImageView = fragmentEventDetailsImage
            mapView = fragmentEventDetailsMapView
        }
    }

    private fun setupViews() {
        observeSelectedEventDetails()
    }

    private fun observeSelectedEventDetails() {
        homeViewModel.selectedEvent.observe(viewLifecycleOwner, Observer { selectedEvent ->
            selectedEvent?.let { eventDetails ->

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

                    val spannedDetail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(eventDetailDescription, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(eventDetailDescription)
                    }

                    eventItemDetailsDescription.text = spannedDetail

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

    override fun onDestroyView() {
        homeViewModel.clearSelectedEventDetailsItemData()
        _binding = null
        super.onDestroyView()
    }
}