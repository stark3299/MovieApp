package com.example.mymovieapp.repository

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.mymovieapp.apiinterface.DataManagerCallback
import com.example.mymovieapp.db.MovieDatabase
import com.example.mymovieapp.model.DataManager
import com.example.mymovieapp.model.NetworkModel
import com.example.mymovieapp.model.SavedMovieCache
import com.example.mymovieapp.model.SavedMoviesModel
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
import kotlinx.serialization.json.Json

class SavedRepository(context: Context) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _watchlistMoviesFlow = MutableSharedFlow<SavedMoviesModel>(replay = 1)
    val watchlistMoviesFlow: SharedFlow<SavedMoviesModel> = _watchlistMoviesFlow
    private var lastFetchedTime: Long = 0L
    private val savedMovieCacheDao = MovieDatabase.getDatabase(context).movieDao()

    fun watchlistMoviesAPI(bundle: Bundle) {
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.watchlist_movies)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(GlobalConstant.get_movies_to_watchlist_endpoint)
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
                        val parsedData = gson.fromJson(jsonString, SavedMoviesModel::class.java)
                        repositoryScope.launch {
                            savedMovieCacheDao.insertSavedMovieCache(SavedMovieCache(json = jsonString))
                            _watchlistMoviesFlow.emit(parsedData)
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
    suspend fun loadSavedMoviesFromCache() {
        val cache = savedMovieCacheDao.getSavedMovieCache()
        cache?.let {
            val gson = Gson()
            val parsedData = gson.fromJson(it.json, SavedMoviesModel::class.java)
            _watchlistMoviesFlow.emit(parsedData)
        }
    }
}