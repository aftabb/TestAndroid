package com.example.winvestatest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.winvestatest.models.Result

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Result>)

    @Query("Select * from result LIMIT 40")
    fun getMovies(): List<Result>

    @Query("Select * from result where id=:movieId")
    fun getMovieDetails(movieId: Int): Result
}