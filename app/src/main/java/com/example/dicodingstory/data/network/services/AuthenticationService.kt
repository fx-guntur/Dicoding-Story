package com.example.dicodingstory.data.network.services

import com.example.dicodingstory.data.network.request.SigninRequest
import com.example.dicodingstory.data.network.request.SignupRequest
import com.example.dicodingstory.data.network.response.BasicResponse
import com.example.dicodingstory.data.network.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("register")
    suspend fun register(
        @Body request: SignupRequest
    ): BasicResponse

    @POST("login")
    suspend fun login(
        @Body request: SigninRequest
    ): LoginResponse
}