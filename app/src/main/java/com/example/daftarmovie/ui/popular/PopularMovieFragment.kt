package com.example.daftarmovie.ui.popular

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.daftarmovie.R
import com.example.daftarmovie.data.remote.MovieService.makeService
import com.example.daftarmovie.model.MovieTM
import com.example.daftarmovie.model.MovieResponse
import com.example.daftarmovie.ui.adapter.MovieRvAdapter
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularMovieFragment : Fragment() {

    private lateinit var movieRvAdapter: MovieRvAdapter
    private lateinit var movieList: MutableList<MovieTM>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_popular_movies, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        fetchPopularMovies()
    }

    private fun initView(){
        movieList = mutableListOf()
        movieRvAdapter = MovieRvAdapter(movieList)
        rvMovies.apply {
            layoutManager = GridLayoutManager(requireActivity(),2)
            setHasFixedSize(true)
            adapter = movieRvAdapter
        }
    }

    private fun fetchPopularMovies(){
        LoadingBarPopular.visibility = View.VISIBLE

        val service = makeService()
        service.getPopularMovies("678ef42a1b584848591cbd02ac3899c3").enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e( "Error Message", t.localizedMessage)
                LoadingBarPopular.visibility = View.GONE

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful){
                    val movieResponse = response.body()
                    movieResponse?.results?.map { movie ->
                        movieList.add(movie)
                        movieRvAdapter.notifyDataSetChanged()
                        Log.d("movies",movie.title)
                    }
                } else{
                    Log.e("movies","get response failed")
                }
                LoadingBarPopular.visibility = View.GONE

            }

        })
    }



}

