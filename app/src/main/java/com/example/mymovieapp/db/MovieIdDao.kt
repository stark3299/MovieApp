package com.example.mymovieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovieapp.model.RoomMoviesId

@Dao
interface MovieIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: RoomMoviesId)

    @Query("SELECT * FROM room_movies_id LIMIT 1")
    suspend fun getFirstMovieId(): RoomMoviesId?
}