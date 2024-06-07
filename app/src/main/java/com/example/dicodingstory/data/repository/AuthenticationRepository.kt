package com.example.dicodingstory.data.repository

import com.example.dicodingstory.data.datasource.AuthenticationDataSource
import com.example.dicodingstory.data.model.User
import com.example.dicodingstory.data.network.request.SigninRequest
import com.example.dicodingstory.data.network.request.SignupRequest
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AuthenticationRepository(
    private val dataSource: AuthenticationDataSource
) {
    suspend fun signIn(
        email: String,
        pass: String
    ): Flow<ResultStatus<String>> {
        val request = SigninRequest(email, pass)
        return dataSource.signIn(request).flowOn(Dispatchers.IO)
    }

    suspend fun signUp(
        fullName: String,
        email: String,
        pass: String
    ): Flow<ResultStatus<String>> {
        val request = SignupRequest(fullName, email, pass)
        return dataSource.signUp(request).flowOn(Dispatchers.IO)
    }

    suspend fun signOut(): Flow<ResultStatus<String>> {
        return dataSource.signOut().flowOn(Dispatchers.IO)
    }

    fun getUserSession(): Flow<User> = dataSource.getUserSession().flowOn(Dispatchers.IO)
}