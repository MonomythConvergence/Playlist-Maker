package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.data.dto.PlaylistDTO

class AddToPlaylistAdapter(
    private val diplayedList: List<PlaylistDTO>,
    private val listOfPlaylistsContainingTrack : ArrayList<PlaylistDTO>,
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
                val playlist = diplayedList[position]
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
        val playlist = diplayedList[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int = diplayedList.size

}
