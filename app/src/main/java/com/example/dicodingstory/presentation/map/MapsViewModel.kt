package com.example.dicodingstory.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.base.LiveDataOperation
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.data.repository.StoryRepository
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.launch

class MapsViewModel(repository: StoryRepository) : ViewModel() {
    val recentStories: LiveData<ResultStatus<List<Story>>> =
        LiveDataOperation<ResultStatus<List<Story>>> {
            viewModelScope.launch {
                repository.getStory(true).collect {
                    postValue(it)
                }
            }
        }
}