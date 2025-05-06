package com.example.mymovieapp.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.model.RecommendedMovie
import com.example.mymovieapp.model.RecommendedMoviesModel
import com.example.mymovieapp.model.SearchItem
import com.example.mymovieapp.model.SearchMoviesModel
import com.example.mymovieapp.repository.SearchRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchViewModel(private val context: Context) : ViewModel() {

    val repository : SearchRepository = SearchRepository(context)
    val searchMoviesList : MutableLiveData<List<SearchItem>> = MutableLiveData()
    val recommendedMoviesList : MutableLiveData<List<RecommendedMovie>> = MutableLiveData()
    private val _searchQuery = MutableStateFlow("")

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    init {
        viewModelScope.launch {
            repository.searchMoviesFlow.collect { searchMovies ->
                processSearchMoviesData(searchMovies)
            }
        }

        viewModelScope.launch {
            repository.recommendedMoviesFlow.collect { recommendedMovies ->
                processRecommendedMoviesData(recommendedMovies)
            }
        }

        observeSearchQuery()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // 500ms wait time
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    repository.searchMoviesAPI(makeSearchBundle(query)) // Call API with query
                }
        }
    }

    private fun processSearchMoviesData(searchMovies: SearchMoviesModel) {
        searchMoviesList.value = searchMovies.results
    }

    private fun processRecommendedMoviesData(recommendedMovies: RecommendedMoviesModel){
        recommendedMoviesList.value = recommendedMovies.results
    }

    internal fun getRecommendedMovies(movieId : String){
        repository.recommendedMoviesAPI(movieId, makeRecommendedBundle())
    }

    fun loadRecommendedFromCache() {
        viewModelScope.launch {
            repository.loadRecommendedMoviesFromCache()
        }
    }

    fun makeRecommendedBundle() : Bundle{
        val bundle = Bundle()
        bundle.putString("language", "en-US")
        bundle.putBoolean("include_adult", false)
        return bundle
    }

    fun makeSearchBundle(query: String) : Bundle{
        val bundle = Bundle()
        bundle.putString("query", query)
        bundle.putBoolean("include_adult", false)
        bundle.putString("language", "en-US")
        return bundle
    }
}