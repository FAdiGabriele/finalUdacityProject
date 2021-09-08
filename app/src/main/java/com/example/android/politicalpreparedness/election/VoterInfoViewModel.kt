package com.example.android.politicalpreparedness.election

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class VoterInfoViewModel @Inject constructor(private val repository: ElectionsRepository) : ViewModel() {

    val voterInfo : LiveData<VoterInfoResponse> = Transformations.map(repository.voterInfoResponseApiList){ it }

    val getMalformedVoterInfoResponse : MutableLiveData<Boolean> = repository.getMalformedVoterInfoResponse

    private var _electionFound = MutableLiveData<Election?>()
    val electionFound : LiveData<Election?> get() = _electionFound


    fun addElectionToDatabase(election: Election){
        viewModelScope.launch {
            repository.addElection(election)
        }
    }

    fun removeElectionFromDatabase(id : Int){
        viewModelScope.launch {
            repository.removeElection(id)
        }
    }

    fun getElectionFromDatabase(id : Int){
        viewModelScope.launch {
            _electionFound.value = repository.getElectionById(id)
        }
    }


    fun getAddressFromDivision(division: Division): Address{
        return Address("", "", "", division.state, "")
    }

    fun getVoterInfoFromApi(address : Address, id : Int){
        repository.getVoterInfoFromApi(address, id)
    }

    fun generateURLIntent(context: Context, url : String){
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

}