package com.example.dicodingstory.utils.helper

import java.text.SimpleDateFormat
import java.util.Locale

fun String.isEmailCorrect(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isFullNameCorrect(): Boolean {
    return !Regex("[^a-zA-Z ]").containsMatchIn(this)
}

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}