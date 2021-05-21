package com.example.winvestatest.models.movieDetails

data class MovieDetails(
    val backdrop_path: String,
    val poster_path: String,
    val vote_average: Double,
    val release_date: String,
    val overview: String,

    val title: String,
    val adult: Boolean,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String
//    val popularity: Double,
//    //val production_companies: List<ProductionCompany>,
//    //val production_countries: List<ProductionCountry>,
//    //val revenue: Int,
//    val runtime: Int,
//    //val spoken_languages: List<SpokenLanguage>,
//    val status: String,
//    val tagline: String,
//    val video: Boolean,
//    val vote_count: Int
)