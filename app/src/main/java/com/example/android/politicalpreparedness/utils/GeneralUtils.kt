package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.location.Geocoder
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.maps.model.LatLng

fun hideKeyboard(fragment: Fragment) {
    val imm = fragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
}