package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.application.PoliticalPreparedness
import com.example.android.politicalpreparedness.dagger.ViewModelFactory
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import javax.inject.Inject

class ElectionsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var electionViewModel: ElectionsViewModel
    private lateinit var localAdapter: ElectionListAdapter
    private lateinit var apiAdapter: ElectionListAdapter

    lateinit var binding : FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        PoliticalPreparedness.appComponent.inject(this)

        binding = FragmentElectionBinding.inflate(inflater, container, false)

        electionViewModel = ViewModelProvider(this, viewModelFactory)[ElectionsViewModel::class.java]


        localAdapter = ElectionListAdapter(ElectionListener {
            val args = Bundle()
            args.putInt("arg_election_id", it.id)
            args.putParcelable("arg_division", it.division)
            findNavController().navigate(R.id.action_electionsFragment_to_voterInfoFragment, args)
        })
        apiAdapter = ElectionListAdapter(ElectionListener {
            val args = Bundle()
            args.putInt("arg_election_id", it.id)
            args.putParcelable("arg_division", it.division)
            findNavController().navigate(R.id.action_electionsFragment_to_voterInfoFragment, args)
        })

        setObservers()

        binding.apiRecyclerview.adapter = apiAdapter
        binding.apiRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.localReciclerview.adapter = localAdapter
        binding.localReciclerview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root

    }

    private fun setObservers(){
        electionViewModel.savedElections.observe(viewLifecycleOwner) {
            localAdapter.submitList(it)
        }

        electionViewModel.upcomingElections.observe(viewLifecycleOwner) {
            apiAdapter.submitList(it)
        }
    }

}