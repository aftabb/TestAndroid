package com.example.winvestatest.network

import com.example.androidtest.models.Movies
import com.example.androidtest.models.movieDetails.MovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getAllMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<Movies>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieDetails>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Response<Movies>


}