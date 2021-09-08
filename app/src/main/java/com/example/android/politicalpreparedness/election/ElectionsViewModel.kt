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

    val upcomingElections : LiveData<List<Election>> = Transformations.map(repository.electionsApiList) { it }

    val savedElections : LiveData<List<Election>> = Transformations.map(repository.electionsLocalList) { it }

    init{
        repository.getElectionsFromApi()
    }

}