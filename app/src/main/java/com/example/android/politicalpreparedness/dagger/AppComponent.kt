package com.example.android.politicalpreparedness.dagger

import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.election.VoterInfoFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ViewModelModule::class)])
interface  AppComponent {
    fun inject(fragment: ElectionsFragment)
    fun inject(fragment: VoterInfoFragment)
}