package com.example.daftarmovie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.daftarmovie.model.MovieTM

@Dao
interface MovieDao {
    @Insert
    fun insertMovie(movie: MovieTM)

    @Query("SELECT * FROM movies")
    fun getMovies() :  List<MovieTM>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun findMovieById(id: Int)

    @Query("DELETE FROM movies WHERE id = :id")
    fun deleteMovieById(id: Int)
}