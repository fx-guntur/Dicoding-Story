package com.example.dicodingstory.di.module

import com.example.dicodingstory.BuildConfig
import com.example.dicodingstory.data.network.HeaderInterceptor
import com.example.dicodingstory.data.network.services.AuthenticationService
import com.example.dicodingstory.data.network.services.StoryService
import com.example.dicodingstory.data.preference.LoginUserSessionPref
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {
    single { getHttpClient(get()) }
    single { getRetrofit(get()) }
    single { getAuthService(get()) }
    single { getStoryService(get()) }
}

fun getHttpClient(
    loginUserSessionPref: LoginUserSessionPref
): OkHttpClient {
    return OkHttpClient
        .Builder()
        .addInterceptor(HeaderInterceptor(loginUserSessionPref))
        .addInterceptor(httpLoggingInterceptor)
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .build()
}

private val httpLoggingInterceptor = if (BuildConfig.DEBUG) {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
} else {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
}

fun getRetrofit(
    okHttpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getAuthService(retrofit: Retrofit): AuthenticationService =
    retrofit.create(AuthenticationService::class.java)

fun getStoryService(retrofit: Retrofit): StoryService =
    retrofit.create(StoryService::class.java)