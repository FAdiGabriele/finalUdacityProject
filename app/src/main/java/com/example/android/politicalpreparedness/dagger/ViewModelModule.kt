package com.example.android.politicalpreparedness.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule(
    private val electionsRepository: ElectionsRepository
) {

    @Provides
    @IntoMap
    @ViewModelKey(ElectionsViewModel::class)
    fun electionsViewModel(): ViewModel  =
        ElectionsViewModel(electionsRepository)

    @Provides
    @IntoMap
    @ViewModelKey(VoterInfoViewModel::class)
    fun voterInfoViewModel(): ViewModel  =
        VoterInfoViewModel(electionsRepository)

    @Provides
    fun provideViewModelFactory(map: Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModel>): ViewModelProvider.Factory {
        return ViewModelFactory(map)
    }
}