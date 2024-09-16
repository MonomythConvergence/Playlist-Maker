package com.example.playlistmaker.library.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.ItemClickCallback
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.ui.RecyclerAdapter
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FavoritesFragment : Fragment() {

    private val favoritesFragmentViewModel: FavoritesFragmentViewModel by viewModel()

    private lateinit var noItemsFrame: ConstraintLayout
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var localFavoritesList: ArrayList<Track> = ArrayList<Track>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    val debounceClick = debounce<Unit>(Constants.CLICK_DEBOUNCE_DELAY, coroutineScope, true) {
        favoritesFragmentViewModel.setClickDebounce(true)
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentView: View = inflater.inflate(R.layout.fragment_library_favorites, container)
        noItemsFrame =
            fragmentView.findViewById<ConstraintLayout>(R.id.libraryFavoritesNoItemsFrame)
        recycler = fragmentView.findViewById<RecyclerView>(R.id.libraryFavoritesRecycler)
        progressBar = fragmentView.findViewById<ProgressBar>(R.id.progressBar)

        setUpRecyclerAndAdapter()

        favoritesFragmentViewModel.favoritesList.observe(viewLifecycleOwner) { favoriteTracks ->
            updateUI(favoriteTracks)
        }
        favoritesFragmentViewModel.loadFavorites()
        return fragmentView
    }

    private fun updateUI(newFavoritesList: List<Track>?) {
        when (newFavoritesList) {
            null -> {
                noItemsFrame.isVisible = false
                recycler.isVisible = false
                progressBar.isVisible = true
            }

            emptyList<Track>() -> {
                noItemsFrame.isVisible = true
                recycler.isVisible = false
                progressBar.isVisible = false
            }

            else -> {
                localFavoritesList.clear()
                localFavoritesList.addAll(newFavoritesList)
                recycler.adapter?.notifyDataSetChanged()
                noItemsFrame.isVisible = false
                recycler.isVisible = true
                progressBar.isVisible = false
            }
        }
    }

    private fun setUpRecyclerAndAdapter() {
        val itemClickCallback = object : ItemClickCallback {
            override fun onClickCallback(track: Track) {

                if (!favoritesFragmentViewModel.getClickDebounceState()) {
                    favoritesFragmentViewModel.setClickDebounce(false)
                    debounceClick(Unit)
                    val bundle = Bundle()
                    bundle.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY, track)
                    bundle.putString("source", "library")
                    findNavController().navigate(
                        R.id.action_navigation_library_to_player,
                        bundle
                    )
                }
            }

        }


        val favoritesAdapter =
            RecyclerAdapter(
                localFavoritesList,
                favoritesFragmentViewModel,
                itemClickCallback
            )
        recycler.adapter = favoritesAdapter
        recycler.layoutManager = GridLayoutManager(requireContext(), 1)
    }



}
