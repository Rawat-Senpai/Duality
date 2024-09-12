package com.example.dualityapplication.models

data class SignUpResponseModel(
    val `data`: String,
    val message: String,
    val savedUser: SavedUser,
    val status: Int
)