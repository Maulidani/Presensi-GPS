package com.skripsi.presensigps.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skripsi.presensigps.network.api.ApiClient
import com.skripsi.presensigps.network.model.LoginModel
import com.skripsi.presensigps.network.model.PresenceBackModel
import com.skripsi.presensigps.network.model.SalesInfoModel
import com.skripsi.presensigps.network.model.UserModel
import com.skripsi.presensigps.repository.Repository
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel {

    class LoginViewModel(private val repository: Repository = Repository(ApiClient.instances)) :
        ViewModel() {

        private var getLoginLiveData = MutableLiveData<ScreenState<LoginModel>>()

        val loginLiveData: LiveData<ScreenState<LoginModel>>
            get() = getLoginLiveData

        fun login(email: String, password: String) = viewModelScope.launch {
            getLoginLiveData.postValue(ScreenState.Loading(null))

            if (email.isNotEmpty() && password.isNotEmpty()) {
                repository.login(email, password)
                    .enqueue(
                        object : Callback<LoginModel> {
                            override fun onResponse(
                                call: Call<LoginModel>,
                                response: Response<LoginModel>
                            ) {
                                if (response.isSuccessful) {

                                    Log.e(this.toString(), "success: " + response.body())

                                    getLoginLiveData.postValue(ScreenState.Success(response.body()))

                                } else {

                                    Log.e(
                                        this.toString(),
                                        "not success: " + response.code().toString()
                                    )

                                    getLoginLiveData.postValue(
                                        ScreenState.Error(
                                            response.code().toString(),
                                            null
                                        )
                                    )
                                }
                            }

                            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                                Log.e("user", "failure: " + t.message.toString())

                                getLoginLiveData.postValue(
                                    ScreenState.Error(
                                        t.message.toString(),
                                        null
                                    )
                                )
                            }
                        })
            }
        }
    }

    class SalesInfoViewModel(private val repository: Repository = Repository(ApiClient.instances)) :
        ViewModel() {

        private var getSalesInfoLiveData = MutableLiveData<ScreenState<SalesInfoModel>>()

        private var getPresencebackLiveData = MutableLiveData<ScreenState<PresenceBackModel>>()

        val salesInfoLiveData: LiveData<ScreenState<SalesInfoModel>>
            get() = getSalesInfoLiveData

        val presencebackLiveData: LiveData<ScreenState<PresenceBackModel>>
            get() = getPresencebackLiveData

        fun salesInfo(token: String, userId: String) = viewModelScope.launch {
            getSalesInfoLiveData.postValue(ScreenState.Loading(null))

            repository.salesInfo("Bearer $token", userId.toInt())
                .enqueue(
                    object : Callback<SalesInfoModel> {
                        override fun onResponse(
                            call: Call<SalesInfoModel>,
                            response: Response<SalesInfoModel>
                        ) {
                            if (response.isSuccessful) {

                                Log.e(this.toString(), "success: " + response.body())

                                getSalesInfoLiveData.postValue(ScreenState.Success(response.body()))

                            } else {

                                Log.e(
                                    this.toString(),
                                    "not success: " + response.code().toString()
                                )

                                getSalesInfoLiveData.postValue(
                                    ScreenState.Error(
                                        response.code().toString(),
                                        null
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<SalesInfoModel>, t: Throwable) {
                            Log.e("user", "failure: " + t.message.toString())

                            getSalesInfoLiveData.postValue(
                                ScreenState.Error(
                                    t.message.toString(),
                                    null
                                )
                            )
                        }

                    })
        }

        fun presenceBack(id: String) = viewModelScope.launch {
            getPresencebackLiveData.postValue(ScreenState.Loading(null))

            repository.presenceBack(id.toInt()).enqueue(object : Callback<PresenceBackModel> {
                override fun onResponse(
                    call: Call<PresenceBackModel>,
                    response: Response<PresenceBackModel>
                ) {
                    if (response.isSuccessful) {

                        Log.e(this.toString(), "success: " + response.body())

                        getPresencebackLiveData.postValue(
                            ScreenState.Error(
                                response.code().toString(),
                                null
                            )
                        )
                    } else {

                        Log.e(
                            this.toString(),
                            "not success: " + response.code().toString()
                        )

                    }
                }

                override fun onFailure(call: Call<PresenceBackModel>, t: Throwable) {
                    Log.e(
                        this.toString(),
                        "fail: " + t.message.toString()
                    )

                    getPresencebackLiveData.postValue(
                        ScreenState.Error(
                            t.message.toString(),
                            null
                        )
                    )
                }

            })
        }
    }


    class UserInfoViewModel(private val repository: Repository = Repository(ApiClient.instances)) :
        ViewModel() {

        private var getUserLiveData = MutableLiveData<ScreenState<UserModel>>()

        val userLiveData: LiveData<ScreenState<UserModel>>
            get() = getUserLiveData

        fun user(token: String) = viewModelScope.launch {
            getUserLiveData.postValue(ScreenState.Loading(null))

                repository.user("Bearer $token")
                    .enqueue(
                        object : Callback<UserModel> {
                            override fun onResponse(
                                call: Call<UserModel>,
                                response: Response<UserModel>
                            ) {
                                if (response.isSuccessful) {

                                    Log.e(this.toString(), "success: " + response.body())

                                    getUserLiveData.postValue(ScreenState.Success(response.body()))

                                } else {

                                    Log.e(
                                        this.toString(),
                                        "not success: " + response.code().toString()
                                    )

                                    getUserLiveData.postValue(
                                        ScreenState.Error(
                                            response.code().toString(),
                                            null
                                        )
                                    )
                                }
                            }

                            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                                Log.e("user", "failure: " + t.message.toString())

                                getUserLiveData.postValue(
                                    ScreenState.Error(
                                        t.message.toString(),
                                        null
                                    )
                                )
                            }
                        })

        }
    }

}