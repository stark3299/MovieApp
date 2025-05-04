package com.example.mymovieapp.apiinterface

import android.util.Log
import com.example.mymovieapp.model.NetworkModel
import com.example.mymovieapp.utils.GlobalConstant
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(GlobalConstant.base_url) // Base URL, overridden dynamically per request
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun makeRequest(model: NetworkModel, callback: DataManagerCallback) {
        Log.d("NetworkCall", "API: ${model.api}")
        Log.d("NetworkCall", "Method: ${model.methodType}")
        Log.d("NetworkCall", "CommandId: ${model.commandId}")

        val call: Call<JsonObject> = when (model.methodType?.uppercase()) {
            "POST" -> {
                model.jsnParam?.let {
                    apiService.postRequest(model.api ?: "", it)
                } ?: run {
                    Log.e("NetworkCall", "POST body is null")
                    return
                }
            }
            "GET" -> apiService.getRequest(model.api ?: "")
            else -> throw IllegalArgumentException("Unsupported method type: ${model.methodType}")
        }

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("NetworkCall", "Response code: ${response.code()}")
                Log.d("NetworkCall", "Response body: ${response.body()}")
                Log.d("NetworkCall", "Error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    callback.onSuccess(model.commandId, response.body())
                } else {
                    callback.onFailure(
                        model.commandId,
                        "API Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("NetworkCall", "Request failed: ${t.localizedMessage}")
                callback.onFailure(model.commandId, t.message ?: "Unknown error")
            }
        })
    }
}
