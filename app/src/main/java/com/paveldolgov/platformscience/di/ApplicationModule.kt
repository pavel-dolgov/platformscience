package com.paveldolgov.platformscience.di

import android.content.Context
import android.content.res.Resources
import com.paveldolgov.platformscience.client.Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun providePlatformScienceClient(): Client {
        return Client.Builder().build()
    }

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}