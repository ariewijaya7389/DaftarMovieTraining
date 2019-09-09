package com.example.daftarmovie.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.daftarmovie.R
import com.example.daftarmovie.model.MovieTM
import com.example.daftarmovie.ui.detail.DetailMovieActivity
import kotlinx.android.synthetic.main.list_movie_item.view.*

class MovieRvAdapter(private val movies:List<MovieTM>) : RecyclerView.Adapter<MovieRvAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_movie_item, parent, false
        )
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie =  movies[position]
//        holder.itemView.imagePoster.setBackgroundResource(movie.image)
//        holder.itemView.textTitle.text = movie.title

        holder.bindView(movie)
    }

    companion object{
        const val POSTER_URL = "https://image.tmdb.org/t/p/w185/"
    }

    inner class MovieViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(movieTM: MovieTM){
            itemView.apply {
                textTitle.text = movieTM.title
                Glide
                    .with(context).asDrawable()
                    .load(POSTER_URL + movieTM.posterPath)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imagePoster)
                setOnClickListener {
                    val intent = Intent(context, DetailMovieActivity::class.java)
//                    intent.putExtra("title", movieTM.title)
//                    intent.putExtra("releaseDate", movieTM.releaseDate)
//                    intent.putExtra("popularity", movieTM.popularity)
                    intent.putExtra("movieIntent", movieTM)

                    context.startActivity(intent)
                }
            }
        }
    }


}