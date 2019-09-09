package com.example.daftarmovie.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.daftarmovie.R
import com.example.daftarmovie.model.Trailer
import kotlinx.android.synthetic.main.list_tayang.view.*
import kotlinx.android.synthetic.main.list_trailer.view.*

class TrailerRvAdapter(private val trailerMovies:List<Trailer>): RecyclerView.Adapter<TrailerRvAdapter.TrailerViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_trailer,parent,false
        )
        return TrailerViewHolder(view)
    }
    override fun getItemCount(): Int = trailerMovies.size


    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailerMovies[position]

        holder.bindView(trailer)
    }

    inner class TrailerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(Trailer: Trailer){
            itemView.apply {
                Glide
                    .with(context).asDrawable()
                    .load("https://i1.ytimg.com/vi/${Trailer.key}/0.jpg")
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageThumbnail)
                setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+Trailer.key))

                    context.startActivity(intent)
                }
            }
        }
    }
}