package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.*
import retrofit2.Call
import retrofit2.Response

class ElectionsRepository (private val database: ElectionDatabase ) {

    val electionsLocalList: LiveData<List<Election>> = Transformations.map(
        database.electionDao.getElections()) { it }

    private var _electionsApiList = MutableLiveData<List<Election>>()
    val electionsApiList : LiveData<List<Election>>
        get() =_electionsApiList

    private var _voterInfoResponseApiList = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponseApiList : LiveData<VoterInfoResponse>
        get() =_voterInfoResponseApiList

    private var _representativeResponseApiList = MutableLiveData<RepresentativeResponse>()
    val representativeResponseApiList : LiveData<RepresentativeResponse>
        get() =_representativeResponseApiList



    fun getElectionsFromApi(){
        val call = CivicsApi.retrofitService.getElections()

        call.enqueue(object : retrofit2.Callback<ElectionResponse> {
            override fun onResponse(
                call: Call<ElectionResponse>,
                response: Response<ElectionResponse>
            ) {
                _electionsApiList.value = response.body()?.elections
            }

            override fun onFailure(call: Call<ElectionResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getVoterInfoFromApi(address: Address){
        val call = CivicsApi.retrofitService.getVoterInfoByAddress(address.toFormattedString())

        call.enqueue(object : retrofit2.Callback<VoterInfoResponse> {
            override fun onResponse(
                call: Call<VoterInfoResponse>,
                response: Response<VoterInfoResponse>
            ) {
                _voterInfoResponseApiList.value = response.body()
            }

            override fun onFailure(call: Call<VoterInfoResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })

    }

    fun getRepresentativeFromApiByAddress(address: Address){
        val call = CivicsApi.retrofitService.getRepresentativesByAddress(address.toFormattedString())

        call.enqueue(object : retrofit2.Callback<RepresentativeResponse> {
            override fun onResponse(
                call: Call<RepresentativeResponse>,
                response: Response<RepresentativeResponse>
            ) {
                _representativeResponseApiList. value = response.body()
            }

            override fun onFailure(call: Call<RepresentativeResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }

    fun getRepresentativeFromApiByDivision(division: Division){
        val call = CivicsApi.retrofitService.getRepresentativesByDivision(ElectionAdapter().divisionToJson(division))

        call.enqueue(object : retrofit2.Callback<RepresentativeResponse> {
            override fun onResponse(
                call: Call<RepresentativeResponse>,
                response: Response<RepresentativeResponse>
            ) {
                _representativeResponseApiList.value = response.body()
            }

            override fun onFailure(call: Call<RepresentativeResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }
}