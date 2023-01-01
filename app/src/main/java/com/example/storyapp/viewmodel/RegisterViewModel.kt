package com.example.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.data.retrofit.api.ApiConfig
import com.example.storyapp.data.retrofit.response.RegisterUserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {
    private val _alertMsg = MutableLiveData<String>()
    val alertMessage: LiveData<String> = _alertMsg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun saveUser(userModel: UserModel) {
        viewModelScope.launch {
            pref.saveUser(userModel)
        }
    }

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback<RegisterUserResponse> {
            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                _isLoading.value = false
                _alertMsg.value = t.message.toString()
            }


            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        viewModelScope.launch {
                            pref.saveUser(
                                UserModel(
                                    "",
                                    name,
                                    false,
                                    ""
                                )
                            )
                        }
                        _isError.value = false
                        _alertMsg.value =
                            "Akunnya sudah jadi nih. Yuk, login dan lihat cerita seru teman-teman kamu."
                    } else {
                        _isError.value = true
                    }
                } else {
                    _alertMsg.value = response.message()
                    _isError.value = true
                }
            }
        })
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }
}