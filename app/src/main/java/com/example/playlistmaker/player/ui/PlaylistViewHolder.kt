package com.example.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist

class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val playlistTitle: TextView = itemView.findViewById(R.id.addToPlaylistTitle)
    private val playlistTrackCount: TextView = itemView.findViewById(R.id.addToPlaylistTrackCount)
    private val playlistCover: ImageView = itemView.findViewById(R.id.addToPlaylistCover)

    fun bind(playlist: Playlist) {
        playlistTitle.text = playlist.playlistTitle
        playlistTrackCount.text = playlist.trackCount.toString()

        Glide.with(playlistCover)
            .load(playlist.coverImagePath)
            .placeholder(R.drawable.placeholder)
            .into(playlistCover)
    }

}