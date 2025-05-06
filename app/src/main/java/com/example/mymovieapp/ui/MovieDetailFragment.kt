package com.example.mymovieapp.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.FragmentMovieDetailsBinding
import com.example.mymovieapp.model.MovieDetailModel
import com.example.mymovieapp.utils.loadImage
import com.example.mymovieapp.viewmodel.MovieDetailViewModel
import androidx.core.net.toUri

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var movieAllDetails : MovieDetailModel
    private lateinit var movieId: String
    private var watchListStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getString("movieId")
            ?: throw IllegalArgumentException("movieId must be provided")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val factory = MovieDetailViewModel.Factory()
        movieDetailViewModel = ViewModelProvider(this, factory)[MovieDetailViewModel::class.java]
        setObservers()
        movieDetailViewModel.getMovieDetails(movieId)
        setOnclickForUrl()
        savedToWatchList()
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.shareButton.setOnClickListener {
            makeShareLinkForMoviePage()
        }
    }

    private fun setOnclickForUrl() {
        binding.playButton.setOnClickListener {
            val homepageUrl = movieAllDetails.homepage
            if (!homepageUrl.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, homepageUrl.toUri())
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No URL available for this movie.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeShareLinkForMoviePage(){
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://movieplex.com/movie?movieId=$movieId")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share movie link"))
    }

    private fun savedToWatchList() {
        binding.addToWatchList.setOnClickListener {
            movieDetailViewModel.addMovieToWatchlist(movieId)
        }
    }

    private fun setObservers() {
        movieDetailViewModel.movieAllDetails.observe(viewLifecycleOwner) { movieDetails ->
            movieAllDetails = movieDetails
            bindMovieDetailsData(movieDetails)
        }
        movieDetailViewModel.watchListAddedStatus.observe(viewLifecycleOwner) { status ->
            watchListStatus = status
            showToast(status)
        }
    }

    private fun showToast(status: Boolean){
        if(status){
            Toast.makeText(requireContext(), "Movie Added to you Watchlist", Toast.LENGTH_SHORT).show()
            val color = ContextCompat.getColor(requireContext(), R.color.red)
            binding.addToWatchList.imageTintList = ColorStateList.valueOf(color)
        } else
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    private fun bindMovieDetailsData(details: MovieDetailModel) {
        binding.posterImage.loadImage("https://image.tmdb.org/t/p/original/${details.posterPath}")
        binding.titleText.text = details.title
        val releaseYear = details.releaseDate.take(4)
        val runtimeFormatted = "${details.runtime / 60}h ${details.runtime % 60}m"
        val rating = if (details.adult) "R" else "PG"
        binding.infoText.text = "$releaseYear • $runtimeFormatted • $rating"

        // Genres
        binding.genreContainer.removeAllViews()
        details.genres.forEach { genre ->
            val genreTextView = TextView(requireContext()).apply {
                text = genre.name
                setPadding(24, 8, 24, 8)
                setTextColor(Color.WHITE)
                setBackgroundResource(R.drawable.genre_chips)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 16, 0)
                layoutParams = params
            }
            binding.genreContainer.addView(genreTextView)
        }
        val language = details.originalLanguage.uppercase()
        val country = details.originCountry.firstOrNull() ?: "N/A"
        val status = details.status
        binding.langInfoText.text = "$status • $language • $country"

        binding.overviewText.text = details.overview ?: "No description available."
        binding.productionLogosContainer.removeAllViews()
        details.productionCompanies.forEach { company ->
            company.logoPath?.let { logoPath ->
                val logoImageView = ImageView(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(120, 120).apply {
                        setMargins(0, 0, 16, 0)
                    }
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }
                logoImageView.loadImage("https://image.tmdb.org/t/p/original/$logoPath")
                binding.productionLogosContainer.addView(logoImageView)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
