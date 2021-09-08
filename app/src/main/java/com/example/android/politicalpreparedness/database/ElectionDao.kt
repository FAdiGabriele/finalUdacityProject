package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addElection(election: Election)

    @Update
    suspend fun updateElection(election: Election)

    @Query("SELECT * FROM election_table")
    fun getElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElection(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()

}