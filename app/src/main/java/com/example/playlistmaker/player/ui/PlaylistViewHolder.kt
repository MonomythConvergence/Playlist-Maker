package com.example.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.library.data.dto.PlaylistDTO

class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val playlistTitle: TextView = itemView.findViewById(R.id.addToPlaylistTitle)
    private val playlistTrackCount: TextView = itemView.findViewById(R.id.addToPlaylistTrackCount)
    private val playlistCover: ImageView = itemView.findViewById(R.id.addToPlaylistCover)

    fun bind(playlistDTO: PlaylistDTO) {
        playlistTitle.text = playlistDTO.playlistTitle
        playlistTrackCount.text = playlistDTO.trackCount.toString()

        Glide.with(playlistCover)
            .load(playlistDTO.coverImagePath)
            .placeholder(R.drawable.placeholder)
            .into(playlistCover)
    }

}