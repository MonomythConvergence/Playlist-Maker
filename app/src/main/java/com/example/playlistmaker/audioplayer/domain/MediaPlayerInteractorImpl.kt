package com.example.playlistmaker.audioplayer.domain

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {
    private lateinit var mediaPlayerListener: MediaPlayerListener



    override fun setListener(mediaPlayerListener: MediaPlayerListener) {
        if (mediaPlayerListener != null) {
            this.mediaPlayerListener = mediaPlayerListener
         } else {
            return
        }
    }

    override fun preparePlayer() {
        mediaPlayerRepository.preparePlayer()
        mediaPlayerListener.onPlayerPrepared() }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        mediaPlayerRepository.releasePlayer()
    }

    override fun playbackControl() {
        mediaPlayerRepository.playbackControl()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun getDuration(): Int {
        return mediaPlayerRepository.getDuration()
    }

    override fun getIsPlaying(): Boolean {
        return mediaPlayerRepository.getIsPlaying()
    }

    override fun getPlayerState(): MediaPlayerState {
        return mediaPlayerRepository.mediaPlayerState
    }

    override fun setPlayerState(mediaPlayerState : MediaPlayerState) {
        mediaPlayerRepository.setPlayerState(mediaPlayerState)
    }

}