package com.example.mymovieapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymovieapp.model.NowShowingMoviesCache
import com.example.mymovieapp.model.RecommendedMoviesCache
import com.example.mymovieapp.model.RoomMoviesId
import com.example.mymovieapp.model.SavedMovieCache
import com.example.mymovieapp.model.TrendingMoviesCache

@Database(entities = [RoomMoviesId::class, SavedMovieCache::class, RecommendedMoviesCache::class, TrendingMoviesCache::class, NowShowingMoviesCache::class], version = 3)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieIdDao

    companion object {
        @Volatile private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}