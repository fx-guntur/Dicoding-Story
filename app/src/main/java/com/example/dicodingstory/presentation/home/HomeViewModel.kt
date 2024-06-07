package com.example.dicodingstory.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.dicodingstory.data.repository.StoryRepository

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStory() = repository.getPagingStory().cachedIn(viewModelScope)
}