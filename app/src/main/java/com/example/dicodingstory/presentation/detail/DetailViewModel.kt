package com.example.dicodingstory.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.base.LiveDataOperation
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.data.repository.StoryRepository
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStory(idStory: String): LiveData<ResultStatus<Story>> =
        LiveDataOperation<ResultStatus<Story>> {
            viewModelScope.launch {
                repository.getDetail(idStory).collect {
                    postValue(it)
                }
            }
        }
}