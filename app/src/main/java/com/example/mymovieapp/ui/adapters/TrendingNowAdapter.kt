package com.example.mymovieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.model.TrendingMovie
import com.example.mymovieapp.utils.loadImage

class TrendingAdapter(private val movies: List<TrendingMovie>) :
    RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    inner class TrendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.trendingPoster)
        val title: TextView = itemView.findViewById(R.id.trendingTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trending_movie, parent, false)
        return TrendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.poster.loadImage("https://image.tmdb.org/t/p/original/${movies[position].posterPath}")
    }

    override fun getItemCount(): Int = movies.size
}