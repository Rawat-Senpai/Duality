package com.example.dualityapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.repository.UserRepository
import com.example.dualityapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository):ViewModel() {

   val userResponseLiveData :LiveData<NetworkResult<LoginResponsModel>?> get() = userRepository.loginUser

   fun loginUser(user:LoginRequestModel){
      viewModelScope.launch {
         userRepository.loginUser(user)
      }
   }


}