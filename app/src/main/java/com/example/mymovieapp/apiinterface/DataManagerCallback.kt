package com.example.mymovieapp.apiinterface

import com.google.gson.JsonObject

interface DataManagerCallback {
    fun onSuccess(commandId: Int, result: JsonObject?)
    fun onFailure(commandId: Int, error: String)
}
