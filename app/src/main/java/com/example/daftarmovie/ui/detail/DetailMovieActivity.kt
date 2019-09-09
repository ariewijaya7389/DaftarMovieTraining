package com.example.daftarmovie.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.daftarmovie.R
import com.example.daftarmovie.data.local.MovieDatabase
import com.example.daftarmovie.data.remote.MovieService.makeService
import com.example.daftarmovie.model.MovieTM
import com.example.daftarmovie.model.Trailer
import com.example.daftarmovie.model.TrailerResponse
import com.example.daftarmovie.ui.adapter.TayangRvAdapter
import com.example.daftarmovie.ui.adapter.TrailerRvAdapter
import kotlinx.android.synthetic.main.activity_detail_movie.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var trailerRvAdapter: TrailerRvAdapter
    private lateinit var trailerList: MutableList<Trailer>
    private lateinit var movieDatabase: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

//        val title = intent.getStringExtra("title")
//        val releaseDate = intent.getStringExtra("releaseDate")
//        val popularity = intent.getDoubleExtra("popularity",0.0)

        val movie = intent.getParcelableExtra<MovieTM>("movieIntent")
        Log.i(
            "Detail Movie",
            "Movie title : ${movie?.title}, ${movie?.releaseDate}, ${movie?.popularity}"
        )

        initView()
        showDetailMovie(movie)
        fetchTrailers(movie.id)

        supportActionBar?.title = movie?.title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_movies, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.favoriteButton -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDetailMovie(movieTM: MovieTM) {
        textTitle.text = movieTM.title
        textReleaseDate.text = movieTM.releaseDate
        textRating.text = "${movieTM.voteAverage} / 10"
        textSummary.text = movieTM.overview
        imageViewDetail.apply {
            Glide
                .with(context).asDrawable()
                .load(TayangRvAdapter.POSTER_URL_DETAIL + movieTM.posterPath)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewDetail)
        }
        imageViewBackdrop.apply {
            Glide
                .with(context).asDrawable()
                .load(TayangRvAdapter.POSTER_URL_DETAIL + movieTM.backdropPath)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewBackdrop)
        }

    }

    private fun initView(){
        movieDatabase = MovieDatabase.getInstance(this)

        trailerList = mutableListOf()
        trailerRvAdapter = TrailerRvAdapter(trailerList)
        rvTrailer.apply {
            layoutManager = LinearLayoutManager(this@DetailMovieActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = trailerRvAdapter
        }
    }


    private fun fetchTrailers(movieId: Int) {

        val service = makeService()
        service.getTrailersByMovieId(movieId, "678ef42a1b584848591cbd02ac3899c3").enqueue(object :
            Callback<TrailerResponse> {
            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                Log.i("fetch-trailers-error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<TrailerResponse>,
                response: Response<TrailerResponse>
            ) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    movieResponse?.results?.map {
                        trailerList.add(it)
                        trailerRvAdapter.notifyDataSetChanged()
                        Log.i("movieId", it.id)
                    }
                } else {
                    Log.e("movies", "get response failed")
                }

            }

        })
    }

}
