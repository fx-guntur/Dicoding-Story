package com.example.dicodingstory.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dicodingstory.data.model.LanguageOption
import com.example.dicodingstory.data.model.ThemeOption
import com.example.dicodingstory.utils.constant.AppConstants.PREFERENCE_SETTING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStoreSetting: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_SETTING)

class SettingPreference(context: Context) {
    private val dataStore = context.dataStoreSetting

    private val themePrefKey = stringPreferencesKey("theme_setting")
    private val languagePrefKey = stringPreferencesKey("language_setting")

    fun getTheme(): Flow<ThemeOption> {
        return dataStore.data.map { preferences ->
            val themeString = preferences[themePrefKey] ?: ThemeOption.SYSTEM.name
            ThemeOption.valueOf(themeString)
        }
    }

    suspend fun setTheme(themeMode: ThemeOption) {
        dataStore.edit { preferences ->
            preferences[themePrefKey] = themeMode.name
        }
    }

    fun getLanguage(): Flow<LanguageOption> {
        return dataStore.data.map { preferences ->
            val languageString = preferences[languagePrefKey] ?: LanguageOption.SYSTEM.name
            LanguageOption.valueOf(languageString)
        }
    }

    suspend fun setLanguage(language: LanguageOption) {
        dataStore.edit { preferences ->
            preferences[languagePrefKey] = language.name
        }
    }
}