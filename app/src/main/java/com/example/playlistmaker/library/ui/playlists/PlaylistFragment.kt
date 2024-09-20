package com.example.playlistmaker.library.ui.playlists

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.PlaylistClickCallback
import com.example.playlistmaker.library.domain.playlist.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()

    private lateinit var playlistRecycler: RecyclerView
    private lateinit var newPlaylistButton: Button
    private lateinit var noPlaylistsError: View

    private var permissionGranted: Boolean = false
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            permissionGranted = isGranted
        }

    private var localPlaylistList: ArrayList<Playlist> = ArrayList()

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_library_playlists, container)

        playlistRecycler = view.findViewById(R.id.playlistRecycler)
        noPlaylistsError = view.findViewById(R.id.noPlaylistsError)

        if (!permissionGranted) {
            readPermissionCheck()
        }


        playlistsFragmentViewModel.playlists.observe(viewLifecycleOwner, Observer { list ->
            updateUI(list)
        })

        setUpAdapter()


        newPlaylistButton = view.findViewById(R.id.newPlaylistButton)
        newPlaylistButton.setOnClickListener {
            val arg = Bundle()
            arg.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_PLAYLIST)
            findNavController().navigate(R.id.action_navigation_library_to_new_playlist, arg)
        }

        return view
    }

    private fun updateUI(newList: List<Playlist>) {
        localPlaylistList.clear()
        localPlaylistList.addAll(newList)
        when (localPlaylistList) {
            emptyList<Playlist>() -> {
                playlistRecycler.isVisible = false
                noPlaylistsError.isVisible = true
            }

            else -> {
                playlistRecycler.isVisible = true
                noPlaylistsError.isVisible = false
                playlistRecycler.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun setUpAdapter() {
        val playlistClickCallback = object : PlaylistClickCallback {
            override fun onClickCallback(playlist: Playlist) {
                val bundle = Bundle()
                bundle.putParcelable(Constants.PARCELABLE_PLAYLIST_TO_EDIT_PLAYLIST_KEY, playlist)
                findNavController().navigate(
                    R.id.action_navigation_library_to_edit_playlist,
                    bundle
                )
            }

            override fun onLongClickCallback(playlist: Playlist) {
                //nothing
            }
        }

        val playlistAdapter =
            PlaylistsAdapter(
                requireContext(), localPlaylistList, playlistClickCallback
            )
        playlistRecycler.adapter = playlistAdapter
        playlistRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun readPermissionCheck(): Boolean {
        when (Build.VERSION.SDK_INT) {
            0 - 28 -> {
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    return false
                } else {
                    return true
                }
            }

            else -> {
                if (checkPermission(Manifest.permission.READ_MEDIA_IMAGES)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    return (checkPermission(Manifest.permission.READ_MEDIA_IMAGES))
                } else {
                    return true
                }
            }
        }
    }


    private fun checkPermission(permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }


}


