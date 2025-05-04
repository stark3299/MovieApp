package com.example.mymovieapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingMoviesModel(
    val page: Int? = null,

    @SerialName("total_pages")
    val totalPages: Int? = null,

    @SerialName("total_results")
    val totalResults: Int? = null,

    val results: List<TrendingMovie> = emptyList()
)

@Serializable
data class TrendingMovie(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,

    val id: Int,
    val title: String,

    @SerialName("original_title")
    val originalTitle: String,
    val overview: String,

    @SerialName("poster_path")
    val posterPath: String? = null,

    @SerialName("media_type")
    val mediaType: String,
    val adult: Boolean,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    val popularity: Double,

    @SerialName("release_date")
    val releaseDate: String,
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int
)
