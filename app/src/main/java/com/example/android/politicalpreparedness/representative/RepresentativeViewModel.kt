package com.example.android.politicalpreparedness.representative

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Constants
import com.example.android.politicalpreparedness.utils.Constants.NETWORK_TAG
import com.example.android.politicalpreparedness.utils.geoCodeLocation
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepresentativeViewModel @Inject constructor(val repository: ElectionsRepository) : ViewModel() {


    private val _upcomingElections = MutableLiveData<List<Representative>>()
    val upcomingElections : LiveData<List<Representative>>
        get() = _upcomingElections
    val locationRequestedAndApproved = MutableLiveData<Boolean>()
    val currentPosition = MutableLiveData<LatLng>()

    init{
        fetchRepresentativeByAddress(
            getAddressFromFields(
                Constants.DEFAULT_ADDRESS_LINE_1,
                Constants.DEFAULT_ADDRESS_LINE_2,
                Constants.DEFAULT_ADDRESS_CITY,
                Constants.DEFAULT_ADDRESS_STATE,
                Constants.DEFAULT_ADDRESS_ZIP)
        )

    }

    fun fetchRepresentativeByAddress(address: Address){
        viewModelScope.launch {
            Log.e(NETWORK_TAG, "fetchRepresentativeByAddress started")
             val result = repository.getRepresentativeFromApiByAddress(address)
            Log.e(NETWORK_TAG, "fetchRepresentativeByAddress finished")
            _upcomingElections.value = result
        }

    }

    fun getAddressFromGeoLocation(geoCoder: Geocoder, currentPositionCoordinates : LatLng): Address{
        return geoCodeLocation(geoCoder, currentPositionCoordinates)
    }


    fun getAddressFromFields(addressLine1 : String, addressLine2 : String, city: String, country : String, zip : String) : Address{
        return Address(addressLine1, addressLine2,city,country,zip)
    }
}
