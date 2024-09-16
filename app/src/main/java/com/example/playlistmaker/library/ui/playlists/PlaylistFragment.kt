package com.example.playlistmaker.library.ui.playlists

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.data.dto.PlaylistDTO
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()

    private lateinit var newPlaylistToast: TextView
    private lateinit var playlistRecycler: RecyclerView
    private lateinit var newPlaylistButton: Button
    private lateinit var noPlaylistsError: View

    private var localPlaylistList: ArrayList<PlaylistDTO> = ArrayList()

    val REQUEST_CODE_PERMISSIONS = 2

    companion object {
        private const val ARG_NAME = "newTitle"

        fun newInstance(name: String): PlaylistFragment {
            val fragment = PlaylistFragment()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_library_playlists, container)

        playlistRecycler = view.findViewById(R.id.playlistRecycler)
        noPlaylistsError = view.findViewById(R.id.noPlaylistsError)

        val newPlaylistTitle = arguments?.getString(ARG_NAME) ?: ""
        if (newPlaylistTitle != "") {
            displayToast(newPlaylistTitle)
        }
        checkPermission()

        playlistsFragmentViewModel.playlists.observe(viewLifecycleOwner, Observer { list ->
            updateUI(list)
        })

        setUpAdapter(playlistsFragmentViewModel)


        newPlaylistButton = view.findViewById(R.id.newPlaylistButton)
        newPlaylistButton.setOnClickListener {
            val arg = Bundle()
            arg.putString("source", "playlist")
            findNavController().navigate(R.id.navigation_new_playlist, arg)
        }

        return view
    }

    private fun updateUI(newList: List<PlaylistDTO>) {
        localPlaylistList.clear()
        localPlaylistList.addAll(newList)
        when (localPlaylistList) {
            emptyList<PlaylistDTO>() -> {
                playlistRecycler.isVisible = false
                noPlaylistsError.isVisible = true
            }

            else -> {
                playlistRecycler.isVisible = true
                noPlaylistsError.isVisible = false
                playlistRecycler.adapter?.notifyDataSetChanged()
                Log.d("MyTag", localPlaylistList[0].playlistTitle)
            }
        }
    }

    private fun setUpAdapter(playlistsFragmentViewModel: PlaylistsFragmentViewModel) {
        val playlistAdapter =
            PlaylistsAdapter(
                requireContext(), localPlaylistList
            )
        playlistRecycler.adapter = playlistAdapter
        playlistRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    private fun displayToast(newPlaylistTitle: String) {
        Toast.makeText(
            requireContext(),
            "${getString(R.string.playlist_toast_text, newPlaylistTitle)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS
            )
        }
    }


}


