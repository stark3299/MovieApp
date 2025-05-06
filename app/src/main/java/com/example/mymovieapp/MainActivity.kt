package com.example.mymovieapp

import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mymovieapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_saved
            )
        )
        handleDeepLink(intent?.data)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_saved -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }
        }
    }

    private fun handleDeepLink(uri: Uri?) {
        uri?.let {
            if (it.host == "movieplex.com" && it.path == "/movie") {
                val movieId = it.getQueryParameter("movieId")
                if (!movieId.isNullOrEmpty()) {
                    openMovieDetailsPage(movieId)
                }
            }
        }
    }

    private fun openMovieDetailsPage(movieId: String) {
        val bundle = Bundle().apply {
            putString("movieId", movieId)
        }
        navController.navigate(R.id.movieDetailFragment, bundle)
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}