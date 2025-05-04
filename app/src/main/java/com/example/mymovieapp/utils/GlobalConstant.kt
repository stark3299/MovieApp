package com.example.mymovieapp.utils

object GlobalConstant{
    const val API_KEY = "95efdb255cbfcae0587ba529d88613ef"
    const val trending_movie_api_endpoint = "trending/movie/week"
    const val now_showing_movie_api_endpoint = "movie/now_playing"
    const val search_movies_api_endpoint = "search/movie"
    const val recommended_movies_api_endpoint = "movie/movie_id/recommendations"
    const val details_movie_api_endpoint = "movie/"
    const val base_url = "https://api.themoviedb.org/3/"
    const val API_Read_Access_Token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NWVmZGIyNTVjYmZjYWUwNTg3YmE1MjlkODg2MT" +
            "NlZiIsIm5iZiI6MS43NDYwNzc3NjY2MzM5OTk4ZSs5LCJzdWIiOiI2ODEzMDg0NmFjZmRjZWVkODI0NzQ1NGYiLCJzY29wZXMi" +
            "OlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.3j9B8hoY4I44b4XKXtfJFUAdfd3rcZoyUNldU0wuV14"


    //Command ID
    const val trending_movies_command_id = 101
    const val now_showing_movies_command_id = 102
    const val search_movies_command_id = 103
    const val recommended_movies_command_id = 104
    const val details_movie_command_id = 105


    //String Constants
    const val home_fragment = "homeFragment"
    const val search_fragment = "searchFragment"
}