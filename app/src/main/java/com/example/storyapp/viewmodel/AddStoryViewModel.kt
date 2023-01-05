package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.retrofit.api.ApiConfig
import com.example.storyapp.data.retrofit.response.FileUploadResponse
import com.example.storyapp.data.retrofit.response.LoginResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {

    private var _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> = _isUploading

    private var _isError = MutableLiveData<Boolean>()
    val iserror: LiveData<Boolean> = _isError

    private var _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    fun uploadToServer(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ) {
        _isUploading.value = true
        val client =
            ApiConfig.getApiService().uploadImage("Bearer $token", imageMultipart, description)

        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isUploading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if ((responseBody != null) && (responseBody.error == false)) {
                        _msg.value = "success"
                        _isError.value = false
                    }
                } else {
                    val responseBody = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        LoginResponse::class.java
                    )
                    _msg.value = responseBody.message
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isUploading.value = false
                _msg.value = "Failed"
            }

        })


    }
}
