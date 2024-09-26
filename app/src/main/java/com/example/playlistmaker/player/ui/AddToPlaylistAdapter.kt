package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist

class AddToPlaylistAdapter(
    private val displayedList: List<Playlist>,
    private val listOfPlaylistsContainingTrack : ArrayList<Playlist>,
    private val onClick: AddToPlaylistClickback
) :

    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottomsheet, parent, false)
        val holder = PlaylistViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val playlist = displayedList[position]
                if (!listOfPlaylistsContainingTrack.contains(playlist)) {
                    listOfPlaylistsContainingTrack.add(playlist)
                    onClick.addSelectedTrackToPlaylist(playlist, true)
                }
                else onClick.addSelectedTrackToPlaylist(playlist, false)

            }
        }
        return holder
    }


    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = displayedList[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int = displayedList.size

}
