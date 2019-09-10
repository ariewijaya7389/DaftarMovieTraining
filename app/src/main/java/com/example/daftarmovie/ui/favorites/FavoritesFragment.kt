package com.example.daftarmovie.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.daftarmovie.R
import com.example.daftarmovie.data.local.MovieDao
import com.example.daftarmovie.data.local.MovieDatabase
import com.example.daftarmovie.data.remote.MovieService
import com.example.daftarmovie.model.MovieResponse
import com.example.daftarmovie.model.MovieTM
import com.example.daftarmovie.ui.adapter.MovieRvAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FavoritesFragment : Fragment() {

    private lateinit var movieRvAdapter: MovieRvAdapter
    private lateinit var movieList: MutableList<MovieTM>
    private lateinit var movieDao: MovieDao
    private lateinit var movieDatabase: MovieDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    private fun initView() {
        movieList = mutableListOf()
        movieRvAdapter = MovieRvAdapter(movieList)
        rvFavorite.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            setHasFixedSize(true)
            adapter = movieRvAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
        getFavoriteMovie()
    }

    private fun getFavoriteMovie(){
        movieDatabase = MovieDatabase.getInstance(requireActivity())
        movieDao= movieDatabase.movieDao()
        val response = movieDao.getMovies()

        if (response.size > 0){
            response?.map { movie ->
                movieList.add(movie)
                movieRvAdapter.notifyDataSetChanged()
                Log.d("movies", movie.title)
        }
    }

//    private fun fetchFavoriteMovies() {
//        LoadingBarFavorites.visibility = View.VISIBLE
//
//        val service = MovieService.makeService()
//        service.getPopularMovies("678ef42a1b584848591cbd02ac3899c3").enqueue(object :
//            Callback<MovieResponse> {
//            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
//                Log.e("Error Message", t.localizedMessage)
//                LoadingBarFavorites.visibility = View.GONE
//
//            }
//
//            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
//                if (response.isSuccessful) {
//                    val movieResponse = response.body()
//                    movieResponse?.results?.map { movie ->
//                        movieList.add(movie)
//                        movieRvAdapter.notifyDataSetChanged()
//                        Log.d("movies", movie.title)
//                    }
//                } else {
//                    Log.e("movies", "get response failed")
//                }
//
//                LoadingBarFavorites.visibility = View.GONE
//
//            }
//
//        })
    }
}