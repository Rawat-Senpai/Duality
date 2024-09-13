package com.example.dualityapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dualityapplication.api.AuthApi
import com.example.dualityapplication.models.ChangePasswordRequestModel
import com.example.dualityapplication.models.ChangePasswordResponseModel
import com.example.dualityapplication.models.ForgotPasswordRequestModel
import com.example.dualityapplication.models.ForgotPasswordResponseModel
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

    private val _userForgotPassword = MutableLiveData<NetworkResult<ForgotPasswordResponseModel>?>()

    val userForgotPassword :LiveData<NetworkResult<ForgotPasswordResponseModel>?> get()= _userForgotPassword


    private val _changePasswordResponse = MutableLiveData<NetworkResult<ChangePasswordResponseModel>?>()

    val changePasswordResponse :LiveData<NetworkResult<ChangePasswordResponseModel>?> get()= _changePasswordResponse



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


    suspend fun forgotPassword(data:ForgotPasswordRequestModel){
        _userForgotPassword.postValue(NetworkResult.Loading())
        val result = BaseUtils.safeApiCall { authApi.forgorPassword(data) }
        _userForgotPassword.postValue(result)

    }

    suspend fun changePassword(data:ChangePasswordRequestModel,token:String){
        _changePasswordResponse.postValue(NetworkResult.Loading())
        val result = BaseUtils.safeApiCall { authApi.changePassword(data,token) }
        _changePasswordResponse.postValue(result)
    }


}