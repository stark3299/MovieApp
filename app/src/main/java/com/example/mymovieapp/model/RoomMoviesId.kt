package com.example.mymovieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_movies_id")
data class RoomMoviesId (
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String
)
