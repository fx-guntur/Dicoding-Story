package com.example.dicodingstory.data.datasource

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.dicodingstory.data.local.room.DicodingStoryDatabase
import com.example.dicodingstory.data.mediator.DicodingStoryRemoteMediator
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.data.network.services.StoryService
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.utils.constant.AppConstants.MULTIPART_FILE_NAME
import com.example.dicodingstory.utils.helper.createResponse
import com.example.dicodingstory.utils.helper.toMultipart
import com.example.dicodingstory.utils.helper.toRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File


class StoryDataSource(
    private val database: DicodingStoryDatabase,
    private val service: StoryService
) {
    suspend fun setStory(
        photo: File,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<ResultStatus<String>> = flow {
        emit(ResultStatus.Loading)
        try {
            val response = service.setStory(
                photo = photo.toMultipart(MULTIPART_FILE_NAME),
                description = description.toRequestBody(),
                latitude = latitude.toRequestBody(),
                longitude = longitude.toRequestBody()
            )

            if (response.error) {
                emit(ResultStatus.Error(response.message))
                return@flow
            }

            emit(ResultStatus.Success(response.message))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun getStoryDetail(idStory: String): Flow<ResultStatus<Story>> = flow {
        emit(ResultStatus.Loading)
        try {
            val response = service.getStoryDetail(idStory)
            if (response.error) {
                emit(ResultStatus.Error(response.message))
                return@flow
            }

            emit(ResultStatus.Success(response.story))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun getStory(
        page: Int? = null,
        size: Int? = null,
        isLocationEnabled: Boolean = false
    ) = flow {
        emit(ResultStatus.Loading)
        try {
            val response = service.getStory(
                page,
                size,
                location = if (isLocationEnabled) 1 else 0
            )

            if (response.error) {
                emit(ResultStatus.Error(response.message))
                return@flow
            }

            emit(ResultStatus.Success(response.listStory))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun fetchStory(
        size: Int?,
        isLocationEnabled: Boolean = false
    ): Flow<ResultStatus<List<Story>>> =
        getStory(size = size, isLocationEnabled = isLocationEnabled)

    suspend fun fetchStory(
        isLocationEnabled: Boolean = false
    ): Flow<ResultStatus<List<Story>>> = getStory(isLocationEnabled = isLocationEnabled)

    fun fetchPagingStory(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 2,
                initialLoadSize = 5
            ),
            remoteMediator = DicodingStoryRemoteMediator(database, service),
            pagingSourceFactory = {
                database.getDicodingStoryDao().getAllStories()
            }
        ).liveData
    }
}