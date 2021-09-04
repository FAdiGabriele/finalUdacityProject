package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.application.PoliticalPreparedness
import com.example.android.politicalpreparedness.dagger.ViewModelFactory
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import javax.inject.Inject

class ElectionsFragment: Fragment() {



    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var electionViewModel: ElectionsViewModel

    lateinit var binding : FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        PoliticalPreparedness.appComponent.inject(this)

        binding = FragmentElectionBinding.inflate(inflater, container, false)

        //TODO: Add ViewModel values and create ViewModel
        electionViewModel = ViewModelProvider(this, viewModelFactory)[ElectionsViewModel::class.java]

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}