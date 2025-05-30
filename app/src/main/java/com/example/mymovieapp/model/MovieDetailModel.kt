package com.example.mymovieapp.model

import com.google.gson.annotations.SerializedName

data class MovieDetailModel (
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollection? = null,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("homepage") val homepage: String? = null,
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("origin_country") val originCountry: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Long,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)

data class BelongsToCollection(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null
)

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class ProductionCompany(
    @SerializedName("id") val id: Int,
    @SerializedName("logo_path") val logoPath: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val originCountry: String
)

data class ProductionCountry(
    @SerializedName("iso_3166_1") val iso3166_1: String,
    @SerializedName("name") val name: String
)

data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String,
    @SerializedName("iso_639_1") val iso639_1: String,
    @SerializedName("name") val name: String
)
