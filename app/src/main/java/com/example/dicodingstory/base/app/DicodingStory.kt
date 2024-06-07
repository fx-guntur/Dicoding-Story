package com.example.dicodingstory.base.app

import android.app.Application
import com.example.dicodingstory.BuildConfig
import com.example.dicodingstory.di.module.authenticationDataSource
import com.example.dicodingstory.di.module.authenticationRepository
import com.example.dicodingstory.di.module.localModule
import com.example.dicodingstory.di.module.preferenceModule
import com.example.dicodingstory.di.module.remoteModule
import com.example.dicodingstory.di.module.storyDataSourceModule
import com.example.dicodingstory.di.module.storyRepositoryModule
import com.example.dicodingstory.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class DicodingStory : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@DicodingStory)
            modules(
                listOf(
                    remoteModule,
                    preferenceModule,
                    authenticationDataSource,
                    authenticationRepository,
                    storyRepositoryModule,
                    storyDataSourceModule,
                    viewModelModule,
                    localModule
                )
            )
        }
    }
}