package com.example.myplayerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myplayerapp.databinding.TrackItemBinding
import com.example.myplayerapp.dto.Track
import com.example.myplayerapp.observers.MediaLifecycleObserver

class TrackAdapter(private val mediaObserver: MediaLifecycleObserver)
    : ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(mediaObserver, binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }
}