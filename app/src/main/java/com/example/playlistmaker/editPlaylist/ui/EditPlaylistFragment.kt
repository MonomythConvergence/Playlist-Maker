package com.example.playlistmaker.editPlaylist.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.editPlaylist.domain.PathToBitmapConverterCallback
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TrackClickCallback
import com.example.playlistmaker.search.ui.RecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditPlaylistFragment : Fragment() {

    private val editPlaylistFragmentViewModel: EditPlaylistViewModel by viewModel()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(R.id.action_navigation_edit_playlist_to_playlist)
            true
        }
    }

    private lateinit var playlistEditCover: ImageView
    private lateinit var playlistEditTitle: TextView
    private lateinit var playlistEditDescription: TextView
    private lateinit var playlistEditTotalPlaytime: TextView
    private lateinit var playlistEditTrackCount: TextView
    private lateinit var playlistEditOpenShareMenu: ImageView
    private lateinit var playlistEditOpenOptionsMenu: ImageView
    private lateinit var playlistEditBackButton: ImageView
    private lateinit var playlistEditBottomsheetFadeOverlay: View
    private lateinit var playlistEditBottomSheet: ConstraintLayout
    private lateinit var playlistEditTrackListRecycler: RecyclerView
    private lateinit var playlistEditNoTracksInPlaylistError: TextView
    private lateinit var playlistEditMenu: ConstraintLayout
    private lateinit var playlistEditMenuCover: ImageView
    private lateinit var playlistEditMenuTitle: TextView
    private lateinit var playlistEditMenuCount: TextView
    private lateinit var playlistEditMenuShare: TextView
    private lateinit var playlistEditMenuEdit: TextView
    private lateinit var playlistEditMenuDelete: TextView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var localListOfTracks: ArrayList<Track> = ArrayList()
    private var localPlaylist: Playlist = Playlist(-1, "", "", "", emptyList(), emptyList(), -1, -1)

    companion object {
        fun newInstance() = EditPlaylistFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
        arguments?.clear()
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

        setUpViewsAndAdapters(view)

        editPlaylistFragmentViewModel.playlistLiveData.observe(
            viewLifecycleOwner,
            Observer { updatedPlaylist ->
                comparePlaylistsAndSelectivelyUpdate(updatedPlaylist)
            })

        editPlaylistFragmentViewModel.playlistTracksLiveData.observe(
            viewLifecycleOwner,
            Observer { updatedTracks ->
                localListOfTracks.clear()
                localListOfTracks.addAll(updatedTracks)
                playlistEditTrackListRecycler.adapter?.notifyDataSetChanged()
            })



        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_HIDDEN -> {
                        switchBottomSheetUI(BottomSheetState.COLLAPSING)
                    }

                    else -> {
                        //nothing
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //nothing
            }
        })

        var passedPlaylist: Playlist? =
            arguments?.getParcelable(Constants.PARCELABLE_PLAYLIST_TO_EDIT_PLAYLIST_KEY)
        passedPlaylist =
            arguments?.getParcelable(Constants.PARCELABLE_NEW_PLAYLIST_TO_EDIT_PLAYLIST_KEY)
                ?: passedPlaylist
        passedPlaylist =
            arguments?.getParcelable<Playlist>(Constants.PARCELABLE_TO_PLAYER_KEY_PLAYLIST)
                ?: passedPlaylist
        Log.d("mytag","${arguments?.getParcelable<Playlist>(Constants.PARCELABLE_TO_PLAYER_KEY_PLAYLIST)}???")


        if (passedPlaylist != null) {
            editPlaylistFragmentViewModel.updatePlaylistInDB(passedPlaylist)
            editPlaylistFragmentViewModel.updatePlaylistLiveData(passedPlaylist)
            if (passedPlaylist.trackList.isNotEmpty()) {
                switchBottomSheetUI(BottomSheetState.TRACK_LIST_OPEN)
            } else {
                switchBottomSheetUI(BottomSheetState.TRACK_LIST_EMPTY)
            }
        }


        playlistEditBackButton.setOnClickListener {
            val arg = Bundle()
            arg.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_NEW_PLAYLIST)
            findNavController().navigate(R.id.action_navigation_edit_playlist_to_playlist)//note to self - do not change
        }

        playlistEditOpenOptionsMenu.setOnClickListener {
            switchBottomSheetUI(BottomSheetState.EDIT_MENU_OPENED)
        }
        playlistEditOpenShareMenu.setOnClickListener {
            sharePlaylist()
        }
        playlistEditMenuShare.setOnClickListener {
            sharePlaylist()
        }

        playlistEditMenuEdit.setOnClickListener {
            val args = Bundle()
            args.putParcelable(Constants.PARCELABLE_PLAYLIST_TO_NEW_PLAYLIST_KEY, localPlaylist)
            args.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_EDIT_PLAYLIST)
            findNavController().navigate(R.id.action_navigation_edit_playlist_to_new_playlist, args)
        }

        playlistEditMenuDelete.setOnClickListener {
            showPlaylistDeletionDialogue(localPlaylist)
        }


        return view
    }


    //UI Setup

    private fun setUpViewsAndAdapters(view: View) {
        playlistEditCover = view.findViewById(R.id.playlistEditCover)
        playlistEditTitle = view.findViewById(R.id.playlistEditTitle)
        playlistEditDescription = view.findViewById(R.id.playlistEditDescription)
        playlistEditTotalPlaytime =
            view.findViewById(R.id.playlistEditTotalPlaytime)
        playlistEditTrackCount =
            view.findViewById(R.id.playlistEditTrackCount)
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

        val clickBack = object : TrackClickCallback {

            override fun onLongClickCallback(track: Track) {
                showTrackDeletionDialogue(track)
            }

            override fun onClickCallback(track: Track) {
                val args = Bundle()
                args.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY_TRACK, track)
                args.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY_PLAYLIST, localPlaylist)
                Log.d("mytag","${localPlaylist.trackCount}")//todo delete
                args.putString(Constants.SOURCE_FRAGMENT_KEY,Constants.SOURCE_EDIT_PLAYLIST)
                findNavController().navigate(R.id.action_navigation_edit_playlist_to_player, args)
            }
        }

        val adapter = RecyclerAdapter(
            localListOfTracks,
            editPlaylistFragmentViewModel,
            clickBack
        )
        playlistEditTrackListRecycler.adapter = adapter
        playlistEditTrackListRecycler.layoutManager = GridLayoutManager(requireContext(), 1)


    }

    private fun showPlaylistDeletionDialogue(playlistToDelete: Playlist) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_playlist_deletion, localPlaylist.playlistTitle))
            .setNeutralButton("Нет") { dialog, which ->

                switchBottomSheetUI(BottomSheetState.COLLAPSING)
                dialog.dismiss()
            }
            .setPositiveButton("Да") { dialog, which ->
                editPlaylistFragmentViewModel.deletePlaylist(playlistToDelete)
                val args = Bundle()
                args.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_EDIT_PLAYLIST)
                findNavController().navigate(R.id.action_navigation_edit_playlist_to_playlist)
            }.show()
    }

    private fun showTrackDeletionDialogue(trackToDelete: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_track_deletion))
            .setNeutralButton(getString(R.string.no)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                if (localListOfTracks.size==1)
                {switchBottomSheetUI(BottomSheetState.TRACK_LIST_EMPTY)}

                editPlaylistFragmentViewModel.deleteTrackFromPlaylist(trackToDelete)
            }.show()
    }

    //UI updates

    private fun switchBottomSheetUI(state: BottomSheetState) {
        when (state) {
            BottomSheetState.EDIT_MENU_OPENED -> {
                bottomSheetBehavior.setPeekHeight(dpToPx(383))
                playlistEditBottomSheet.isVisible = true
                bottomSheetBehavior.state = STATE_COLLAPSED
                playlistEditMenu.isVisible = true
                playlistEditTrackListRecycler.isVisible = false
                playlistEditNoTracksInPlaylistError.isVisible = false
                playlistEditBottomsheetFadeOverlay.isVisible = true
                bottomSheetBehavior.isHideable = true
            }

            BottomSheetState.TRACK_LIST_OPEN -> {
                bottomSheetBehavior.setPeekHeight(dpToPx(266))
                playlistEditBottomSheet.isVisible = true
                bottomSheetBehavior.state = STATE_COLLAPSED
                playlistEditMenu.isVisible = false
                playlistEditTrackListRecycler.isVisible = true
                playlistEditNoTracksInPlaylistError.isVisible = false
                playlistEditBottomsheetFadeOverlay.isVisible = false
                bottomSheetBehavior.isHideable = false

            }

            BottomSheetState.TRACK_LIST_EMPTY -> {
                bottomSheetBehavior.setPeekHeight(dpToPx(266))
                playlistEditBottomSheet.isVisible = true
                bottomSheetBehavior.state = STATE_COLLAPSED
                playlistEditMenu.isVisible = false
                playlistEditTrackListRecycler.isVisible = false
                playlistEditNoTracksInPlaylistError.isVisible = true
                playlistEditBottomsheetFadeOverlay.isVisible = false
                bottomSheetBehavior.isHideable = false
            }

            BottomSheetState.COLLAPSING -> {
                bottomSheetBehavior.isHideable = true
                if (localListOfTracks.isNotEmpty()) {
                    switchBottomSheetUI(BottomSheetState.TRACK_LIST_OPEN)
                } else {
                    switchBottomSheetUI(BottomSheetState.TRACK_LIST_EMPTY)
                }
            }
        }
    }

    private fun comparePlaylistsAndSelectivelyUpdate(newPlaylist: Playlist) {
        if (localPlaylist.playlistTitle != newPlaylist.playlistTitle) {
            playlistEditTitle.text = newPlaylist.playlistTitle
            playlistEditMenuTitle.text = newPlaylist.playlistTitle
        }
        if (localPlaylist.playlistDescriptor != newPlaylist.playlistDescriptor) {
            playlistEditDescription.text = newPlaylist.playlistDescriptor
        }

        if (localPlaylist.coverImagePath != newPlaylist.coverImagePath) {
            updateCovers(newPlaylist.coverImagePath)
        }

        if (playlistEditTrackCount.text != countUpTracks(newPlaylist.trackList)) {
            playlistEditTrackCount.text = countUpTracks(newPlaylist.trackList)
            playlistEditMenuCount.text = countUpTracks(newPlaylist.trackList)
            playlistEditTotalPlaytime.text = countUpMinutes(convertToMinutes(newPlaylist.trackList))
        }

        if (localListOfTracks != newPlaylist.trackList) {
            editPlaylistFragmentViewModel.updatePlaylistTracksLiveData(newPlaylist.trackList)
        }

        localPlaylist = newPlaylist
    }


    private fun updateCovers(imagePath: String) {
        val bitmapCallback = object : PathToBitmapConverterCallback {
            override suspend fun convertPathToBitmap(bitmap: Any) {
                playlistEditCover.setImageBitmap(bitmap as Bitmap)
                playlistEditMenuCover.setImageBitmap(bitmap as Bitmap)
            }
        }
        editPlaylistFragmentViewModel.convertPathToBitmap(imagePath, bitmapCallback)
    }

    //Auxiliary functions

    private fun sharePlaylist() {
        val messageHeader = "${playlistEditTrackCount.text}:" + "\n"
        var messageBulk = ""
        var trackCounter = "0"
        for (track in localListOfTracks) {
            trackCounter = trackCounter + 1
            messageBulk =
                messageBulk + "${trackCounter}. ${track.artistName} - ${track.trackName} ${track.trackTime}\n"
        }

        if (localListOfTracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_tracks_to_share),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            editPlaylistFragmentViewModel.handleShare(messageHeader + messageBulk)
        }
    }

    fun dpToPx(dp: Int): Int {
        val density = requireContext().resources.displayMetrics.density
        return Math.round(dp * density)
    }

    private fun convertToMinutes(list: List<Track>): Int {//в требованиях ничего - поэтому единолично решил брать целые
        var seconds = 0
        for (element in list) {
            val parts = element.trackTime.split(":")
            seconds += (parts[0].toInt() * 60 + parts[1].toInt())
        }
        val minutes = ((seconds + 59) / 60)
        return minutes
    }

    private fun countUpMinutes(minutes: Int): String {
        val result = when {
            (minutes % 10 == 1 && minutes % 100 != 11) -> "$minutes минута"
            minutes % 10 in 2..4 && minutes % 100 !in 12..14 -> "$minutes минуты"
            else -> "$minutes минут"
        }
        return result
    }

    private fun countUpTracks(list: List<Track>): String {

        val result = when {
            (list.size % 10 == 1 && list.size % 100 != 11) -> "${list.size} трек"
            list.size % 10 in 2..4 && list.size % 100 !in 12..14 -> "${list.size} трека"
            else -> "${list.size} треков"
        }
        return result
    }
}