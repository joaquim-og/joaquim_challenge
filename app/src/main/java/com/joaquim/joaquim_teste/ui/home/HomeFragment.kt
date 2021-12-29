package com.joaquim.joaquim_teste.ui.home

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.CheckPermissions
import com.joaquim.joaquim_teste.data.commom.SetToastMessage
import com.joaquim.joaquim_teste.data.commom.VerifyNetwork
import com.joaquim.joaquim_teste.data.commom.extensions.hide
import com.joaquim.joaquim_teste.data.commom.extensions.navigateSafe
import com.joaquim.joaquim_teste.data.commom.extensions.show
import com.joaquim.joaquim_teste.data.commom.extensions.toDate
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.databinding.FragmentHomeBinding
import com.joaquim.joaquim_teste.ui.HomeEventViewModel
import com.joaquim.joaquim_teste.ui.home.adapter.HomeEventsAdapter
import io.objectbox.annotation.NameInDb
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeEventViewModel by sharedViewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeEventsRecyclerView: RecyclerView
    private lateinit var homeLoadingLottieAnimation: LottieAnimationView
    private lateinit var homeNoWifiLottieAnimation: LottieAnimationView

    private val verifyNetwork: VerifyNetwork by inject()
    private val toastMessage: SetToastMessage by inject()
    private val checkPermissions: CheckPermissions by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupBindingVariables()
        setupViews()

        return binding.root
    }

    private fun setupBindingVariables() {
        with(binding) {
            homeEventsRecyclerView = fragmentHomeEventRecyclerView
            homeLoadingLottieAnimation = fragmentHomeEventLoadingAnimation
            homeNoWifiLottieAnimation = fragmentHomeEventNoWifiAnimation
        }
    }

    private fun setupViews() {

        homeEventsRecyclerView.layoutManager = LinearLayoutManager(this.context)

        with(verifyNetwork) {

            performActionIfConnected {
                observeEventsInfo()
            }

            performActionIfIsNOTConnected {
                homeLoadingLottieAnimation.hide()
                homeNoWifiLottieAnimation.show()
                toastMessage.setToastMessage(R.string.no_network_error)
            }
        }

    }

    private fun observeEventsInfo() {
        homeViewModel.eventsInfo.observe(viewLifecycleOwner, Observer { eventsDetails ->

            eventsDetails?.let {
                setRecyclerData(it)
            } ?: toastMessage.setToastMessage(R.string.error_get_api_data)

        })
    }

    private fun setRecyclerData(eventDetails: List<LocalObjectBoxDbEventDetails>) {

        val eventsInfo = mutableListOf<LocalObjectBoxDbEventDetailsItem>()

        eventDetails.forEach { localEventsDetails ->
            localEventsDetails.eventDetailsInfos.forEach {
                eventsInfo.add(it)
            }
        }

        val eventsAdapter = HomeEventsAdapter(
            eventsInfo
        ) {
            homeViewModel.setSelectedEventInfo(it)
            navigateToSelectedEvent()
        }

        if (eventsInfo.isNotEmpty()) {
            homeLoadingLottieAnimation.hide()
            homeEventsRecyclerView.show()
            homeEventsRecyclerView.adapter = eventsAdapter
            eventsAdapter.notifyDataSetChanged()
        }

    }

    private fun navigateToSelectedEvent() {
        if (checkPermissions.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            navigateToSelectEvent()
        } else {
            navigateToRequestUserLocationPermissionDialog()
        }
    }

    private fun navigateToRequestUserLocationPermissionDialog() {
        navigateSafe(R.id.action_homeFragment_to_requestLocationPermissionBottomDialog)
    }

    private fun navigateToSelectEvent() {
        navigateSafe(R.id.action_homeFragment_to_eventItemDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}