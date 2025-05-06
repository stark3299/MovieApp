package com.example.mymovieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_movies_id")
data class RoomMoviesId (
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String
)

@Entity(tableName = "trending_movies_cache")
data class TrendingMoviesCache(
    @PrimaryKey val id: Int = 1,
    val json: String,
    val timestamp: Long
)

@Entity(tableName = "now_showing_movies_cache")
data class NowShowingMoviesCache(
    @PrimaryKey val id: Int = 1,
    val json: String,
    val timestamp: Long
)

@Entity(tableName = "recommended_movies_cache")
data class RecommendedMoviesCache(
    @PrimaryKey val id: Int = 0,
    val json: String
)

@Entity(tableName = "saved_movie_cache")
data class SavedMovieCache(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val json: String
)
