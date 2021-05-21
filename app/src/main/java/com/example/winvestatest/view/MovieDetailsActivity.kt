package com.example.winvestatest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.winvestatest.core.utility.AppConfiguration
import com.example.winvestatest.core.utility.Utils
import com.example.winvestatest.models.movieDetails.Genre
import com.example.winvestatest.models.movieDetails.MovieDetails
import com.example.winvestatest.R
import com.example.winvestatest.databinding.ActivityMovieDetailsBinding
import com.example.winvestatest.viewmodels.MovieDetailsViewmodel
import com.google.android.material.snackbar.Snackbar
import java.lang.StringBuilder

class MovieDetailsActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMovieDetailsBinding
    private lateinit var mViewmodel: MovieDetailsViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewmodel = ViewModelProvider(this).get(MovieDetailsViewmodel::class.java)

        //getting movieid from previous activity and fetching details
        val movieId = intent.getIntExtra("MovieId", 399566)

        attachObserver()
        //checking network availability
        if (Utils.isNetworkAvailable(this)) {

            Glide
                .with(this)
                .load(R.drawable.hour_glass)
                .into(mBinding.imageView8)
            mBinding.imageView8.visibility = View.VISIBLE

            mViewmodel.fetchMovieDetails(movieId)

        } else {
            showError(resources.getString(R.string.no_internet))
        }
        //closing activity
        mBinding.imageView6.setOnClickListener {
            this.finish()
        }


    }

    private fun attachObserver() {
        mViewmodel.getMovieDetails().observe(this, Observer {
            populateMovieDetails(it)
            mBinding.imageView8.visibility = View.GONE
            mBinding.mainContainer.visibility = View.VISIBLE

        })

        mViewmodel.getApiError().observe(this, Observer {
            showError(it)
        })
    }

    /**
     * Populating ui from movie details object
     *
     * @param movieDetails
     */
    private fun populateMovieDetails(movieDetails: MovieDetails) {
        //setting data to views
        Log.d("MovieDetails", movieDetails.title)
        Glide
            .with(this)
            .load(AppConfiguration.IMAGE_BACKGROUND_URL + movieDetails.backdrop_path)
            .into(mBinding.imageView2)

        Glide
            .with(this)
            .load(AppConfiguration.IMAGE_BASE_URL + movieDetails.poster_path)
            .into(mBinding.imageView3)

        mBinding.textView.text = movieDetails.title
        mBinding.textView3.text = movieDetails.vote_average.toString()
        mBinding.textView4.text = movieDetails.release_date
        mBinding.textView6.text = movieDetails.overview

        mBinding.textView2.text = getGenersValue(movieDetails.genres)
    }


    /**
     * generating generes value from list f genres.
     *
     * @param genres
     * @return
     */
    private fun getGenersValue(genres: List<Genre>): String {
        var name = StringBuilder()
        for (element in genres) {
            name.append(element.name + " ")
        }
        return name.toString()
    }


    private fun showError(errorMessage: String) {
        Snackbar.make(mBinding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }
}