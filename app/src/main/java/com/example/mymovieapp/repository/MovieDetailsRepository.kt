package com.example.mymovieapp.repository

import android.os.Bundle
import android.util.Log
import com.example.mymovieapp.apiinterface.DataManagerCallback
import com.example.mymovieapp.model.DataManager
import com.example.mymovieapp.model.MovieDetailModel
import com.example.mymovieapp.model.NetworkModel
import com.example.mymovieapp.utils.GlobalConstant
import com.example.mymovieapp.utils.SharedFunction
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MovieDetailsRepository {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _movieDetailsFlow = MutableSharedFlow<MovieDetailModel>(replay = 1)
    val movieDetailsFlow: SharedFlow<MovieDetailModel> = _movieDetailsFlow
    private val _watchlistStatusFlow = MutableSharedFlow<Boolean>(replay = 1)
    val watchlistStatusFlow: SharedFlow<Boolean> = _watchlistStatusFlow
    private var lastFetchedTime: Long = 0L
    private val cacheExpiryMillis = 5 * 60 * 1000

    fun movieDetailsAPI(movieId: String, bundle: Bundle) {
        val endpointMovieDetails = makeMovieDetailsEndpoint(movieId)
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.details_movie_command_id)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(endpointMovieDetails)
            .setParamsMap(SharedFunction.getInstance().bundleToParamMaps(bundle))
            .setMethodType("GET")
            .build()

        DataManager.requestAPI(networkModel, object : DataManagerCallback {
            override fun onSuccess(commandId: Int, response: JsonObject?) {
                try {
                    if (response != null) {
                        val jsonString = response.toString()
                        Log.d("MovieDetailsRepo", "Received data: $jsonString")
                        val gson = Gson()
                        val parsedData = gson.fromJson(jsonString, MovieDetailModel::class.java)
                        repositoryScope.launch {
                            _movieDetailsFlow.emit(parsedData)
                        }
                        lastFetchedTime = System.currentTimeMillis()
                    } else {
                        Log.e("MovieDetailsRepo", "Response is null")
                    }
                } catch (e: Exception) {
                    Log.e("MovieDetailsRepo", "Parsing failed: ${e.message}")
                }
            }

            override fun onFailure(commandId: Int, error: String) {
                Log.e("MovieDetailsRepo", "API failed: $error")
            }
        })
    }

    fun addMovieToWatchlist(jsonBody: JsonObject) {
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.add_movie_to_watchlist)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(GlobalConstant.add_movie_to_watchlist_endpoint)
            .setJsonParam(jsonBody)
            .setMethodType("POST")
            .build()

        DataManager.requestAPI(networkModel, object : DataManagerCallback {
            override fun onSuccess(commandId: Int, response: JsonObject?) {
                try {
                    val success : Boolean= response?.get("success")?.asBoolean == true
                    Log.d("MovieDetailsRepo", "Watchlist status: $success")
                    repositoryScope.launch {
                        _watchlistStatusFlow.emit(success)
                    }
                } catch (e: Exception) {
                    Log.e("MovieDetailsRepo", "Watchlist parsing error: ${e.message}")
                    repositoryScope.launch {
                        _watchlistStatusFlow.emit(false)
                    }
                }
            }

            override fun onFailure(commandId: Int, error: String) {
                Log.e("MovieDetailsRepo", "API failed: $error")
            }
        })
    }

    private fun makeMovieDetailsEndpoint(movieId: String): String {
        return GlobalConstant.details_movie_api_endpoint.replace("movie_id", movieId)
    }
}
