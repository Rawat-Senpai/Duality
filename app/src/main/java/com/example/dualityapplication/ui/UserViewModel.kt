package com.example.dualityapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualityapplication.models.UserProfileRequestModel
import com.example.dualityapplication.models.UserProfileResponseModel
import com.example.dualityapplication.repository.ProfileRepository
import com.example.dualityapplication.repository.UserRepository
import com.example.dualityapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: ProfileRepository): ViewModel() {


    val userResponseLiveData : LiveData<NetworkResult<UserProfileResponseModel>?> get() = userRepository.profileUser

    fun loginUser(user: UserProfileRequestModel){
        viewModelScope.launch {
            userRepository.createUserProfile(user)
        }
    }


}
