package com.example.dicodingstory.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.base.LiveDataOperation
import com.example.dicodingstory.data.model.User
import com.example.dicodingstory.data.repository.AuthenticationRepository
import com.example.dicodingstory.domain.common.ResultStatus
import kotlinx.coroutines.launch

class SigninViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    fun signIn(
        email: String,
        pass: String,
    ): LiveData<ResultStatus<String>> {
        return LiveDataOperation<ResultStatus<String>> {
            viewModelScope.launch {
                repository.signIn(email, pass).collect {
                    postValue(it)
                }
            }
        }
    }

    fun getSession(): LiveData<User> {
        return repository.getUserSession().asLiveData()
    }
}