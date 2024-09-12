package com.example.dualityapplication.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.TimeZone

class BaseUtils {

    companion object {

        const val weak = "Weak"
        const val medium = "Medium"
        const val strong = "Strong"


        fun showToast(context: Context?, msg: String?) {
            val toast = Toast.makeText(
                context,
                msg, Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 250)
            toast.show()
        }

        fun showLabelValidation(context: Context, textView: TextView, text: String) {
            textView.visibility = View.VISIBLE
            textView.text = text
        }

        suspend fun <T> safeApiCall(
            apiCall: suspend () -> Response<T>
        ): NetworkResult<T> {
            return try {
                val response = apiCall()
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else if (response.isSuccessful && response.body() == null) {
                    NetworkResult.Error("Response body is null")
                } else {
                    NetworkResult.Error(response.message())
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "An unknown error occurred")
            }
        }

    }


}