package com.example.daftarmovie.ui.tayang

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daftarmovie.R
import com.example.daftarmovie.data.remote.MovieService
import com.example.daftarmovie.model.MovieTM
import com.example.daftarmovie.model.MovieResponse
import com.example.daftarmovie.ui.adapter.TayangRvAdapter
import kotlinx.android.synthetic.main.fragment_tayang.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TayangMovieFragment : Fragment() {

    private lateinit var tayangRvAdapter: TayangRvAdapter
    private lateinit var tayangList: MutableList<MovieTM>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_tayang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        fetchTayangMovies()
    }

    private fun initView(){
        tayangList= mutableListOf()
        tayangRvAdapter= TayangRvAdapter(tayangList)
        rvTayang.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvTayang.setHasFixedSize(true)
        rvTayang.adapter = tayangRvAdapter
    }
    private fun fetchTayangMovies(){
        LoadingBarTayang.visibility = View.VISIBLE
        val service = MovieService.makeService()

        service.getNowPlayingMovies("678ef42a1b584848591cbd02ac3899c3").enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e( "Error Message", t.localizedMessage)
                LoadingBarTayang.visibility = View.GONE

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful){
                    val movieResponse = response.body()
                    movieResponse?.results?.map { movie ->
                        tayangList.add(movie)
                        tayangRvAdapter.notifyDataSetChanged()
                        Log.d("movies",movie.title)
                        Log.d("movies",movie.posterPath)
                    }
                } else{
                    Log.e("movies","get response failed")
                }
                LoadingBarTayang.visibility = View.GONE

            }

        })
    }



}