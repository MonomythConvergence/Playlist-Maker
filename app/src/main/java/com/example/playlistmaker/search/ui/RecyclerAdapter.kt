package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.data.ItemClickCallback
import java.util.ArrayList

class RecyclerAdapter(
    private val diplayedList: ArrayList<Track>,
    private val viewModel: ViewModel,
    private val itemClickCallback: ItemClickCallback
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
                itemClickCallback.onClickCallback(track)
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

