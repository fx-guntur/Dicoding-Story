package com.example.dicodingstory.di.module

import com.example.dicodingstory.presentation.add.AddStoryViewModel
import com.example.dicodingstory.presentation.detail.DetailViewModel
import com.example.dicodingstory.presentation.home.HomeViewModel
import com.example.dicodingstory.presentation.map.MapsViewModel
import com.example.dicodingstory.presentation.profile.ProfileViewModel
import com.example.dicodingstory.presentation.signin.SigninViewModel
import com.example.dicodingstory.presentation.signup.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SignupViewModel(get()) }
    viewModel { SigninViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { MapsViewModel(get()) }
}