package com.example.dicodingstory.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.dicodingstory.data.datasource.StoryDataSource
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class StoryRepository(
    private val dataSource: StoryDataSource
) {
    suspend fun getStory(
        isLocationEnabled: Boolean = false
    ): Flow<ResultStatus<List<Story>>> = dataSource.fetchStory(isLocationEnabled).flowOn(
        Dispatchers.IO
    )

    suspend fun getDetail(
        idStory: String
    ): Flow<ResultStatus<Story>> = dataSource.getStoryDetail(idStory).flowOn(
        Dispatchers.IO
    )

    suspend fun setStory(
        photo: File,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<ResultStatus<String>> =
        dataSource.setStory(photo, description, latitude, longitude).flowOn(
            Dispatchers.IO
        )

    fun getPagingStory(): LiveData<PagingData<Story>> = dataSource.fetchPagingStory()

}