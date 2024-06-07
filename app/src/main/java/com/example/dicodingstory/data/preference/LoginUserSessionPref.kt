package com.example.dicodingstory.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dicodingstory.data.model.User
import com.example.dicodingstory.utils.constant.AppConstants.PREFERENCE_SESSION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStoreSession: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_SESSION)

class LoginUserSessionPref(context: Context) {
    private val userTokenKey = stringPreferencesKey("token")
    private val userIdKey = stringPreferencesKey("user_id")
    private val usernameKey = stringPreferencesKey("username")

    private val dataStore = context.dataStoreSession

    suspend fun getToken(): String {
        val data = dataStore.data.first()

        return if (data.contains(userTokenKey)) {
            data[userTokenKey]!!
        } else {
            ""
        }
    }

    suspend fun setSession(user: User) {
        dataStore.edit { pref ->
            pref[userTokenKey] = user.token
            pref[userIdKey] = user.id
            pref[usernameKey] = user.name
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { pref ->
            User(
                pref[userTokenKey] ?: "",
                pref[userIdKey] ?: "",
                pref[usernameKey] ?: ""
            )
        }
    }

    suspend fun signOut() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

}