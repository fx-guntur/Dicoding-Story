package com.example.dicodingstory.utils.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this) {
        it?.let { t -> action(t) }
    }
}