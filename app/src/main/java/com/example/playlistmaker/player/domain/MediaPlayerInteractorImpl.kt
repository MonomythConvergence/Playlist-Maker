package com.example.playlistmaker.player.domain


class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {


    override  fun preparePlayer(url: String){
        mediaPlayerRepository.preparePlayer(url)
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

    override fun getPlayerState(): MediaPlayerState {
        return mediaPlayerRepository.mediaPlayerState
    }

    override fun setPlayerState(mediaPlayerState : MediaPlayerState) {
        mediaPlayerRepository.setPlayerState(mediaPlayerState)
    }

    override fun resetPlayer() {
        mediaPlayerRepository.resetPlayer()
    }

}