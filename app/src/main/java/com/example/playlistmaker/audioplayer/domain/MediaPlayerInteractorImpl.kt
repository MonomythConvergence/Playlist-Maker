package com.example.playlistmaker.audioplayer.domain

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {
    private var mediaPlayerListener: MediaPlayerListener? = null
    override fun setListener(mediaPlayerListener: MediaPlayerListener) {
        this.mediaPlayerListener = mediaPlayerListener
    }

    override fun preparePlayer() {
        mediaPlayerRepository.preparePlayer()
        mediaPlayerListener?.onPlayerPrepared()
    }

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


}