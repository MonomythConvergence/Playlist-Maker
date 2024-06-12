package com.example.playlistmaker.search.ui


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R

import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import java.util.ArrayList

class SearchAdapter(val diplayedList: ArrayList<Track>, private val viewModel: SearchViewModel) :

    RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_result_item, parent, false)
        val holder = TrackViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val playerIntent = Intent(parent.context, PlayerActivity::class.java)
            playerIntent.putExtra(Constants.PARCELABLE_TO_PLAYER_KEY, diplayedList[position])
            if (position != RecyclerView.NO_POSITION) {
                val track = diplayedList[position]
                viewModel.addTrackToRecent(track)
                parent.context.startActivity(playerIntent)
            }

        }
        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = diplayedList[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = diplayedList.size

}

