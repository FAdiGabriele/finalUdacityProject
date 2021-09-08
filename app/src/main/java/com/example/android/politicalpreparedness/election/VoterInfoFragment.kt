package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.application.PoliticalPreparedness
import com.example.android.politicalpreparedness.dagger.ViewModelFactory
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.utils.Constants.NETWORK_TAG
import kotlinx.android.synthetic.main.fragment_voter_info.view.*
import javax.inject.Inject

class VoterInfoFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var args : VoterInfoFragmentArgs
    var electionNotFoundInLocalDb = false
    var stateLocationURL = ""
    var ballotURL = ""

    lateinit var binding: FragmentVoterInfoBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        PoliticalPreparedness.appComponent.inject(this)


        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory)[VoterInfoViewModel::class.java]

        setObservers()

        arguments?.let{
            args = VoterInfoFragmentArgs.fromBundle(it)

            viewModel.getElectionFromDatabase(args.argElectionId)

            viewModel.getVoterInfoFromApi(
                viewModel.getAddressFromDivision(args.argDivision),
                args.argElectionId
            )
        } ?: Runnable {
            Toast.makeText(requireContext(), resources.getString(R.string.application_error), Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }


        return binding.root

    }

    private fun setObservers(){
        viewModel.voterInfo.observe(viewLifecycleOwner) { response ->

            //we get Election from Api
            if(electionNotFoundInLocalDb) {
                binding.electionName.title = response.election.name
                binding.electionDate.text = response.election.electionDay.toString()
                binding.stateCorrespondenceHeader.text = response.state?.get(0)?.name

                binding.followButton.setOnClickListener {
                    viewModel.addElectionToDatabase(response.election)
                    findNavController().popBackStack()
                }
            }

            binding.address.text = response.state?.get(0)?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()
            stateLocationURL = response.state?.get(0)?.electionAdministrationBody?.electionInfoUrl.toString()
            ballotURL = response.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl.toString()
            Log.e(NETWORK_TAG, "stateLocationURL: $stateLocationURL")
            Log.e(NETWORK_TAG, "ballotURL: $ballotURL")

            binding.stateLocations.setOnClickListener {
                viewModel.generateURLIntent(requireContext(), stateLocationURL)
            }

            binding.stateBallot.setOnClickListener {
                viewModel.generateURLIntent(requireContext(),ballotURL)
            }
        }

        viewModel.electionFound.observe(viewLifecycleOwner) { election ->
            if(election == null){
                //Election is not present so we need to request to Api
                electionNotFoundInLocalDb = true

                binding.followButton.text = resources.getString(R.string.follow_election)
            }else{
                //Election is present so we take all data from DB

                electionNotFoundInLocalDb = false

                binding.electionName.title = election.name
                binding.electionDate.text = election.electionDay.toString()
                binding.stateCorrespondenceHeader.text = election.division.state

                binding.followButton.apply {
                    this.text = resources.getString(R.string.unfollow_election)
                    this.setOnClickListener {
                        viewModel.removeElectionFromDatabase(election.id)
                        findNavController().popBackStack()
                    }
                }
            }
        }

        viewModel.getMalformedVoterInfoResponse.observe(viewLifecycleOwner){ getError ->
            if(getError){
                viewModel.getMalformedVoterInfoResponse.value = false
                Toast.makeText(requireContext(), resources.getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
}