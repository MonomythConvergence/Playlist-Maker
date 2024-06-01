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

class SearchAdapter(diplayedList: ArrayList<Track>, activity: SearchActivity) :


    RecyclerView.Adapter<TrackViewHolder>() {
    val list = diplayedList
    val activityInstance = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_result_item, parent, false)
        val holder = TrackViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val playerIntent = Intent(activityInstance, PlayerActivity::class.java)
            playerIntent.putExtra(Constants.PARCELABLE_TO_PLAYER_KEY, list[position])
            if (position != RecyclerView.NO_POSITION) {
                val track = list[position]
                SearchHistory(App.recentTracksSharedPreferences).addTrackToRecent(track)
                Log.d("mydebug", "player intent is gonna start")
                activityInstance.startActivity(playerIntent)
            }

        }
        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = list[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = list.size

}

