package com.example.mymovieapp.repository

import android.util.Log
import com.example.mymovieapp.apiinterface.DataManagerCallback
import com.example.mymovieapp.db.MovieIdDao
import com.example.mymovieapp.model.DataManager
import com.example.mymovieapp.model.NetworkModel
import com.example.mymovieapp.model.NowShowingModel
import com.example.mymovieapp.model.NowShowingMoviesCache
import com.example.mymovieapp.model.RoomMoviesId
import com.example.mymovieapp.model.TrendingMoviesCache
import com.example.mymovieapp.model.TrendingMoviesModel
import com.example.mymovieapp.utils.GlobalConstant
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeRepository(private val movieDao: MovieIdDao) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var trendingMoviesModel : TrendingMoviesModel? = null
    var nowShowingModel : NowShowingModel? = null
    private val _trendingMoviesFlow = MutableSharedFlow<TrendingMoviesModel>(replay = 1)
    val trendingMoviesFlow: SharedFlow<TrendingMoviesModel> = _trendingMoviesFlow
    private val _nowShowingMoviesFlow = MutableSharedFlow<NowShowingModel>(replay = 1)
    val nowShowingMoviesFlow: SharedFlow<NowShowingModel> = _nowShowingMoviesFlow
    private var lastFetchedTime: Long = 0L

    fun insertMovie(movie: RoomMoviesId) {
        repositoryScope.launch {
            movieDao.insertMovie(movie)
        }
    }

    fun trendingMoviesAPI() {
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.trending_movies_command_id)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(GlobalConstant.trending_movie_api_endpoint)
            .setMethodType("GET")
            .build()

        DataManager.requestAPI(networkModel, object : DataManagerCallback {
            override fun onSuccess(commandId: Int, response: JsonObject?) {
                try {
                    if (response != null) {
                        val json = Json { ignoreUnknownKeys = true }
                        val jsonString = response.toString()
                        Log.d("TrendingRepo", "Received data: $jsonString")
                        val parsedData = json.decodeFromString<TrendingMoviesModel>(jsonString)
                        // Cache and emit
                        trendingMoviesModel = parsedData
                        lastFetchedTime = System.currentTimeMillis()

                        repositoryScope.launch {
                            movieDao.saveTrendingMoviesCache(
                                TrendingMoviesCache(json = jsonString, timestamp = lastFetchedTime)
                            )
                            _trendingMoviesFlow.emit(parsedData)
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

    fun nowShowingMoviesAPI() {
        val networkModel = NetworkModel.Builder()
            .setCommandId(GlobalConstant.now_showing_movies_command_id)
            .setHostName(GlobalConstant.base_url)
            .setEndPointID1(GlobalConstant.now_showing_movie_api_endpoint)
            .setMethodType("GET")
            .build()

        DataManager.requestAPI(networkModel, object : DataManagerCallback {
            override fun onSuccess(commandId: Int, response: JsonObject?) {
                try {
                    if (response != null) {
                        val json = Json { ignoreUnknownKeys = true }
                        val jsonString = response.toString()
                        Log.d("NowShowingRepo", "Received data: $jsonString")
                        val parsedData = json.decodeFromString<NowShowingModel>(jsonString)
                        nowShowingModel = parsedData
                        lastFetchedTime = System.currentTimeMillis()

                        repositoryScope.launch {
                            movieDao.saveNowShowingMoviesCache(
                                NowShowingMoviesCache(json = jsonString, timestamp = lastFetchedTime)
                            )
                            _nowShowingMoviesFlow.emit(parsedData)
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
    suspend fun loadTrendingMoviesFromCache() {
        val cache = movieDao.getTrendingMoviesCache()
        cache?.let {
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<TrendingMoviesModel>(it.json)
            trendingMoviesModel = parsed
            _trendingMoviesFlow.emit(parsed)
        }
    }

    suspend fun loadNowShowingMoviesFromCache() {
        val cache = movieDao.getNowShowingMoviesCache()
        cache?.let {
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<NowShowingModel>(it.json)
            nowShowingModel = parsed
            _nowShowingMoviesFlow.emit(parsed)
        }
    }
}
