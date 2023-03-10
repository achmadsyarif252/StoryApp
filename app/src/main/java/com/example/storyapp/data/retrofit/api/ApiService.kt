package com.example.storyapp.data.retrofit.api

import com.example.storyapp.data.retrofit.response.FileUploadResponse
import com.example.storyapp.data.retrofit.response.LoginResponse
import com.example.storyapp.data.retrofit.response.RegisterUserResponse
import com.example.storyapp.data.retrofit.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //register
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterUserResponse>

    //login
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    //get all stories
    @GET("stories")
    fun getStory(
        @Header("Authorization") authorization: String
    ): Call<StoryResponse>

    //get story wit location test
    @GET("stories?location=1")
    fun getStoryLocation(
        @Header("Authorization") authorization: String
    ): Call<StoryResponse>

    //post story
    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<FileUploadResponse>
}