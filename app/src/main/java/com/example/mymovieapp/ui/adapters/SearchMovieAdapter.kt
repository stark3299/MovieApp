package com.example.mymovieapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.model.SearchItem
import com.example.mymovieapp.utils.SharedFunction
import com.example.mymovieapp.utils.loadImage

class SearchMovieAdapter(private val navController: NavController, private val onItemClick: (SearchItem) -> Unit) : ListAdapter<SearchItem, SearchMovieAdapter.SearchViewHolder>(SearchDiffCallback()){
    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)

        fun bind(movie: SearchItem) {
            titleTextView.text = movie.title ?: "No Title"
            poster.loadImage("https://image.tmdb.org/t/p/original/${movie.posterPath}")
            itemView.setOnClickListener {
                SharedFunction.getInstance().openMovieDetailsPage(movie.id.toString(), navController)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_movie, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Log.d("AdapterBind", "Binding item at position: $position")
        holder.bind(getItem(position))
        holder.poster.setOnClickListener {
            SharedFunction.getInstance().openMovieDetailsPage(getItem(position).id.toString(), navController)
        }
        holder.titleTextView.setOnClickListener {
            SharedFunction.getInstance().openMovieDetailsPage(getItem(position).id.toString(), navController)
        }
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            Log.d("DiffUtil", "Comparing IDs: ${oldItem.id} vs ${newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }
    }
}