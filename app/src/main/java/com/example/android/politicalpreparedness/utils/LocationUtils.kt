package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.utils.Constants.LOCATION_TAG
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
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

@SuppressLint("MissingPermission")
fun getLocation(locationManager : LocationManager, currentPosition : MutableLiveData<LatLng>) {
    val locationListener: LocationListener = object : LocationListener {
        override fun onProviderEnabled(provider: String) {
            Log.e(LOCATION_TAG, "provide $provider enabled")
        }

        override fun onLocationChanged(location: Location) {
            currentPosition.value = LatLng(location.latitude, location.longitude)
        }

        //It is called for avoid crashing on Android 9 or lower when we remove GPS position after select a position on a map
        override fun onProviderDisabled(provider: String) {
            //super was removed

            Log.e(LOCATION_TAG, "provide $provider disabled")
        }
    }

    locationManager
        .requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            20f,
            locationListener
        )
}

fun askToTurnOnLocation(fragment : Fragment, methodToInvoke: () -> Unit  = {}, resolve : Boolean = true){
    val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_LOW_POWER
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val settingsClient = LocationServices.getSettingsClient(fragment.requireActivity())

    //variable that checks location settings
    val locationSettingsResponseTask =
        settingsClient.checkLocationSettings(builder.build())

    locationSettingsResponseTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException && resolve) {
            try {
                exception.startResolutionForResult(
                    fragment.requireActivity(),
                    Constants.REQUEST_TURN_DEVICE_LOCATION_ON
                )
            } catch (sendEx: IntentSender.SendIntentException) {
                Log.d(
                    LOCATION_TAG,
                    "Error getting location settings resolution: " + sendEx.message
                )
            }
        } else {
            //it's a custom Snackbar with an action
            Snackbar.make(
                fragment.requireView(),
                "", Snackbar.LENGTH_INDEFINITE
            ).setAction(android.R.string.ok) {
                askToTurnOnLocation(fragment)
            }.show()
        }
    }
    locationSettingsResponseTask.addOnSuccessListener {
        Log.d(LOCATION_TAG, "Location activated")
        methodToInvoke.invoke()
    }
}


fun requestForegroundLocationPermissions(fragment : Fragment) {
    Log.e(LOCATION_TAG, "requestForegroundLocationPermissions")
    if (isPermissionGranted(fragment.requireContext()))
        return
    val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val resultCode =  Constants.REQUEST_FOREGROUND_PERMISSIONS_REQUEST_CODE

    fragment.requestPermissions(
        permissionsArray,
        resultCode)

    Log.e(LOCATION_TAG, "wait for foreground")
}

fun geoCodeLocation(geoCoder: Geocoder, currentPositionCoordinates : LatLng): Address {
    return geoCoder.getFromLocation(currentPositionCoordinates.latitude, currentPositionCoordinates.longitude, 1)
        .map { address ->
            Log.e(LOCATION_TAG, "line 1 ${address.thoroughfare}")
            Log.e(LOCATION_TAG, "line 2 ${address.subThoroughfare}")
            Log.e(LOCATION_TAG, "zip ${address.postalCode}")
            Log.e(LOCATION_TAG, "city ${address.locality}")
            Log.e(LOCATION_TAG, "state ${address.adminArea}")
            Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
        }
        .first()
}