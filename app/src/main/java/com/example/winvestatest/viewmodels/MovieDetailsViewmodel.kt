package com.example.winvestatest.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.winvestatest.core.utility.AppConfiguration
import com.example.winvestatest.core.utility.Utils
import com.example.winvestatest.db.AppDatabase
import com.example.winvestatest.models.movieDetails.MovieDetails
import com.example.winvestatest.network.ApiService
import com.example.winvestatest.network.RetroClient
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MovieDetailsViewmodel(private val context: Application) : AndroidViewModel(context) {
    private val movieDetails = MutableLiveData<MovieDetails>()
    private val apiError = MutableLiveData<String>()

    fun fetchMovieDetails(movieId: Int) {

        if (Utils.isNetworkAvailable(context)) {
            val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)
            viewModelScope.launch {
                try {
                    val api = retroClient.getMovieDetails(movieId, AppConfiguration.mApiKey)
                    if (api.isSuccessful) {
                        movieDetails.value = api.body()
                    } else {
                        apiError.value = api.message()
                    }
                } catch (e: Exception) {
                    apiError.value = e.localizedMessage.toString()
                }
            }
        } else {
            try {
                val executor: Executor = Executors.newSingleThreadExecutor()
                executor.execute {
                    val db = AppDatabase.getAppDataBase(context)
                    val result = db!!.getMovieDao().getMovieDetails(movieId)

                    val finalResult = MovieDetails(
                        result.backdrop_path!!,
                        result.poster_path!!,
                        result.vote_average!!,
                        result.release_date!!,
                        result.overview!!,
                        result.title!!,
                        false, 0, emptyList(), "", result.id!!,
                        "", "", ""
                    )

                    movieDetails.postValue(finalResult)


                }
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }

    fun getMovieDetails(): LiveData<MovieDetails> {
        return movieDetails
    }

    fun getApiError(): LiveData<String> {
        return apiError
    }
}