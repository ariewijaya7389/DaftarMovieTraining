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
import kotlinx.android.synthetic.main.list_tayang.view.*

class TayangRvAdapter(private val tayangMovies:List<MovieTM>): RecyclerView.Adapter<TayangRvAdapter.TayangViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TayangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_tayang,parent,false
        )
        return TayangViewHolder(view)
    }
    override fun getItemCount(): Int = tayangMovies.size


    override fun onBindViewHolder(holder: TayangViewHolder, position: Int) {
        val tayangMovie = tayangMovies[position]

        holder.bindView(tayangMovie)
        }



    companion object{
        const val POSTER_URL = "https://image.tmdb.org/t/p/w185/"
        const val POSTER_URL_DETAIL = "https://image.tmdb.org/t/p/w500/"
    }

    inner class TayangViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(movieTM: MovieTM){
            itemView.apply {
                textTayang.text = movieTM.title
                textTayangSummary.text = movieTM.overview
                Glide
                    .with(context).asDrawable()
                    .load(POSTER_URL + movieTM.posterPath)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageTayang)
                setOnClickListener{
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