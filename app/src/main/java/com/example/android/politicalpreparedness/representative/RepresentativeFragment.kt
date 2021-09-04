package com.example.android.politicalpreparedness.representative

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
import com.example.android.politicalpreparedness.utils.Constants.TAG
import com.example.android.politicalpreparedness.utils.handleRequestPermission
import javax.inject.Inject

class RepresentativeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var representativeViewModel: RepresentativeViewModel

    lateinit var binding : FragmentRepresentativeBinding
    lateinit var adapter: RepresentativeListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        Log.e(TAG, "onRepresentativeFragment")

        PoliticalPreparedness.appComponent.inject(this)

        binding =  FragmentRepresentativeBinding.inflate(inflater, container, false)

        representativeViewModel =  ViewModelProvider(this, viewModelFactory)[RepresentativeViewModel::class.java]

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
            representativeViewModel.getAddressFromGeoLocation()
        }

        binding.representativeList.adapter = adapter
        binding.representativeList.layoutManager = LinearLayoutManager(requireContext())
        setObservers()

        return binding.root

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //TODO settalo bene
        handleRequestPermission()
    }

    fun setObservers(){
        representativeViewModel.upcomingElections.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }




}