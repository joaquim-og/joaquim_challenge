package com.joaquim.joaquim_teste.data.commom

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class VerifyNetwork(private val connectivityManager: ConnectivityManager?) {

    fun performActionIfConnected(hasInternetAction: () -> Unit) {
        if (hasInternet()){
            hasInternetAction()
        }
    }

    fun performActionIfIsNOTConnected(noInternetAction: () -> Unit) {
        if (!hasInternet()) {
            noInternetAction()
        }
    }

    private fun hasInternet(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } else {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo

            if (activeNetworkInfo != null){
                return with(activeNetworkInfo.type){
                    this == ConnectivityManager.TYPE_WIFI
                    this == ConnectivityManager.TYPE_MOBILE
                }
            } else {
                false
            }
        }

    }

}