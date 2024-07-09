package com.example.playlistmaker.library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener { finish() }

        val tabLayout = findViewById<TabLayout>(R.id.libraryTabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.libraryViewPager)
        val adapter = FragmentPagerAdapter(
            supportFragmentManager,
            lifecycle
        )
        setUpUI(tabLayout, viewPager, adapter)


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setUpUI(
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        adapter: FragmentStateAdapter
    ) {
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }.attach()
    }

}