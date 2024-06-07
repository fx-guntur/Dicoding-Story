package com.example.dicodingstory.data.network.services

import com.example.dicodingstory.data.network.response.BasicResponse
import com.example.dicodingstory.data.network.response.DetailStoryResponse
import com.example.dicodingstory.data.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): DetailStoryResponse

    @POST("stories")
    @Multipart
    suspend fun setStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null,
    ): BasicResponse
}