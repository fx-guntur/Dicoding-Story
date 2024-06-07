package com.example.dicodingstory.data.datasource

import com.example.dicodingstory.data.network.request.SigninRequest
import com.example.dicodingstory.data.network.request.SignupRequest
import com.example.dicodingstory.data.network.services.AuthenticationService
import com.example.dicodingstory.data.preference.LoginUserSessionPref
import com.example.dicodingstory.di.module.preferenceModule
import com.example.dicodingstory.di.module.remoteModule
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.utils.helper.createResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthenticationDataSource(
    private val service: AuthenticationService,
    private val pref: LoginUserSessionPref
) {
    fun getUserSession() = pref.getSession()

    suspend fun signUp(request: SignupRequest): Flow<ResultStatus<String>> = flow {
        emit(ResultStatus.Loading)
        try {
            val response = service.register(request)
            if (response.error) {
                emit(ResultStatus.Error(response.message))
                return@flow
            }

            emit(ResultStatus.Success(response.message))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun signIn(request: SigninRequest): Flow<ResultStatus<String>> = flow {
        emit(ResultStatus.Loading)
        try {
            val response = service.login(request)
            if (response.error) {
                emit(ResultStatus.Error(response.message))
                return@flow
            }

            pref.setSession(response.loginResult)
            reloadModule()
            emit(ResultStatus.Success(response.message))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun signOut(): Flow<ResultStatus<String>> = flow {
        emit(ResultStatus.Loading)
        try {
            pref.signOut()
            reloadModule()
            emit(ResultStatus.Success("Sign Out Success!"))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.createResponse()?.message ?: ""))
        }
    }

    private fun reloadModule() {
        unloadKoinModules(preferenceModule)
        loadKoinModules(preferenceModule)

        unloadKoinModules(remoteModule)
        loadKoinModules(remoteModule)
    }
}