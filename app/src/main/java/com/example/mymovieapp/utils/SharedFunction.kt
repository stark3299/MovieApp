package com.example.mymovieapp.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.navigation.NavController
import com.example.mymovieapp.R
import com.example.mymovieapp.db.MovieDatabase
import kotlinx.coroutines.runBlocking

class SharedFunction private constructor() {

    companion object {
        @Volatile
        private var instance: SharedFunction? = null

        fun getInstance(): SharedFunction {
            return instance ?: synchronized(this) {
                instance ?: SharedFunction().also { instance = it }
            }
        }
    }

    fun getSavedMovieId(context: Context?): Int? {
        if(context == null)
            return 0
        val db = MovieDatabase.getDatabase(context)
        val movieDao = db.movieDao()

        return try {
            val movieEntity = runBlocking {
                movieDao.getFirstMovieId()
            }
            movieEntity?.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun checkNullOrNot(string: String?): Boolean {
        return !(string.isNullOrEmpty() || string.equals("null", ignoreCase = true))
    }

    fun bundleToParamMaps(bundle: Bundle) : HashMap<String, String>{
        val bundleParams = convertBundleToParamsMap(bundle)
        bundleParams.forEach { (key, value) ->
            bundleParams.put(key, value)
        }
        return bundleParams
    }

    fun openMovieDetailsPage(movieId: String, navController: NavController){
        val bundle = Bundle().apply {
            putString("movieId", movieId)
        }
        navController.navigate(R.id.movieDetailFragment, bundle)
    }

    private fun convertBundleToParamsMap(bundle: Bundle): HashMap<String, String> {
        val paramsMap = HashMap<String, String>()
        for (key in bundle.keySet()) {
            val value = bundle[key]?.toString() ?: ""
            paramsMap[key] = value
        }
        return paramsMap
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}
