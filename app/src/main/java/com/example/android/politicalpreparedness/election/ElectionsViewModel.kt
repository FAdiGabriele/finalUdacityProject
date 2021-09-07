package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class ElectionsViewModel @Inject constructor(repository: ElectionsRepository) : ViewModel() {

    //TODO: Create live data val for upcoming elections
    val upcomingElections : LiveData<List<Election>> = Transformations.map(repository.electionsApiList) { it }

    //TODO: Create live data val for saved elections
    val savedElections : LiveData<List<Election>> = Transformations.map(repository.electionsLocalList) { it }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    init{
            repository.getElectionsFromApi()
    }



    //TODO: Create functions to navigate to saved or upcoming election voter info

}