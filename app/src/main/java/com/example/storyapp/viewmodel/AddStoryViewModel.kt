package com.example.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.R
import com.example.storyapp.retrofit.api.ApiConfig
import com.example.storyapp.retrofit.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(context: Context) : ViewModel() {
    private val applicationContext = context.applicationContext

    private var _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> = _isUploading

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
                        _msg.value = applicationContext.getString(R.string.success_image_upload)
                    }
                } else {
                    _msg.value =
                        applicationContext.getString(R.string.upload_fail, response.errorBody())
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isUploading.value = false
                _msg.value = applicationContext.getString(R.string.upload_fail, t.message)
            }

        })


    }
}
