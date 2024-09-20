package com.example.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.playlistmaker.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val navControllerState = navController.saveState()
        outState.putBundle(Constants.BACKSTACK_KEY, navControllerState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //deleteDatabase("playlists.db") //todo delete

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        Navigation.setViewNavController(bottomNavigationView, navController)

        if (savedInstanceState != null) {
            val navControllerState = savedInstanceState.getBundle(Constants.BACKSTACK_KEY)
            navController.restoreState(navControllerState)
        } else {
            navController.setGraph(R.navigation.nav_graph)

        }

        val separatorDivider: View = findViewById(R.id.panelSeparatorDivider)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_player, R.id.navigation_new_playlist, R.id.navigation_edit_playlist -> {
                    bottomNavigationView.isVisible = false
                    separatorDivider.isVisible = false
                }

                else -> {
                    bottomNavigationView.isVisible = true
                    separatorDivider.isVisible = true
                }
            }

        }
    }

}

