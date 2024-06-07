package com.example.dicodingstory.di.module

import android.app.Application
import androidx.room.Room
import com.example.dicodingstory.data.local.room.DicodingStoryDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

fun provideDatabase(application: Application): DicodingStoryDatabase {
    return Room.databaseBuilder(application, DicodingStoryDatabase::class.java, "dicoding-db")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideStoryDao(database: DicodingStoryDatabase) = database.getDicodingStoryDao()

val localModule = module {
    single { provideStoryDao(get()) }
    single { provideDatabase(androidApplication()) }
}