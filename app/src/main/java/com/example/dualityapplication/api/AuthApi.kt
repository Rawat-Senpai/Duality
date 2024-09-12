package com.example.dualityapplication.api

import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {


    @POST("user/loginWithPhone")
    suspend fun loginUser(@Body body: LoginRequestModel): Response<LoginResponsModel>


}