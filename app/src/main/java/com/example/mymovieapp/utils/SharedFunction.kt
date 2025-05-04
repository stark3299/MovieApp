package com.example.mymovieapp.utils

import android.content.Context
import android.os.Bundle
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

    // Other functions you want to use with getInstance
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

    private fun convertBundleToParamsMap(bundle: Bundle): HashMap<String, String> {
        val paramsMap = HashMap<String, String>()
        for (key in bundle.keySet()) {
            val value = bundle[key]?.toString() ?: ""
            paramsMap[key] = value
        }
        return paramsMap
    }
}
