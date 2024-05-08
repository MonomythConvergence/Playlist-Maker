package com.example.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import android.widget.ImageButton
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.domain.MediaPlayerListener
import com.example.playlistmaker.audioplayer.domain.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val url: String?):
    MediaPlayerRepository {
    private val mediaPlayer = MediaPlayer()


    var mediaPlayerState: MediaPlayerState = MediaPlayerState.IDLE
    val validURL = !url.isNullOrEmpty()

    override fun preparePlayer() {
        if (validURL) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        }
    }


    override fun startPlayer() {
        if (validURL) {
            mediaPlayer.start()
            mediaPlayerState = MediaPlayerState.STARTED
        }
    }

    override fun pausePlayer() {
        if (validURL) {
            mediaPlayer.pause()
            mediaPlayerState = MediaPlayerState.PAUSED
        }
    }

    override fun releasePlayer() {
        if (validURL) {
            mediaPlayer.release()
        }
    }

    override fun playbackControl() {
        if (validURL) {
            when (mediaPlayerState) {
                MediaPlayerState.STARTED, MediaPlayerState.PLAYBACK_COMPLETED -> {
                    pausePlayer()
                }

                MediaPlayerState.PREPARED, MediaPlayerState.PAUSED -> {
                    startPlayer()
                }

                else -> {}
            }
        }
    }
    override fun getCurrentPosition() : Int {
        return mediaPlayer.currentPosition
    }
    override fun getDuration() : Int {
        return mediaPlayer.duration
    }

    override fun getIsPlaying() : Boolean{
        return mediaPlayer.isPlaying
    }
}