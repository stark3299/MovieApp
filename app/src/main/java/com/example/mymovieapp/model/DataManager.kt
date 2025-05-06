package com.example.mymovieapp.model

import com.example.mymovieapp.apiinterface.DataManagerCallback
import com.example.mymovieapp.apiinterface.RetrofitManager

object DataManager {
    fun requestAPI(model: NetworkModel, callback: DataManagerCallback) {
        RetrofitManager.makeRequest(model, callback)
    }
}