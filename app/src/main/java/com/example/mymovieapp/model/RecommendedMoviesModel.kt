package com.example.mymovieapp.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RecommendedMoviesModel(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<RecommendedMovie>
)

@Serializable
data class RecommendedMovie(
    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String?,

    @SerialName("original_title")
    val originalTitle: String?,

    @SerialName("overview")
    val overview: String?,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("media_type")
    val mediaType: String?,

    @SerialName("adult")
    val adult: Boolean,

    @SerialName("original_language")
    val originalLanguage: String?,

    @SerialName("genre_ids")
    val genreIds: List<Int>,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("video")
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int
)

