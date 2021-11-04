package com.skripsi.presensigps.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skripsi.presensigps.network.api.ApiClient
import com.skripsi.presensigps.network.model.PresenceIndexModel
import com.skripsi.presensigps.network.model.ReportIndexModel
import com.skripsi.presensigps.network.model.SalesInfoModel
import com.skripsi.presensigps.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresenceReportViewModel {
    class PresenceReportUser(
        private val repository: Repository = Repository(ApiClient.instances)
    ) :
        ViewModel() {

        private var _presenceLivedata = MutableLiveData<ScreenState<PresenceIndexModel>>()

        val presenceLiveData: LiveData<ScreenState<PresenceIndexModel>>
            get() = _presenceLivedata

        fun myPresence(userId: String)  = viewModelScope.launch {
            _presenceLivedata.postValue(ScreenState.Loading(null))

            repository.presenceUser(userId.toInt())
                .enqueue(object : Callback<PresenceIndexModel> {
                    override fun onResponse(
                        call: Call<PresenceIndexModel>,
                        response: Response<PresenceIndexModel>
                    ) {
                        if (response.isSuccessful) {
                            Log.e("presence", "success: " + response.body())
                            _presenceLivedata.postValue(ScreenState.Success(response.body()))

                        } else {
                            Log.e("presence", "not success: " + response.code().toString())
                            _presenceLivedata.postValue(
                                ScreenState.Error(
                                    response.code().toString(), null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<PresenceIndexModel>, t: Throwable) {
                        Log.e("presence", "failure: " + t.message.toString())
                        _presenceLivedata.postValue(ScreenState.Error(t.message.toString(), null))
                    }
                })
        }

        private var _reportLivedata = MutableLiveData<ScreenState<ReportIndexModel>>()

        val reportLiveData: LiveData<ScreenState<ReportIndexModel>>
            get() = _reportLivedata

        fun myReport(userId: String) = viewModelScope.launch {
            _reportLivedata.postValue(ScreenState.Loading(null))

            repository.reportUser(userId.toInt())
                .enqueue(object : Callback<ReportIndexModel> {
                    override fun onResponse(
                        call: Call<ReportIndexModel>,
                        response: Response<ReportIndexModel>
                    ) {
                        if (response.isSuccessful) {
                            Log.e("report", "success: " + response.body())
                            _reportLivedata.postValue(ScreenState.Success(response.body()))

                        } else {
                            Log.e("report", "not success: " + response.code().toString())
                            _reportLivedata.postValue(
                                ScreenState.Error(
                                    response.code().toString(), null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<ReportIndexModel>, t: Throwable) {
                        Log.e("report", "failure: " + t.message.toString())
                        _reportLivedata.postValue(ScreenState.Error(t.message.toString(), null))
                    }

                })
        }
    }
}