package com.example.myplayerapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import com.example.myplayerapp.R
import com.example.myplayerapp.adapter.TrackAdapter
import com.example.myplayerapp.databinding.ActivityMainBinding
import com.example.myplayerapp.observers.MediaLifecycleObserver
import com.example.myplayerapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mediaObserver: MediaLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaObserver = MediaLifecycleObserver(binding.root.context)

        val adapter = TrackAdapter(mediaObserver)
        binding.tracksRecyclerView.adapter = adapter
        viewModel.data.observe(this) { state ->
            adapter.submitList(state.album.tracks)
            binding.albumTitle.text = state.album.title
            binding.artistNameTextView.text = state.album.artist
            binding.publishedTextView.text = state.album.published
            binding.genreTextView.text = state.album.genre
        }

        binding.bigStateButton.setOnClickListener {
            if (mediaObserver.player != null) {
                var a = mediaObserver
                if (mediaObserver.player!!.isPlaying) {
                    mediaObserver.onPause()
                } else {
                    mediaObserver.play()
                }
            }
        }
        mediaObserver.bigButton = binding.bigStateButton

        lifecycle.addObserver(mediaObserver)
    }
}