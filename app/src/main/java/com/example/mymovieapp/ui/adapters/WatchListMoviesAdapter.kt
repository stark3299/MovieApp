package com.example.mymovieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.model.SavedMovieItem
import com.example.mymovieapp.utils.SharedFunction
import com.example.mymovieapp.utils.loadImage

class WatchListMoviesAdapter(private val movies: List<SavedMovieItem>, private val navController: NavController) :
    RecyclerView.Adapter<WatchListMoviesAdapter.WatchListMoviesViewHolder>() {

    inner class WatchListMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListMoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_movie, parent, false)
        return WatchListMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchListMoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.poster.loadImage("https://image.tmdb.org/t/p/original/${movie.posterPath}")
        holder.poster.setOnClickListener {
            SharedFunction.getInstance().openMovieDetailsPage(movie.id.toString(), navController)
        }
        holder.itemView.setOnClickListener {
            SharedFunction.getInstance().openMovieDetailsPage(movie.id.toString(), navController)
        }
    }

    override fun getItemCount(): Int = movies.size.coerceAtMost(10)
}