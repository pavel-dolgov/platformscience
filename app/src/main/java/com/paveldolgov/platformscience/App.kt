package com.paveldolgov.platformscience

import android.app.Application
import com.paveldolgov.platformscience.servise.DataRefreshService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var dataRefreshService: DataRefreshService

    override fun onCreate() {
        super.onCreate()
        dataRefreshService.refresh()
    }
}