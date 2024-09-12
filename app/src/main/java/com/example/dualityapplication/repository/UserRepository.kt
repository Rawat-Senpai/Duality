package com.example.dualityapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dualityapplication.api.AuthApi
import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.models.SignUpRequestModel
import com.example.dualityapplication.models.SignUpResponseModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.NetworkResult
import javax.inject.Inject

class UserRepository @Inject constructor(private val authApi: AuthApi) {

    private val _loginUser = MutableLiveData<NetworkResult<LoginResponsModel>?>()

    val loginUser :LiveData<NetworkResult<LoginResponsModel>?> get() = _loginUser


    private val _signUpUser = MutableLiveData<NetworkResult<SignUpResponseModel>?>()

    val signUpUser :LiveData<NetworkResult<SignUpResponseModel>?> get() = _signUpUser


    suspend fun loginUser(user:LoginRequestModel){

        _loginUser.postValue(NetworkResult.Loading())
        val result = BaseUtils.safeApiCall { authApi.loginUser(user) }
        _loginUser.postValue(result)


    }

    suspend fun signUpUser(user:SignUpRequestModel){

        _signUpUser.postValue(NetworkResult.Loading())
        val result = BaseUtils.safeApiCall { authApi.signUpUser(user) }
        _signUpUser.postValue(result)


    }



}