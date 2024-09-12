package com.example.dualityapplication.api

import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.models.SignUpRequestModel
import com.example.dualityapplication.models.SignUpResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {


    @POST("user/loginWithPhone")
    suspend fun loginUser(@Body body: LoginRequestModel): Response<LoginResponsModel>


    @POST("user/signup")
    suspend fun signUpUser(@Body body: SignUpRequestModel): Response<SignUpResponseModel>


}