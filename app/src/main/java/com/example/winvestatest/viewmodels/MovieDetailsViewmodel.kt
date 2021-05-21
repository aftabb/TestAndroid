package com.example.winvestatest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtest.core.AppConfiguration
import com.example.androidtest.models.movieDetails.MovieDetails
import com.example.winvestatest.network.ApiService
import com.example.winvestatest.network.RetroClient
import kotlinx.coroutines.launch

class MovieDetailsViewmodel : ViewModel() {
    private val movieDetails = MutableLiveData<MovieDetails>()
    private val apiError = MutableLiveData<String>()


    /*   fun fetchMovieDetails(movieId: Int) {
       val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)

       retroClient.getMovieDetails(movieId, AppConfiguration.mApiKey)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribeWith(object : DisposableSingleObserver<MovieDetails>() {
               override fun onSuccess(t: MovieDetails?) {
                   movieDetails.value = t
               }

               override fun onError(e: Throwable?) {
                   Log.e(e.toString(), "InError")
                   apiError.value = e.toString()
               }

           })
   }
   */

    fun fetchMovieDetails(movieId: Int) {

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
    }

    fun getMovieDetails(): LiveData<MovieDetails> {
        return movieDetails
    }

    fun getApiError(): LiveData<String> {
        return apiError
    }
}