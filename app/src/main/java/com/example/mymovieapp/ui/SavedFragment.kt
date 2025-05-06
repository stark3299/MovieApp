package com.example.mymovieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovieapp.databinding.FragmentSavedBinding
import com.example.mymovieapp.model.SavedMovieItem
import com.example.mymovieapp.ui.adapters.WatchListMoviesAdapter
import com.example.mymovieapp.viewmodel.SavedViewModel

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private var savedMoviesList : List<SavedMovieItem> = emptyList()
    private lateinit var savedViewModel: SavedViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = SavedViewModel.Factory()
        navController = findNavController()
        savedViewModel = ViewModelProvider(this, factory)[SavedViewModel::class.java]
        setObservers()
        savedViewModel.getWatchlistMovies()
    }

    private fun setObservers(){
        savedViewModel.watchlistMoviesDetails.observe(viewLifecycleOwner) { watchListMovies ->
            savedMoviesList = watchListMovies
            setSavedMoviesAdapter(savedMoviesList)
        }
    }

    private fun setSavedMoviesAdapter(list : List<SavedMovieItem>){
        binding.savedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.savedRecyclerView.adapter = WatchListMoviesAdapter(list, navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}