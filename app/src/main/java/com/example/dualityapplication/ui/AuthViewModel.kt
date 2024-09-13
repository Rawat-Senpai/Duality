package com.example.dualityapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualityapplication.models.ChangePasswordRequestModel
import com.example.dualityapplication.models.ChangePasswordResponseModel
import com.example.dualityapplication.models.ForgotPasswordRequestModel
import com.example.dualityapplication.models.ForgotPasswordResponseModel
import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.models.LoginResponsModel
import com.example.dualityapplication.models.SignUpRequestModel
import com.example.dualityapplication.models.SignUpResponseModel
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

   val userSignUpLiveData : LiveData<NetworkResult<SignUpResponseModel>?> get() = userRepository.signUpUser

      fun signUpUser(user:SignUpRequestModel){
         viewModelScope.launch {
            userRepository.signUpUser(user)
         }
      }

   val userForgotPasswordLiveData:LiveData<NetworkResult<ForgotPasswordResponseModel>?> get() = userRepository.userForgotPassword


   fun forgotPassword(user:ForgotPasswordRequestModel){
      viewModelScope.launch {
         userRepository.forgotPassword(user)
      }
   }


   val changePasswordLiveData:LiveData<NetworkResult<ChangePasswordResponseModel>?> get() = userRepository.changePasswordResponse


   fun changePassword(user:ChangePasswordRequestModel,token:String){
      viewModelScope.launch {
         userRepository.changePassword(user,token)
      }
   }



}