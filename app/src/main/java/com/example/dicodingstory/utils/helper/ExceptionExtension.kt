package com.example.dicodingstory.utils.helper

import com.example.dicodingstory.data.network.response.BasicResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.lang.reflect.Type

val gson = Gson()
val type: Type = object : TypeToken<BasicResponse>() {}.type

fun Exception.createResponse(): BasicResponse? {
    return when (this) {
        is HttpException -> {
            gson.fromJson(response()?.errorBody()?.charStream(), type)
        }

        else -> {
            BasicResponse(
                message = this.message ?: "",
                error = true
            )
        }
    }
}