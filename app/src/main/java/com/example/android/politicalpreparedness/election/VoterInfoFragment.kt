package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.application.PoliticalPreparedness
import com.example.android.politicalpreparedness.dagger.ViewModelFactory
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import javax.inject.Inject

class VoterInfoFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var voterInfoViewModel: VoterInfoViewModel

    lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        PoliticalPreparedness.appComponent.inject(this)

        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        //TODO: Add ViewModel values and create ViewModel
        voterInfoViewModel = ViewModelProvider(this, viewModelFactory)[VoterInfoViewModel::class.java]

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root

    }

    //TODO: Create method to load URL intents

}