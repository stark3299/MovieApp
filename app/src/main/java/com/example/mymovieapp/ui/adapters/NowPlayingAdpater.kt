package com.example.mymovieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.model.NowShowingMovie
import com.example.mymovieapp.utils.SharedFunction
import com.example.mymovieapp.utils.loadImage

class NowPlayingAdapter(private val movies: List<NowShowingMovie>, private val navController: NavController) :
    RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder>() {

    inner class NowPlayingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        val title: TextView = itemView.findViewById(R.id.movieTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_now_playing, parent, false)
        return NowPlayingViewHolder(view)
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.visibility = View.GONE
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