package com.example.dicodingstory.data.network

import com.example.dicodingstory.data.preference.LoginUserSessionPref
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val pref: LoginUserSessionPref
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val token = runBlocking {
            pref.getToken()
        }

        return if (token.isEmpty()) {
            chain.proceed(origin)
        } else {
            val auth = origin.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json").build()
            chain.proceed(auth)
        }
    }

}