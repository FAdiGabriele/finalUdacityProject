package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Constants.NETWORK_TAG
import retrofit2.Call
import retrofit2.Response
import retrofit2.await

class ElectionsRepository (private val database: ElectionDatabase ) {

    val electionsLocalList: LiveData<List<Election>> = Transformations.map(
        database.electionDao.getElections()
    ) { it }

    private var _electionsApiList = MutableLiveData<List<Election>>()
    val electionsApiList: LiveData<List<Election>>
        get() = _electionsApiList

    private var _voterInfoResponseApiList = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponseApiList: LiveData<VoterInfoResponse>
        get() = _voterInfoResponseApiList


    fun getElectionsFromApi() {
        val call = CivicsApi.retrofitService.getElections()

        call.enqueue(object : retrofit2.Callback<ElectionResponse> {
            override fun onResponse(
                call: Call<ElectionResponse>,
                response: Response<ElectionResponse>
            ) {
                Log.e(NETWORK_TAG, "getElectionsFromApi Response")
                _electionsApiList.value = response.body()?.elections
            }

            override fun onFailure(call: Call<ElectionResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    suspend fun getVoterInfoFromApi(address: Address) {
        val call = CivicsApi.retrofitService.getVoterInfoByAddress(address.toFormattedString())

        val some = call.await()
    }


    suspend fun getRepresentativeFromApiByAddress(address: Address): List<Representative> {
        val call =
            CivicsApi.retrofitService.getRepresentativesByAddress(address.toFormattedString())

        val (offices, officials) = call.await()
        return offices.flatMap { office -> office.getRepresentatives(officials) }
    }

    suspend fun getRepresentativeFromApiByDivision(division: Division): List<Representative> {
        val call = CivicsApi.retrofitService.getRepresentativesByDivision(
            ElectionAdapter().divisionToJson(division)
        )

        val (offices, officials) = call.await()
        return offices.flatMap { office -> office.getRepresentatives(officials) }
    }
}