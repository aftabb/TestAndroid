package com.example.winvestatest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtest.models.Movies
import com.example.winvestatest.models.adapters.MoviesAdapter
import com.example.winvestatest.models.interfaces.MovieItemClick
import com.example.winvestatest.viewmodels.MoviesViewmodel
import com.example.winvestatest.core.uicomponent.InfiniteScrollingListner
import com.example.winvestatest.databinding.ActivityMainBinding
import com.example.winvestatest.view.MovieDetailsActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), MovieItemClick {

    private lateinit var mMovieViewmodel: MoviesViewmodel
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MoviesAdapter
    private lateinit var endlessScroller: InfiniteScrollingListner
    private lateinit var gridLayoutManager: GridLayoutManager

    private var mTotalPages = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //Initializing Views and Components.
        setSupportActionBar(mBinding.toolbar)
        mMovieViewmodel = ViewModelProvider(this).get(MoviesViewmodel::class.java)
        gridLayoutManager = GridLayoutManager(this, 3)
        mBinding.moviesList.layoutManager = gridLayoutManager

        //Initializing endless scroll for movie list
        endlessScroller = object : InfiniteScrollingListner(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (page + 1 <= mTotalPages)
                    loadMoreMovies(page + 1)
                Log.d("On Scroll Change", "Load More")
            }

        }
        mBinding.moviesList.addOnScrollListener(endlessScroller)

        //Initializing movie adapter
        mAdapter = MoviesAdapter(this, ArrayList(), this)
        mBinding.moviesList.adapter = mAdapter
        mBinding.moviesList.setHasFixedSize(true)

        //Attaching  all observer
        attachObserver()

    }

    private val moviesObserver = Observer<Movies> { movies ->
        mTotalPages = movies.total_pages
        //Adding data in movie list adapter
        mAdapter.addItems(movies.results)
    }

    private fun attachObserver() {
        //movies list observer.
        mMovieViewmodel.getMovies().observe(this, moviesObserver)
    }

    private fun loadMoreMovies(page: Int) {
        //loading more movie on scroll based on search/normal
        mMovieViewmodel.fetchMovies(page + 1)
    }

    override fun onItemClick(position: Int, movieId: Int) {
        //onClick of movie list item opening details activity
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("MovieId", movieId)
        startActivity(intent)
    }

    private fun showError(errorMessage: String) {
        Snackbar.make(mBinding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }

}
