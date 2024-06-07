package com.example.dicodingstory.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.base.LiveDataOperation
import com.example.dicodingstory.data.repository.StoryRepository
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addStory(
        photo: File,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): LiveData<ResultStatus<String>> = LiveDataOperation<ResultStatus<String>> {
        viewModelScope.launch {
            repository.setStory(photo, description, latitude, longitude).collect { postValue(it) }
        }
    }
}