package com.example.dicodingstory.di.module

import com.example.dicodingstory.data.datasource.AuthenticationDataSource
import com.example.dicodingstory.data.repository.AuthenticationRepository
import org.koin.dsl.module

val authenticationDataSource = module {
    single {
        AuthenticationDataSource(get(), get())
    }
}

val authenticationRepository = module {
    single {
        AuthenticationRepository(get())
    }
}