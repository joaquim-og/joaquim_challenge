package com.joaquim.joaquim_teste.data.commom

import androidx.core.content.ContextCompat
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext

class CheckPermissions {

    fun checkPermission(permissionToCheck: String): Boolean {

        val checkAskedPermission = ContextCompat.checkSelfPermission(
            globalContext,
            permissionToCheck
        )

        return when (checkAskedPermission) {
            0 -> true
            else -> false
        }

    }

}