package com.example.dualityapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dualityapplication.api.AuthApi
import com.example.dualityapplication.api.UserApi
import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.models.SignUpRequestModel
import com.example.dualityapplication.models.SignUpResponseModel
import com.example.dualityapplication.models.UserProfileRequestModel
import com.example.dualityapplication.models.UserProfileResponseModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.NetworkResult
import javax.inject.Inject

class ProfileRepository  @Inject constructor(private val userApi: UserApi) {

    private val _profileUser = MutableLiveData<NetworkResult<UserProfileResponseModel>?>()

    val profileUser : LiveData<NetworkResult<UserProfileResponseModel>?> get() = _profileUser


    suspend fun createUserProfile(user: UserProfileRequestModel){

        _profileUser.postValue(NetworkResult.Loading())
        val result = BaseUtils.safeApiCall { userApi.userRegister(user) }
        _profileUser.postValue(result)


    }

}