package com.example.daftarmovie.data.remote

import com.example.daftarmovie.model.MovieResponse
import com.example.daftarmovie.model.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiEndpoint {
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key")apiKey: String
    ) : Call<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key")apiKey: String
    ) : Call<MovieResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailersByMovieId(
        @Path("movie_id")movieId: Int,
        @Query("api_key")apiKey: String
    ): Call<TrailerResponse>
}