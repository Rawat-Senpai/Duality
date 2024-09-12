package com.example.dualityapplication.api

import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.models.UserProfileRequestModel
import com.example.dualityapplication.models.UserProfileResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("user/registration")
    suspend fun userRegister(@Body body: UserProfileRequestModel): Response<UserProfileResponseModel>


}