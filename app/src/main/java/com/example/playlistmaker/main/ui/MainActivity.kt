package com.example.playlistmaker.main.ui


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.playlistmaker.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController



        bottomNavigationView.setupWithNavController(navController)
        if (checkForThemeChange()) {
            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(R.id.navigation_settings)
            navController.graph = navGraph
        } else {
            navController.setGraph(R.navigation.nav_graph)
        }

        Navigation.setViewNavController(bottomNavigationView, navController)
    }

    private fun checkForThemeChange(): Boolean {
        val changingThemeSharedPreferences =
            getSharedPreferences(Constants.THEME_CHANGE_KEY, MODE_PRIVATE)

        if (changingThemeSharedPreferences.getBoolean(Constants.THEME_CHANGE_KEY, false)) {
            changingThemeSharedPreferences.edit().putBoolean(Constants.THEME_CHANGE_KEY, false)
                .apply()
            return true
        } else {
            return false
        }

    }
}
