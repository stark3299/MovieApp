package com.example.mymovieapp.apiinterface

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    fun postRequest(@Url url: String, @Body body: JsonObject): Call<JsonObject>

    @GET
    fun getRequest(@Url url: String): Call<JsonObject>
}