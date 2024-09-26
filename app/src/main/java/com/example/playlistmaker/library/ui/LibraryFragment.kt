package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    private val libraryFragmentViewModel: LibraryViewModel by viewModel()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (findNavController().previousBackStackEntry == null) {
                requireActivity().finish()
            } else {
                findNavController().popBackStack()
            }

            true
        }
    }

    companion object {
        fun newInstance() = LibraryFragment()
    }

    override fun onStop() {
        super.onStop()
        arguments?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_library, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        val newPlaylistTitle = arguments?.getString(Constants.TITLE_TOAST_KEY, "") ?: ""
        var cameFromNewPlaylist = (arguments?.getString(Constants.SOURCE_FRAGMENT_KEY, "")
            ?: "") == Constants.SOURCE_NEW_PLAYLIST
        val cameFromEditing = (arguments?.getString(Constants.SOURCE_FRAGMENT_KEY, "")
            ?: "") == Constants.SOURCE_EDIT_PLAYLIST
        val tabLayout = view.findViewById<TabLayout>(R.id.libraryTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.libraryViewPager)
        val adapter = LibraryFragmentAdapter(
            childFragmentManager, lifecycle
        )
        setUpUI(
            tabLayout,
            viewPager,
            adapter,
            (cameFromNewPlaylist || cameFromEditing)
        )

        if (cameFromNewPlaylist && newPlaylistTitle != "") {
            displayToast(newPlaylistTitle)
        }

        return view
    }

    private fun displayToast(newPlaylistTitle: String) {
        Toast.makeText(
            requireContext(),
            "${getString(R.string.playlist_toast_text, newPlaylistTitle)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setUpUI(
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        adapter: FragmentStateAdapter,
        switchToPlaylists: Boolean
    ) {
        viewPager.adapter = adapter

        if (switchToPlaylists) {
            viewPager.currentItem = 1
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }.attach()
    }
}
