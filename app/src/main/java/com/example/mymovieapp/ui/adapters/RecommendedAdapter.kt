package com.example.mymovieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.model.RecommendedMovie
import com.example.mymovieapp.utils.loadImage

class RecommendedAdapter(private val movies: List<RecommendedMovie>) : RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>(){
    inner class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.trendingPoster)
        val title: TextView = itemView.findViewById(R.id.trendingTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trending_movie, parent, false)
        return RecommendedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        holder.title.visibility = View.GONE
        holder.poster.loadImage("https://image.tmdb.org/t/p/original/${movies[position].posterPath}")
    }

    override fun getItemCount(): Int = movies.size.coerceAtMost(10)
}
