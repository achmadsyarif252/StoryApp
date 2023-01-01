package com.example.storyapp.model

data class UserModel(
    val uId: String,
    val name: String,
    val isLogin: Boolean,
    val token: String = ""
)