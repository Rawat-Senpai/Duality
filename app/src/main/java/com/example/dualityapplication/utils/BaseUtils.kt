package com.example.dualityapplication.utils

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.dualityapplication.models.LoginResponsModel
import retrofit2.Response
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
                    Log.d("checkingDataSuccess",response.toString())
                    NetworkResult.Success(response.body()!!)
                } else if (response.isSuccessful && response.body() == null) {
                    Log.d("checkingDataFail",response.toString())
                    NetworkResult.Error("Response body is null")
                } else if(response.code()==409) {
                    Log.d("checkingDataError",response.toString())
                    NetworkResult.Error("User Already Registered")
                }else{
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                Log.d("checkingDataCrash",e.toString())
                NetworkResult.Error(e.message ?: "An unknown error occurred")
            }
        }

    }


}