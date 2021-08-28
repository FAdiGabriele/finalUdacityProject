package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert()
    suspend fun addElection(election: Election)

    @Update
    suspend fun updateElection(election: Election)

    @Query("SELECT * FROM election_table")
    suspend fun getElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): LiveData<Election>

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElection(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()

}