package com.example.dicodingstory.utils.helper

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar

fun ImageView.setImageUrl(url: String?) {
    Glide.with(this.rootView).load(url).apply(RequestOptions())
        .into(this)
}

fun EditText.showError(message: String) {
    error = message
    requestFocus()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
    }.show()
}