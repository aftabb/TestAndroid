package com.example.winvestatest.repositories

import com.example.winvestatest.core.utility.AppConfiguration
import com.example.winvestatest.models.Movies
import com.example.winvestatest.network.ApiService
import com.example.winvestatest.network.RetroClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository {
    suspend fun fetchMovies(): Flow<Movies> {
        return flow {
            val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)
            val api = retroClient.searchMovies("avengers", AppConfiguration.mApiKey)
            if (api.isSuccessful) {
                emit(api.body()!!)
            }

        }
    }
}