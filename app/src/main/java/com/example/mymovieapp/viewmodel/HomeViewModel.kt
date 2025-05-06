package com.example.mymovieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.db.MovieIdDao
import com.example.mymovieapp.model.NowShowingModel
import com.example.mymovieapp.model.NowShowingMovie
import com.example.mymovieapp.model.RoomMoviesId
import com.example.mymovieapp.model.TrendingMovie
import com.example.mymovieapp.model.TrendingMoviesModel
import com.example.mymovieapp.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val movieDao: MovieIdDao) : ViewModel() {
    val repository : HomeRepository = HomeRepository(movieDao)
    val trendingMoviesList : MutableLiveData<List<TrendingMovie>> = MutableLiveData()
    val nowShowingMoviesList : MutableLiveData<List<NowShowingMovie>> = MutableLiveData()

    class Factory(private val movieDao: MovieIdDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(movieDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    init {
        viewModelScope.launch {
            repository.trendingMoviesFlow.collect { trendingMovies ->
                processTrendingMoviesData(trendingMovies)
            }
        }

        viewModelScope.launch {
            repository.nowShowingMoviesFlow.collect { nowShowingMovies ->
                processNowShowingMoviesData(nowShowingMovies)
            }
        }
    }

    private fun processTrendingMoviesData(trendingMovie: TrendingMoviesModel){
        trendingMoviesList.value = trendingMovie.results
    }

    private fun processNowShowingMoviesData(nowShowingMovie: NowShowingModel){
        nowShowingMoviesList.value = nowShowingMovie.results
        val firstMovie = nowShowingMovie.results.firstOrNull()
        firstMovie?.let {
            val entity = RoomMoviesId(
                id = it.id,
                title = it.title,
                posterPath = it.posterPath ?: ""
            )
            viewModelScope.launch {
                repository.insertMovie(entity)
            }
        }
    }

    internal fun getTrendingMovies(){
        repository.trendingMoviesAPI()
    }

    internal fun getNowShowingMovies(){
        repository.nowShowingMoviesAPI()
    }

    fun fetchTrendingMoviesWithCacheSupport() {
        repository.fetchFromCacheIfValid()
    }
}