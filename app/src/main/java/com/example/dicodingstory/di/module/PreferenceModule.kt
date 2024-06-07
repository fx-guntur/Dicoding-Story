package com.example.dicodingstory.di.module

import com.example.dicodingstory.data.preference.LoginUserSessionPref
import com.example.dicodingstory.data.preference.SettingPreference
import org.koin.dsl.module


val preferenceModule = module {
    single { LoginUserSessionPref(get()) }
    single { SettingPreference(get()) }
}