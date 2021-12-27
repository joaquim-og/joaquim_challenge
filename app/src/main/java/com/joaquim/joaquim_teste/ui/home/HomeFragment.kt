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

        homeViewModel.searchEventsData()
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
        val mock1 = LocalObjectBoxDbEventDetailsItem(
            eventDetailDate = 1534784400000.toDate(),
            eventDetailTitle = "Feira de Produtos Naturais e Orgânicos",
            eventDetailLat = "-30.037878",
            eventDetailLng = "-51.2148497",
            eventDetailPrice = "19",
            eventDetailUID = "$1231",
        )

        val mock2 = LocalObjectBoxDbEventDetailsItem(
            eventDetailDate = 1534784400000.toDate(),
            eventDetailTitle = "Hackathon Social Woop Sicredi",
            eventDetailLat = "-30.037878",
            eventDetailLng = "-51.2148497",
            eventDetailPrice = "59.99",
            eventDetailUID = "$1231213",
        )

        val mock = LocalObjectBoxDbEventDetails()
        mock.eventDetailsInfos.add(mock1)
        mock.eventDetailsInfos.add(mock2)

        setRecyclerData(mock)
//        homeViewModel.eventsInfo.observe(viewLifecycleOwner, Observer { eventsDetais ->
//
//            eventsDetais?.let {
//                setRecyclerData(it)
//            }
//
//        })
    }

    private fun setRecyclerData(eventDetails: LocalObjectBoxDbEventDetails) {

        val eventsInfo = mutableListOf<LocalObjectBoxDbEventDetailsItem>()

        eventDetails.eventDetailsInfos.forEach { eventDetailsItem ->
            eventsInfo.add(eventDetailsItem)
        }

        val eventsAdapter = HomeEventsAdapter(
            eventsInfo
        ) {
            Toast.makeText(globalContext, "AQUI O EVENTO SELECIONADO -> $it", Toast.LENGTH_LONG).show()
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