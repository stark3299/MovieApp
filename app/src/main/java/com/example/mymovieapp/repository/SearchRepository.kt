package com.example.mymovieapp.repository

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.mymovieapp.apiinterface.DataManagerCallback
import com.example.mymovieapp.db.MovieDatabase
import com.example.mymovieapp.model.DataManager
import com.example.mymovieapp.model.NetworkModel
import com.example.mymovieapp.model.RecommendedMoviesCache
import com.example.mymovieapp.model.RecommendedMoviesModel
import com.example.mymovieapp.model.SearchMoviesModel
import com.example.mymovieapp.utils.GlobalConstant
import com.example.mymovieapp.utils.SharedFunction
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class SearchRepository(context: Context) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var searchMoviesModel : SearchMoviesModel? = null
    var recommendedMoviesModel : RecommendedMoviesModel? = null
    private val _searchMoviesFlow = MutableSharedFlow<SearchMoviesModel>(replay = 1)
    val searchMoviesFlow: SharedFlow<SearchMoviesModel> = _searchMoviesFlow
    private val _recommendedMoviesFlow = MutableSharedFlow<RecommendedMoviesModel>(replay = 1)
    val recommendedMoviesFlow: SharedFlow<RecommendedMoviesModel> = _recommendedMoviesFlow
    private var lastFetchedTime: Long = 0L
    private val movieDao = MovieDatabase.getDatabase(context).movieDao()


    fun searchMoviesAPI(bundle: Bundle) {
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.search_movies_command_id)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(GlobalConstant.search_movies_api_endpoint)
            .setParamsMap(SharedFunction.getInstance().bundleToParamMaps(bundle))
            .setMethodType("GET")
            .build()

        DataManager.requestAPI(networkModel, object : DataManagerCallback {
            override fun onSuccess(commandId: Int, response: JsonObject?) {
                try {
                    if (response != null) {
                        val json = Json { ignoreUnknownKeys = true }
                        val jsonString = response.toString()
                        Log.d("TrendingRepo", "Received data: $jsonString")
                        val parsedData = json.decodeFromString<SearchMoviesModel>(jsonString)
                        // Cache and emit
                        searchMoviesModel = parsedData
                        lastFetchedTime = System.currentTimeMillis()

                        repositoryScope.launch {
                            _searchMoviesFlow.emit(parsedData)
                        }
                    } else {
                        Log.e("TrendingRepo", "Response is null")
                    }
                } catch (e: Exception) {
                    Log.e("TrendingRepo", "Parsing failed: ${e.message}")
                }
            }

            override fun onFailure(commandId: Int, error: String) {
                Log.e("RepoError", error)
            }
        })
    }

    fun recommendedMoviesAPI(movieId: String, bundle: Bundle) {
        val endpointRecommended = makeRecommendedEndpoint(movieId)
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.recommended_movies_command_id)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(endpointRecommended)
            .setParamsMap(SharedFunction.getInstance().bundleToParamMaps(bundle))
            .setMethodType("GET")
            .build()

        DataManager.requestAPI(networkModel, object : DataManagerCallback {
            override fun onSuccess(commandId: Int, response: JsonObject?) {
                try {
                    if (response != null) {
                        val json = Json { ignoreUnknownKeys = true }
                        val jsonString = response.toString()
                        Log.d("NowShowingRepo", "Received data: $jsonString")
                        val parsedData = json.decodeFromString<RecommendedMoviesModel>(jsonString)
                        recommendedMoviesModel = parsedData
                        lastFetchedTime = System.currentTimeMillis()

                        repositoryScope.launch {
                                val cache = RecommendedMoviesCache(json = jsonString)
                                movieDao.insertRecommendedMoviesCache(cache)
                            _recommendedMoviesFlow.emit(parsedData)
                        }
                    } else {
                        Log.e("NowShowing", "Response is null")
                    }
                } catch (e: Exception) {
                    Log.e("NowShowing", "Parsing failed: ${e.message}")
                }
            }

            override fun onFailure(commandId: Int, error: String) {
                Log.e("RepoError", error)
            }
        })
    }

    private fun makeRecommendedEndpoint(movieId: String): String {
        return GlobalConstant.recommended_movies_api_endpoint.replace("movie_id", movieId)
    }

    suspend fun loadRecommendedMoviesFromCache() {
        val cache = movieDao.getRecommendedMoviesCache()
        cache?.let {
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<RecommendedMoviesModel>(it.json)
            recommendedMoviesModel = parsed
            _recommendedMoviesFlow.emit(parsed)
        }
    }
}