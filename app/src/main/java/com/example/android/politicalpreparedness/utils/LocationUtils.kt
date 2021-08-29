package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.network.models.Address
import java.util.*

fun checkLocationPermissions(fragment: Fragment): Boolean {
    return if (isPermissionGranted(fragment.requireContext())) {
        true
    } else {
        requestForegroundLocationPermissions(fragment)
        false
    }
}

fun isPermissionGranted(context: Context) : Boolean {
    return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
}

fun getLocation() {
    //TODO: Get location from LocationServices
    //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
}

fun handleRequestPermission(){
    //TODO: Handle location permission result to get location on permission granted
}


fun requestForegroundLocationPermissions(fragment : Fragment) {
    Log.e(Constants.LOCATION_TAG, "requestForegroundLocationPermissions")
    if (isPermissionGranted(fragment.requireContext()))
        return
    val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val resultCode =  Constants.REQUEST_FOREGROUND_PERMISSIONS_REQUEST_CODE

    fragment.requestPermissions(
        permissionsArray,
        resultCode)

    Log.e(Constants.LOCATION_TAG, "wait for foreground")
}

fun geoCodeLocation(context : Context, location: Location): Address {
    val geocoder = Geocoder(context, Locale.getDefault())
    return geocoder.getFromLocation(location.latitude, location.longitude, 1)
        .map { address ->
            Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
        }
        .first()
}