package com.example.daftarmovie.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.daftarmovie.R
import com.example.daftarmovie.data.local.MovieDao
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
import java.lang.Exception

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var trailerRvAdapter: TrailerRvAdapter
    private lateinit var trailerList: MutableList<Trailer>
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var movie:MovieTM
    private var isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

//        val title = intent.getStringExtra("title")
//        val releaseDate = intent.getStringExtra("releaseDate")
//        val popularity = intent.getDoubleExtra("popularity",0.0)

        movie = intent.getParcelableExtra("movieIntent")

        showDetailMovie(movie)
        isMovieFavorite(movie.id)
        supportActionBar?.title = movie.title
        isFavorite = isMovieFavorite(movie.id)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_movies, menu)
        if (isFavorite) {
            menu?.findItem(R.id.favoriteButton)?.icon = resources
                .getDrawable(R.drawable.ic_favorite_black_24dp)
        } else {
            menu?.findItem(R.id.favoriteButton)?.icon = resources
                .getDrawable(R.drawable.ic_favorite_border_black_24dp)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        val isFavorite =isMovieFavorite(movie.id)
        when(item?.itemId){
            R.id.favoriteButton -> {
                when (isFavorite) {
                    true -> {
                        removeFromFavorite(movie.id)
                        item.setIcon(R.drawable.ic_favorite_border_black_24dp)
                        isFavorite = false
                    }
                    false -> {
                        addToFavorite(movie)
                        item.setIcon(R.drawable.ic_favorite_black_24dp)
                        isFavorite = true
                    }
                }
            }
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
        initView()
        fetchTrailers(movie.id)
    }

    private fun initView(){
        movieDatabase = MovieDatabase.getInstance(this)
        movieDao = movieDatabase.movieDao()

        trailerList = mutableListOf()
        trailerRvAdapter = TrailerRvAdapter(trailerList)
        rvTrailer.apply {
            layoutManager = LinearLayoutManager(this@DetailMovieActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = trailerRvAdapter
        }
    }

    private fun addToFavorite(movieTM: MovieTM){
        try {
            movieDao.insertMovie(movieTM)
        }catch (err: Exception){
            Log.e("insert error.", err.localizedMessage!!)
        }
    }

    private fun removeFromFavorite(movieId: Int){
        try {
            movieDao.deleteMovieById(movieId)
        }catch (err: Exception){
            Log.e("delete error.", err.localizedMessage!!)
        }
    }

    private fun isMovieFavorite(movieId: Int):Boolean{
//        var isFavorite = false
        val result = movieDao.findMovieById(movieId)

        Log.d("result",result.toString())

        if (result == movie.id){
            isFavorite = true
        }
        return isFavorite
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
