package com.example.dualityapplication.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Util {

    const val CAMERA_PERMISSION_CODE = 100
    const val STORAGE_PERMISSION_CODE = 101

    const val MALE="MALE"
    const val FEMALE ="FEMALE"
    const val OTHERS="OTHERS"

    const val USER_TOKEN = "user_token"
    const val PREFS_TOKEN_FILE = "prefs_token_file"

    const val BASE_URL="https://ayooba-duality-project.vercel.app/api/v1/"
    fun checkPermission(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

}