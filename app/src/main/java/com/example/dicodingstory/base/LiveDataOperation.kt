package com.example.dicodingstory.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataOperation<T>(private val operation: LiveDataOperation<T>.() -> Unit) :
    MutableLiveData<T>() {

    private var opStatus = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)

        operate()
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(observer)

        operate()
    }

    private fun operate() {
        if (value != null && opStatus.get()) {
            return
        }
        operation()
        opStatus.set(true)
    }
}