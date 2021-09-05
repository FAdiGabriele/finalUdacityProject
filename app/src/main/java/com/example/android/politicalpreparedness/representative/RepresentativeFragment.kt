package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.application.PoliticalPreparedness
import com.example.android.politicalpreparedness.dagger.ViewModelFactory
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.example.android.politicalpreparedness.utils.Constants.TAG
import com.example.android.politicalpreparedness.utils.askToTurnOnLocation
import com.example.android.politicalpreparedness.utils.checkLocationPermissions
import com.example.android.politicalpreparedness.utils.getLocation
import java.util.*
import javax.inject.Inject

class RepresentativeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var representativeViewModel: RepresentativeViewModel

    private lateinit var geocoder : Geocoder
    private lateinit var binding : FragmentRepresentativeBinding
    private lateinit var adapter: RepresentativeListAdapter
    private var notFirstOpen = false
    private lateinit var locationManager : LocationManager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        Log.e(TAG, "onRepresentativeFragment")

        PoliticalPreparedness.appComponent.inject(this)

        binding =  FragmentRepresentativeBinding.inflate(inflater, container, false)

        representativeViewModel =  ViewModelProvider(this, viewModelFactory)[RepresentativeViewModel::class.java]


        prepareGeoCodingVariables()
        prepareLayout()
        setObservers()

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        if(notFirstOpen) representativeViewModel.locationRequestedAndApproved.value = true

        notFirstOpen = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //TODO settalo bene
        if(checkLocationPermissions(this)){
            askToTurnOnLocation(this, {
                getLocation(locationManager, representativeViewModel.currentPosition)
            })
        }
    }

    fun prepareLayout(){

        adapter = RepresentativeListAdapter(RepresentativeListener{})

        binding.buttonSearch.setOnClickListener {
            val searchedAddress =  representativeViewModel.getAddressFromFields(
                binding.addressLine1.text.toString(),
                binding.addressLine2.text.toString(),
                binding.city.text.toString(),
                binding.state.selectedItem.toString(),
                binding.zip.text.toString()
            )

            representativeViewModel.fetchRepresentativeByAddress(
                searchedAddress
            )
        }

        binding.buttonLocation.setOnClickListener {
            if(checkLocationPermissions(this)){
                askToTurnOnLocation(this, {
                    getLocation(locationManager, representativeViewModel.currentPosition)
                })
            }
        }

        binding.representativeList.adapter = adapter
        binding.representativeList.layoutManager = LinearLayoutManager(requireContext())
    }

    fun prepareGeoCodingVariables(){
        locationManager = requireActivity().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(requireContext(), Locale.US)

    }

    fun setObservers(){
        representativeViewModel.upcomingElections.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        representativeViewModel.currentPosition.observe(viewLifecycleOwner){
            val currentAddress = representativeViewModel.getAddressFromGeoLocation(geocoder, it)

            binding.addressLine1.setText(currentAddress.line1)
            binding.addressLine2.setText(currentAddress.line2)
            binding.city.setText(currentAddress.city)
            binding.zip.setText(currentAddress.zip)

            binding.state.setNewValue(currentAddress.state)

            representativeViewModel.fetchRepresentativeByAddress(
                currentAddress
            )
        }

        representativeViewModel.locationRequestedAndApproved.observe(viewLifecycleOwner) { value ->
            if (value) {
                representativeViewModel.locationRequestedAndApproved.value = false
                if (checkLocationPermissions(this)) {
                    askToTurnOnLocation(this, {
                        getLocation(locationManager, representativeViewModel.currentPosition)
                    })
                }
            }
        }


    }




}