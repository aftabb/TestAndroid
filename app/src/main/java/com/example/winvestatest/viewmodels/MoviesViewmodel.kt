package com.example.winvestatest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winvestatest.core.utility.AppConfiguration
import com.example.androidtest.models.Movies
import com.example.winvestatest.network.ApiService
import com.example.winvestatest.network.RetroClient

import kotlinx.coroutines.launch

class MoviesViewmodel : ViewModel() {

    init {
        //calling default
        fetchMovies(1)
    }

    private val movies = MutableLiveData<Movies>()
    private val error = MutableLiveData<String>()

    fun fetchMovies(page: Int) {
        //fetching data from network using rx java
        val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val api = retroClient.getAllMovies(AppConfiguration.mApiKey, page)
                if (api.isSuccessful) {
                    movies.value = api.body()
                } else {
                    //Handling error
                    error.value = api.message()
                }
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }

        }
    }

    fun getMovies(): LiveData<Movies> {
        return movies
    }

    fun showError(): LiveData<String> {
        return error
    }
}