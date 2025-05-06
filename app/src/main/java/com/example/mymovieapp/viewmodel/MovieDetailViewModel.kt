package com.example.mymovieapp.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.model.MovieDetailModel
import com.example.mymovieapp.repository.MovieDetailsRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class MovieDetailViewModel: ViewModel() {
    val repository : MovieDetailsRepository = MovieDetailsRepository()
    val movieAllDetails : MutableLiveData<MovieDetailModel> = MutableLiveData()
    val watchListAddedStatus : MutableLiveData<Boolean> = MutableLiveData()

    class Factory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    init {
        viewModelScope.launch {
            repository.movieDetailsFlow.collect { movieDetails ->
                processDetailMoviesData(movieDetails)
            }
        }
        viewModelScope.launch {
            repository.watchlistStatusFlow.collect { watchListStatus ->
                watchlistStatus(watchListStatus)
            }
        }
    }

    private fun processDetailMoviesData(movieDetails: MovieDetailModel){
        movieAllDetails.value = movieDetails
    }

    private fun watchlistStatus(status: Boolean){
        watchListAddedStatus.value = status
    }

    internal fun getMovieDetails(movieId: String){
        repository.movieDetailsAPI(movieId, makeMovieDetails())
    }

    internal fun addMovieToWatchlist(movieId: String){
        repository.addMovieToWatchlist(makeWatchlistJSONBody(movieId))
    }

    private fun makeWatchlistJSONBody(movieId: String): JsonObject{
    val id = movieId.toIntOrNull() ?: -1
        val jsonBody = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", id)
            addProperty("watchlist", true)
        }
        return jsonBody
    }

    private fun makeMovieDetails() : Bundle{
        var bundle = Bundle()
        bundle.putString("language","en-US")
        return bundle
    }
}