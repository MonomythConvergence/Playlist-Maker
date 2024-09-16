package com.example.playlistmaker.library.ui.playlists

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R

class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val playlistTitleView: TextView = itemView.findViewById(R.id.playlistTitle)
    private val playlistTrackCountView: TextView = itemView.findViewById(R.id.playlistTrackCount)
    private val coverImageView: ImageView = itemView.findViewById(R.id.playlistCover)

    fun bind(playlistTitle: String,
             playlistTrackCount: String,
             coverImagePath: String) {
        playlistTitleView.text = playlistTitle
        playlistTrackCountView.text = playlistTrackCount

        Glide.with(coverImageView)
            .load(coverImagePath)
            .placeholder(R.drawable.placeholder)
            .into(coverImageView)
    }

}