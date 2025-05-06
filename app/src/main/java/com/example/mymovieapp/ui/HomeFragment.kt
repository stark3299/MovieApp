package com.example.mymovieapp.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.databinding.FragmentHomeBinding
import com.example.mymovieapp.db.MovieDatabase
import com.example.mymovieapp.model.NowShowingMovie
import com.example.mymovieapp.model.TrendingMovie
import com.example.mymovieapp.ui.adapters.NowPlayingAdapter
import com.example.mymovieapp.ui.adapters.TrendingAdapter
import com.example.mymovieapp.utils.SharedFunction
import com.example.mymovieapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var trendingMovieList : List<TrendingMovie> = emptyList()
    private var nowShowingMovieList : List<NowShowingMovie> = emptyList()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = MovieDatabase.getDatabase(requireContext())
        navController = findNavController()
        val factory = HomeViewModel.Factory(database.movieDao())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        setObservers()
        if (SharedFunction.getInstance().isInternetAvailable(requireContext())) {
            homeViewModel.getTrendingMovies()
            homeViewModel.getNowShowingMovies()
        } else {
            homeViewModel.loadTrendingFromCache()
            homeViewModel.loadNowShowingFromCache()
        }
    }

    private fun setObservers(){
        homeViewModel.trendingMoviesList.observe(viewLifecycleOwner) { trendingMovies ->
            trendingMovieList = trendingMovies
            setTrendingAdapter(trendingMovieList)
        }

        homeViewModel.nowShowingMoviesList.observe(viewLifecycleOwner) { nowShowingMovies ->
            nowShowingMovieList = nowShowingMovies
            setNowShowingAdapter(nowShowingMovieList)
        }

    }

    private fun setTrendingAdapter(list : List<TrendingMovie>){
        binding.trendingRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.trendingRecycler.adapter = TrendingAdapter(list, navController)
    }

    private fun setNowShowingAdapter(list : List<NowShowingMovie>){
        binding.nowPlayingRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.nowPlayingRecycler.adapter = NowPlayingAdapter(list, navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}