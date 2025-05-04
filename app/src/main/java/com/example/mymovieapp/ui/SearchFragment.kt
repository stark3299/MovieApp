package com.example.mymovieapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovieapp.databinding.FragmentSearchBinding
import com.example.mymovieapp.model.RecommendedMovie
import com.example.mymovieapp.ui.adapters.RecommendedAdapter
import com.example.mymovieapp.ui.adapters.SearchMovieAdapter
import com.example.mymovieapp.utils.SharedFunction
import com.example.mymovieapp.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    val factory = SearchViewModel.Factory()
    private var recommendedMoviesList : List<RecommendedMovie> = emptyList()
    private lateinit var searchAdapter: SearchMovieAdapter
    private val instance : SharedFunction = SharedFunction.getInstance()
    private val searchViewModel by lazy {
        ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = SearchMovieAdapter{}
        setSearchItemRecyclerView()
        setObservers()
        if(instance.checkNullOrNot(instance.getSavedMovieId(context).toString())){
            searchViewModel.getRecommendedMovies(instance.getSavedMovieId(context).toString())
        } else {
            binding.recommendedRecyclerView.visibility = View.GONE
            binding.recommendedTitle.visibility = View.GONE
        }
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    searchAdapter.submitList(emptyList())
                    binding.searchRecyclerView.visibility = View.GONE
                    binding.searchTitle.visibility = View.GONE
                } else {
                    searchViewModel.setSearchQuery(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setObservers(){
        searchViewModel.searchMoviesList.observe(viewLifecycleOwner) { searchMovies ->
            val query = binding.searchEditText.text.toString()
            if (query.isEmpty()) {
                searchAdapter.submitList(emptyList())
                binding.searchRecyclerView.visibility = View.GONE
                binding.searchTitle.visibility = View.GONE
            } else if (!searchMovies.isNullOrEmpty()) {
                searchAdapter.submitList(searchMovies.take(15))
                binding.searchRecyclerView.visibility = View.VISIBLE
                binding.searchTitle.visibility = View.VISIBLE
            } else {
                searchAdapter.submitList(emptyList())
                binding.searchRecyclerView.visibility = View.GONE
                binding.searchTitle.visibility = View.GONE
            }
        }

        searchViewModel.recommendedMoviesList.observe(viewLifecycleOwner) { recommendedMovies ->
            recommendedMoviesList = recommendedMovies
            setRecommendedAdapter(recommendedMoviesList)
        }
    }

    private fun setSearchItemRecyclerView(){
        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchRecyclerView.adapter = searchAdapter
    }

    private fun setRecommendedAdapter(list : List<RecommendedMovie>){
        binding.recommendedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recommendedRecyclerView.adapter = RecommendedAdapter(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}