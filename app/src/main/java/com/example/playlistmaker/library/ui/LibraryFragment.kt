package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
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

        val newPlaylistTitle = arguments?.getString("playlistTitleForToast", "") ?: ""
        val canceledFromCreation = arguments?.getString("plainCancel", "")

        val tabLayout = view.findViewById<TabLayout>(R.id.libraryTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.libraryViewPager)
        val adapter = LibraryFragmentAdapter(
            newPlaylistTitle,
            childFragmentManager, lifecycle
        )
        setUpUI(
            tabLayout,
            viewPager,
            adapter,
            (newPlaylistTitle != "" || canceledFromCreation != "")
        )

        return view
    }

    private fun setUpUI(
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        adapter: FragmentStateAdapter,
        switchToPlaylists: Boolean
    ) {
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }.attach()
        if (switchToPlaylists) {
            viewPager.currentItem = 1
        }

    }
}
