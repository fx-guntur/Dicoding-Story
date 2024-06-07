package com.example.dicodingstory.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.base.LiveDataOperation
import com.example.dicodingstory.data.model.LanguageOption
import com.example.dicodingstory.data.model.ThemeOption
import com.example.dicodingstory.data.preference.SettingPreference
import com.example.dicodingstory.data.repository.AuthenticationRepository
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val preference: SettingPreference,
    private val repository: AuthenticationRepository
) : ViewModel() {
    fun getCurrentTheme(): LiveData<ThemeOption> = preference.getTheme().asLiveData()
    fun getCurrentLanguage(): LiveData<LanguageOption> = preference.getLanguage().asLiveData()

    fun setThemeSetting(themeOption: ThemeOption) {
        viewModelScope.launch {
            preference.setTheme(themeOption)
        }
    }

    fun setLanguageSetting(languageOption: LanguageOption) {
        viewModelScope.launch {
            preference.setLanguage(languageOption)
        }
    }

    val signOut: LiveData<ResultStatus<String>> = LiveDataOperation<ResultStatus<String>> {
        viewModelScope.launch {
            repository.signOut()
                .collect {
                    postValue(it)
                }
        }
    }
}