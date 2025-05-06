package com.example.mymovieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovieapp.model.NowShowingMoviesCache
import com.example.mymovieapp.model.RecommendedMoviesCache
import com.example.mymovieapp.model.RoomMoviesId
import com.example.mymovieapp.model.SavedMovieCache
import com.example.mymovieapp.model.TrendingMoviesCache

@Dao
interface MovieIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: RoomMoviesId)

    @Query("SELECT * FROM room_movies_id LIMIT 1")
    suspend fun getFirstMovieId(): RoomMoviesId?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrendingMoviesCache(cache: TrendingMoviesCache)

    @Query("SELECT * FROM trending_movies_cache WHERE id = 1")
    suspend fun getTrendingMoviesCache(): TrendingMoviesCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNowShowingMoviesCache(cache: NowShowingMoviesCache)

    @Query("SELECT * FROM now_showing_movies_cache WHERE id = 1")
    suspend fun getNowShowingMoviesCache(): NowShowingMoviesCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendedMoviesCache(cache: RecommendedMoviesCache)

    @Query("SELECT * FROM recommended_movies_cache LIMIT 1")
    suspend fun getRecommendedMoviesCache(): RecommendedMoviesCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedMovieCache(savedMovieCache: SavedMovieCache)

    @Query("SELECT * FROM saved_movie_cache LIMIT 1")
    suspend fun getSavedMovieCache(): SavedMovieCache?
}