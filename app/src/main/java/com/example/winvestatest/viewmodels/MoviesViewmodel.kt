package com.example.winvestatest.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.winvestatest.core.utility.AppConfiguration
import com.example.winvestatest.core.utility.Utils
import com.example.winvestatest.db.AppDatabase
import com.example.winvestatest.models.Movies
import com.example.winvestatest.models.Result
import com.example.winvestatest.network.ApiService
import com.example.winvestatest.network.RetroClient

import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MoviesViewmodel(private val context: Application) : AndroidViewModel(context) {

    init {
        //calling default
        fetchMovies(1)
    }

    private val movies = MutableLiveData<Movies>()
    private val error = MutableLiveData<String>()
    private val results = MutableLiveData<List<Result>>()

    fun fetchMovies(page: Int) {
        //fetching from server if network available
        if (Utils.isNetworkAvailable(context)) {
            //fetching data from network
            val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)
            viewModelScope.launch {
                try {
                    val api = retroClient.getAllMovies(AppConfiguration.mApiKey, page)
                    if (api.isSuccessful) {
                        movies.value = api.body()
                        cacheRecords(api.body())
                    } else {
                        //Handling error
                        error.value = api.message()
                    }
                } catch (e: Exception) {
                    error.value = e.localizedMessage
                }

            }
        } else {
            //fetching from database if no network
            try {
                val executor: Executor = Executors.newSingleThreadExecutor()
                executor.execute {
                    val db = AppDatabase.getAppDataBase(context)
                    results.postValue(db?.getMovieDao()?.getMovies())
                }
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }

    /**
     * Saving records in database for offline purpose
     *
     * @param movies
     */
    private fun cacheRecords(movies: Movies?) {
        try {
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val db = AppDatabase.getAppDataBase(context)
                if (movies != null) {
                    db?.getMovieDao()?.insertMovies(movies.results)
                }
            }
        } catch (e: Exception) {
            Log.e("Error", e.localizedMessage)
        }

    }

    fun getMovies(): LiveData<Movies> {
        return movies
    }

    fun getMovieFromDb(): LiveData<List<Result>> {
        return results
    }

    fun showError(): LiveData<String> {
        return error
    }
}