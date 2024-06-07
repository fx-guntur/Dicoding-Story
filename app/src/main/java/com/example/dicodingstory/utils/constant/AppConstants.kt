package com.example.dicodingstory.utils.constant

import android.Manifest

object AppConstants {
    const val PREFERENCE_SETTING = "setting.pref"
    const val PREFERENCE_SESSION = "session.pref"
    const val MAX_SIZE = 1000000
    const val MULTIPART_FORM_DATA = "multipart/form-data"
    const val MULTIPART_FILE_NAME = "photo"

    val LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}