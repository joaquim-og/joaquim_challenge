package com.joaquim.joaquim_teste.data.commom.extensions

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.joaquim.joaquim_teste.R


/**
 * Handle navigation intents/commands and navigates only if safely possible, when this Fragment is still the current destination.
 */
fun Fragment.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    if (mayNavigate()) findNavController().navigate(
        resId, args, navOptions, navigatorExtras
    )
}

fun Fragment.navigateSafe(
    deepLink: Uri, navOptions: NavOptions? = null, navigatorExtras: Navigator.Extras? = null
) {
    if (mayNavigate()) findNavController().navigate(deepLink, navOptions, navigatorExtras)
}

fun Fragment.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    if (mayNavigate()) findNavController().navigate(directions, navOptions)
}

fun Fragment.navigateSafe(directions: NavDirections, navigatorExtras: Navigator.Extras) {
    if (mayNavigate()) findNavController().navigate(directions, navigatorExtras)
}

/**
 * Returns true if the navigation controller is still pointing at 'this' fragment, or false if it already navigated away.
 */
fun Fragment.mayNavigate(): Boolean {

    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id

    // add tag_navigation_destination_id to your ids.xml so that it's unique:
    val destinationIdOfThisFragment =
        view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController

    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
    return if (destinationIdInNavController == destinationIdOfThisFragment) {
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        true
    } else {
        false
    }

}

fun Fragment.vibratePhone() {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}