package com.example.myplayerapp.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayerapp.R
import com.example.myplayerapp.databinding.TrackItemBinding
import com.example.myplayerapp.dto.Track
import com.example.myplayerapp.observers.MediaLifecycleObserver
import com.example.myplayerapp.viewModel.MainViewModel
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable


class TrackViewHolder(
    private val mediaObserver: MediaLifecycleObserver,
    private val binding: TrackItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.apply {
            trackName.text = track.file
            stateButton.setOnClickListener {
                val url = "${MainViewModel.BASE_URL}${track.file}"

                if (mediaObserver.currentUrl.isNotBlank() && mediaObserver.currentUrl != url) {
                    mediaObserver.onStop()
                }

                if (mediaObserver.player == null || mediaObserver.player?.isPlaying!!) {
                    mediaObserver?.onPause()
                } else {
                    if (mediaObserver.currentUrl != url) {
                        mediaObserver.apply {
                            player?.setDataSource(url)
                            player?.prepare()
                            currentUrl = url
                            trackButton = stateButton
                        }.play()
                    } else
                        mediaObserver.play()
                }
            }

            val url = "${MainViewModel.BASE_URL}${track.file}"
            if (mediaObserver.currentUrl.isBlank()) {
                mediaObserver.apply {
                    player?.setDataSource(url)
                    player?.prepare()
                    trackButton = stateButton
                    currentUrl = url
                }
            }
        }
    }


}