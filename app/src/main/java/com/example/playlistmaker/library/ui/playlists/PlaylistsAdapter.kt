package com.example.playlistmaker.library.ui.playlists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist

class PlaylistsAdapter (
    private val context: Context,
    private var displayedList: List<Playlist>
    ) :

    RecyclerView.Adapter<PlaylistViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_playlist, parent, false)
            val holder = PlaylistViewHolder(view)

            //todo next sprint?

            return holder
        }

        override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
            val playlist = displayedList[position]
            val trackCount = context.getString(R.string.track_count, playlist.trackCount.toString())
            holder.bind(playlist.playlistTitle,trackCount, playlist.coverImagePath)
        }

        override fun getItemCount(): Int = displayedList.size

    }

