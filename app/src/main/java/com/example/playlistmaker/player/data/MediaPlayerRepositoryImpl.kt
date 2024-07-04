package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.domain.MediaPlayerState

class MediaPlayerRepositoryImpl():
    MediaPlayerRepository {
    private lateinit var  mediaPlayer : MediaPlayer
    private lateinit var previewUrl :String

    override var mediaPlayerState: MediaPlayerState = MediaPlayerState.IDLE


    override fun preparePlayer(url: String) {
        previewUrl=url
        if (previewUrl.isNotEmpty()) {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayerState=MediaPlayerState.PREPARED
    }}



    override fun startPlayer() {
        if (previewUrl.isNotEmpty()) {
            mediaPlayer.start()
            mediaPlayerState = MediaPlayerState.STARTED
        }
    }

    override fun pausePlayer() {
        if (previewUrl.isNotEmpty()) {
            mediaPlayer.pause()
            mediaPlayerState = MediaPlayerState.PAUSED
        }
    }

    override fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            mediaPlayer.release()
        }
    }

    override fun playbackControl() {
        if (previewUrl.isNotEmpty()) {
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

    override fun resetPlayer() {
        mediaPlayer.reset()
    }
}
