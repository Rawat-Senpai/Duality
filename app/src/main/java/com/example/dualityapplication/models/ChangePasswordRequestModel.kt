package com.example.dualityapplication.models

data class ChangePasswordRequestModel(
    val confirmPassword: String,
    val newPassword: String,
    val otp: String
)