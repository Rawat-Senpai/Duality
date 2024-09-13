package com.example.dualityapplication.api

import com.example.dualityapplication.models.ChangePasswordRequestModel
import com.example.dualityapplication.models.ChangePasswordResponseModel
import com.example.dualityapplication.models.ForgotPasswordRequestModel
import com.example.dualityapplication.models.ForgotPasswordResponseModel
import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.models.SignUpRequestModel
import com.example.dualityapplication.models.SignUpResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {


    @POST("user/loginWithPhone")
    suspend fun loginUser(@Body body: LoginRequestModel): Response<LoginResponsModel>


    @POST("user/signup")
    suspend fun signUpUser(@Body body: SignUpRequestModel): Response<SignUpResponseModel>


    @POST("user/forget/Password")
    suspend fun forgorPassword(@Body body: ForgotPasswordRequestModel):Response<ForgotPasswordResponseModel>

    @POST("user/changePassword/{token}")
    suspend fun changePassword(@Body body: ChangePasswordRequestModel , @Path("token") token:String):Response<ChangePasswordResponseModel>

}