package com.example.dicodingstory.domain.common

sealed class ResultStatus<out R> private constructor() {
    data object Loading : ResultStatus<Nothing>()
    data class Success<out T>(val data: T) : ResultStatus<T>()
    data class Error(val message: String) : ResultStatus<Nothing>()
}