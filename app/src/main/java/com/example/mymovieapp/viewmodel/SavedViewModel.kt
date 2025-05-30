package com.example.mymovieapp.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.model.SavedMovieItem
import com.example.mymovieapp.model.SavedMoviesModel
import com.example.mymovieapp.repository.SavedRepository
import kotlinx.coroutines.launch

class SavedViewModel(private var context: Context) : ViewModel() {
    val repository : SavedRepository = SavedRepository(context)
    val watchlistMoviesDetails : MutableLiveData<List<SavedMovieItem>> = MutableLiveData()

    class Factory(private var context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SavedViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SavedViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    init {
        viewModelScope.launch {
            repository.watchlistMoviesFlow.collect { watchListMovies ->
                processWatchlistMoviesData(watchListMovies)
            }
        }
    }

    private fun processWatchlistMoviesData(movieDetails: SavedMoviesModel){
        watchlistMoviesDetails.value = movieDetails.results
    }

    internal fun getWatchlistMovies(){
        repository.watchlistMoviesAPI(makeWatchListMoviesBundle())
    }

    private fun makeWatchListMoviesBundle() : Bundle{
        var bundle = Bundle()
        bundle.putString("language","en-US")
        bundle.putInt("page",1)
        bundle.putString("sort_by","created_at.asc")
        return bundle
    }

    fun loadSavedMovieFromCache() {
        viewModelScope.launch {
            repository.loadSavedMoviesFromCache()
        }
    }
}