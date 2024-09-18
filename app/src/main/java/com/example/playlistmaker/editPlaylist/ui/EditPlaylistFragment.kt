package com.example.playlistmaker.editPlaylist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.player.domain.BottomSheetState
import com.example.playlistmaker.search.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : Fragment() {

    private val editPlaylistFragmentViewModel: EditPlaylistViewModel by viewModel()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPress()
            true
        }
    }

    lateinit var playlistEditCover: ImageView
    lateinit var playlistEditTitle: TextView
    lateinit var playlistEditDescription: TextView
    lateinit var playlistEditTotalPlaytimeAndCount: TextView
    lateinit var playlistEditOpenShareMenu: ImageView
    lateinit var playlistEditOpenOptionsMenu: ImageView
    lateinit var playlistEditBackButton: ImageView
    lateinit var playlistEditBottomsheetFadeOverlay: View
    lateinit var playlistEditBottomSheet: ConstraintLayout
    lateinit var playlistEditTrackListRecycler: RecyclerView
    lateinit var playlistEditNoTracksInPlaylistError: TextView
    lateinit var playlistEditMenu: ConstraintLayout
    lateinit var playlistEditMenuCover: ImageView
    lateinit var playlistEditMenuTitle: TextView
    lateinit var playlistEditMenuCount: TextView
    lateinit var playlistEditMenuShare: TextView
    lateinit var playlistEditMenuEdit: TextView
    lateinit var playlistEditMenuDelete: TextView
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    var localPlaylist: Playlist = Playlist(-1,"","","", emptyList(),0,-1)

    companion object {
        fun newInstance() = EditPlaylistFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_playlist_edit, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        setUpViews(view)

        editPlaylistFragmentViewModel.bottomSheetLiveData.observe(
            viewLifecycleOwner,
            Observer { state ->
                updateBottomSheetUI(state)
            })
        editPlaylistFragmentViewModel.playlistLiveData.observe(
            viewLifecycleOwner,
            Observer { updatedPlaylist ->
                setPlaylistValues()
            })

        val newPlaylistTitle = arguments?.getString(Constants.SOURCE_FRAMENT_KEY, "") ?: ""
        val canceledFromCreation = arguments?.getString(Constants.NEW_PLATLIST_CANCEL_KEY, "")
        val passedPlaylistID: Long =
            arguments?.getLong(Constants.PARCELABLE_PLAYLIST_TO_EDIT_PLAYLIST_KEY, -1)
                ?: -1 //todo add args to source frags

        if (passedPlaylistID.toInt() != -1) {
            editPlaylistFragmentViewModel.getPlaylistByID(passedPlaylistID)
        }


        playlistEditBackButton.setOnClickListener {
            backPress()
        }


        return view
    }

    private fun setUpViews(view: View) {
        playlistEditCover = view.findViewById(R.id.playlistEditCover)
        playlistEditTitle = view.findViewById(R.id.playlistEditTitle)
        playlistEditDescription = view.findViewById(R.id.playlistEditDescription)
        playlistEditTotalPlaytimeAndCount =
            view.findViewById(R.id.playlistEditTotalPlaytimeAndCount)
        playlistEditOpenShareMenu = view.findViewById(R.id.playlistEditOpenShareMenu)
        playlistEditOpenOptionsMenu = view.findViewById(R.id.playlistEditOpenOptionsMenu)
        playlistEditBackButton = view.findViewById(R.id.playlistEditBackButton)
        playlistEditBottomsheetFadeOverlay =
            view.findViewById(R.id.playlistEditBottomsheetFadeOverlay)
        playlistEditBottomSheet = view.findViewById(R.id.playlistEditBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(playlistEditBottomSheet)
        playlistEditTrackListRecycler = view.findViewById(R.id.playlistEditTrackListRecycler)
        playlistEditNoTracksInPlaylistError =
            view.findViewById(R.id.playlistEditNoTracksInPlaylistError)
        playlistEditMenu = view.findViewById(R.id.playlistEditMenu)
        playlistEditMenuCover = view.findViewById(R.id.playlistEditMenuCover)
        playlistEditMenuTitle = view.findViewById(R.id.playlistEditMenuTitle)
        playlistEditMenuCount = view.findViewById(R.id.playlistEditMenuCount)
        playlistEditMenuShare = view.findViewById(R.id.playlistEditMenuShare)
        playlistEditMenuEdit = view.findViewById(R.id.playlistEditMenuEdit)
        playlistEditMenuDelete = view.findViewById(R.id.playlistEditMenuDelete)
    }

    private fun setPlaylistValues(updatedPlaylist: Playlist, viewToUpdate : View) {
        if (viewToUpdate!=playlistEditCover && viewToUpdate!=playlistEditMenuCover)
        {}
        else{
            playlistEditMenuCover.setImageURI()
            playlistEditCover
        }

    }



    private fun backPress() {
        when (arguments?.getString(Constants.SOURCE_FRAMENT_KEY)) {
            "player" -> {
                val trackFromExtra: Track? =
                    arguments?.getParcelable<Track>(Constants.PARCELABLE_TO_PLAYER_KEY)
                val args = Bundle()
                args.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY, trackFromExtra)
                args.putString(Constants.SOURCE_FRAMENT_KEY, "new_playlist")
                findNavController().navigate(R.id.action_navigation_new_playlist_to_player, args)

            }

            else -> {
                val bundle = Bundle()
                bundle.putString(Constants.NEW_PLATLIST_CANCEL_KEY, "yes")
                findNavController().navigate(
                    R.id.action_navigation_new_playlist_to_playlists,
                    bundle
                )
            }
        }

    }

    private fun updateBottomSheetUI(state: BottomSheetState) {
        when (state) {
            BottomSheetState.HIDDEN -> {
                playlistEditBottomSheet.isVisible = false
                bottomSheetBehavior.state = STATE_HIDDEN
            }

            BottomSheetState.EDIT_MENU_OPENED -> {
                playlistEditBottomSheet.isVisible = true
                bottomSheetBehavior.state = STATE_COLLAPSED
                playlistEditMenu.isVisible = true
                playlistEditTrackListRecycler.isVisible = false
                playlistEditNoTracksInPlaylistError.isVisible = false
            }

            BottomSheetState.TRACK_LIST_OPEN_FULL -> {
                playlistEditBottomSheet.isVisible = true
                bottomSheetBehavior.state = STATE_COLLAPSED
                playlistEditMenu.isVisible = false
                playlistEditTrackListRecycler.isVisible = true
                playlistEditNoTracksInPlaylistError.isVisible = false

            }

            BottomSheetState.TRACK_LIST_OPEN_EMPTY -> {
                playlistEditBottomSheet.isVisible = true
                bottomSheetBehavior.state = STATE_COLLAPSED
                playlistEditMenu.isVisible = false
                playlistEditTrackListRecycler.isVisible = false
                playlistEditNoTracksInPlaylistError.isVisible = true
            }

        }
    }

    fun comparePlaylistsAndSelectivelyUpdate(newPlaylist: Playlist) {
        if (localPlaylist.playlistTitle != newPlaylist.playlistTitle) {
            playlistEditTitle
            playlistEditMenuTitle
        }
        if (localPlaylist.playlistDescriptor != newPlaylist.playlistDescriptor) {

        }
        if (localPlaylist.coverImagePath != newPlaylist.coverImagePath) {
            updateCovers(newPlaylist.coverImagePath)


        }
        if (localPlaylist.trackList != newPlaylist.trackList) {

        }
        if (localPlaylist.trackCount != newPlaylist.trackCount) {

        }
        if (localPlaylist.entryTime != newPlaylist.entryTime) {

        }
    }

    private fun updateCovers(imagePath: String) {


        playlistEditCover.setImageBitmap(getBitmapfromPath(newPlaylist.coverImagePath))
        playlistEditMenuCover.setImageBitmap(getBitmapfromPath(newPlaylist.coverImagePath))
    }
}