package com.joaquim.joaquim_teste.ui.requestPermissions

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.extension.liveData
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.SetToastMessage
import com.joaquim.joaquim_teste.data.commom.VerifyNetwork
import com.joaquim.joaquim_teste.data.commom.extensions.navigateSafe
import com.joaquim.joaquim_teste.databinding.FragmentRequestLocationPermissionBottomDialogBinding
import org.koin.android.ext.android.inject

class RequestLocationPermissionBottomDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentRequestLocationPermissionBottomDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmButton: Button

    private val toastMessage: SetToastMessage by inject()
    private val verifyNetwork: VerifyNetwork by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_request_location_permission_bottom_dialog,
                container,
                false
            )

        setupViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {

            with(verifyNetwork) {

                performActionIfConnected {
                    requestUserPermission()
                }

                performActionIfIsNOTConnected {
                    toastMessage.setToastMessage(R.string.no_network_error)
                }
            }

        }

    }

    private fun setupViews() {
        confirmButton = binding.fragmentRequestLocationDialogButton
    }

    private fun requestUserPermission() {
        val requestPermissions = permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION
        ).build()

        requestPermissions.liveData().observe(viewLifecycleOwner, Observer { result ->

            if (result.allGranted()) {

                navigateEventItemDetailsFragment()

            } else if (result.anyDenied() || result.anyPermanentlyDenied()) {

                toastMessage.setToastMessage(R.string.access_fine_location_permissions_denied)
            }
        })

        requestPermissions.send()
    }

    private fun navigateEventItemDetailsFragment() {
        navigateSafe(R.id.action_requestLocationPermissionBottomDialog_to_eventItemDetailsFragment)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismiss()
    }

}