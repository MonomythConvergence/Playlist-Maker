package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TrackClickCallback
import java.util.ArrayList

class RecyclerAdapter(
    private val diplayedList: List<Track>,
    private val viewModel: ViewModel,
    private val trackClickCallback: TrackClickCallback
) :

    RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result_item, parent, false)
        val holder = TrackViewHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val track = diplayedList[position]
                if (viewModel is SearchViewModel) {
                    viewModel.addTrackToRecent(track)
                }
                trackClickCallback.onClickCallback(track)
            }
        }

        holder.itemView.setOnLongClickListener {
            val position = holder.absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val track = diplayedList[position]
                trackClickCallback.onLongClickCallback(track)
            }
            true
        }

        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = diplayedList[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = diplayedList.size

}

