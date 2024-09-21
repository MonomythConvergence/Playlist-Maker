package com.example.playlistmaker.library.ui.playlists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.PlaylistClickCallback
import com.example.playlistmaker.library.domain.playlist.Playlist

class PlaylistsAdapter (
    private val context: Context,
    private var displayedList: List<Playlist>,
    private val trackClickCallback: PlaylistClickCallback
    ) :

    RecyclerView.Adapter<PlaylistViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_playlist, parent, false)
            val holder = PlaylistViewHolder(view)

            holder.itemView.setOnClickListener {
                val position = holder.absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playlist = displayedList[position]
                    trackClickCallback.onClickCallback(playlist)
                }
            }

            return holder
        }

        private fun countUpTracks(trackCount: Int): String {
            val result = when {
                (trackCount % 10 == 1 && trackCount % 100 != 11) -> "${trackCount} трек"
                trackCount % 10 in 2..4 && trackCount % 100 !in 12..14 -> "${trackCount} трека"
                else -> "${trackCount} треков"
            }
            return result

        }

        override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
            val playlist = displayedList[position]

            val trackCount = countUpTracks(playlist.trackCount)
            holder.bind(playlist.playlistTitle,trackCount, playlist.coverImagePath)
        }

        override fun getItemCount(): Int = displayedList.size

    }

