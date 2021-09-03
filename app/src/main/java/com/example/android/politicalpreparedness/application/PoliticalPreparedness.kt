package com.example.android.politicalpreparedness.application

import android.app.Application
import com.example.android.politicalpreparedness.dagger.AppComponent
import com.example.android.politicalpreparedness.dagger.DaggerAppComponent
import com.example.android.politicalpreparedness.dagger.ViewModelModule
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.repository.ElectionsRepository


class PoliticalPreparedness: Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().viewModelModule(ViewModelModule(
            ElectionsRepository(ElectionDatabase.getInstance(applicationContext)))).build()
    }
}

