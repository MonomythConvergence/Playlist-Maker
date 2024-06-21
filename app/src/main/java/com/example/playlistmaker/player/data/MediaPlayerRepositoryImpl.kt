package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.domain.MediaPlayerState

class MediaPlayerRepositoryImpl(private val url: String?):
    MediaPlayerRepository {
    private val mediaPlayer = MediaPlayer()


    override var mediaPlayerState: MediaPlayerState = MediaPlayerState.IDLE
    val validURL = !url.isNullOrEmpty()


    override fun preparePlayer() {
        if (validURL) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayerState=MediaPlayerState.PREPARED
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

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun getIsPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setPlayerState(mediaPlayerState : MediaPlayerState){
        this.mediaPlayerState =mediaPlayerState
    }
}