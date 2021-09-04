package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Constants
import com.example.android.politicalpreparedness.utils.Constants.NETWORK_TAG
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepresentativeViewModel @Inject constructor(val repository: ElectionsRepository) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Representative>>()
    val upcomingElections : LiveData<List<Representative>>
        get() = _upcomingElections

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

    //TODO: Create function to fetch representatives from API from a provided address
    fun fetchRepresentativeByAddress(address: Address){
        viewModelScope.launch {
            Log.e(NETWORK_TAG, "fetchRepresentativeByAddress started")
             val result = repository.getRepresentativeFromApiByAddress(address)
            Log.e(NETWORK_TAG, "fetchRepresentativeByAddress finished")
            _upcomingElections.value = result
        }

    }

    //TODO: Create function get address from geo location
    fun getAddressFromGeoLocation(){

    }


    fun getAddressFromFields(addressLine1 : String, addressLine2 : String, city: String, country : String, zip : String) : Address{
        return Address(addressLine1, addressLine2,city,country,zip)
    }
}
