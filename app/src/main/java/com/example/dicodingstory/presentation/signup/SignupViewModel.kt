package com.example.dicodingstory.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.base.LiveDataOperation
import com.example.dicodingstory.data.repository.AuthenticationRepository
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    fun signUp(
        name: String,
        email: String,
        pass: String
    ): LiveData<ResultStatus<String>> {
        return LiveDataOperation<ResultStatus<String>> {
            viewModelScope.launch {
                repository.signUp(name, email, pass).collect { postValue(it) }
            }
        }
    }
}