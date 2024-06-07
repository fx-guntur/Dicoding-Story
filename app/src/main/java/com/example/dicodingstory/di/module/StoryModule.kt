package com.example.dicodingstory.di.module

import com.example.dicodingstory.data.datasource.StoryDataSource
import com.example.dicodingstory.data.repository.StoryRepository
import org.koin.dsl.module

val storyDataSourceModule = module {
    single { StoryDataSource(get(), get()) }
}

val storyRepositoryModule = module {
    factory { StoryRepository(get()) }
}