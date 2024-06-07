package com.example.playlistmaker.player.ui


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.datamodels.Track
import java.util.Locale
import com.example.playlistmaker.App
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerState

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity() {


    private lateinit var mediaPlayer: MediaPlayerInteractor
    lateinit var selectedTrack: Track
    var playButtonPressed = false
    lateinit var playAndPauseButton: ImageButton
    private var updatePlayTimeHandler: Handler? = null
    private var updatePlayTimeRunnable: Runnable? = null
    private lateinit var app: App
    private val playerViewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(
            PlayerViewModel::class.java
        )
    }

    override fun onPause() {
        super.onPause()
    }



    override fun onStop() {
        super.onStop()
        if (!selectedTrack.previewUrl.isNullOrEmpty()) {
            mediaPlayer.pausePlayer()
            playButtonPressed = false
            playAndPauseButton.setImageResource(R.drawable.play_button)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (!selectedTrack.previewUrl.isNullOrEmpty()) {
            mediaPlayer.releasePlayer()
            updatePlayTimeHandler?.removeCallbacksAndMessages(null)
        }
        playerViewModel.stateLiveData.removeObserver(Observer {
            updateUi()
        })
    }

    //override fun onResume() {super.onResume()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerViewModel.stateLiveData.observe(this, Observer {
            updateUi()
        })


        app = application as App

        if (intent.getParcelableExtra<Track>(Constants.PARCELABLE_TO_PLAYER_KEY) != null) {
            selectedTrack = intent.getParcelableExtra(Constants.PARCELABLE_TO_PLAYER_KEY)!!
            if (!selectedTrack.previewUrl.isNullOrEmpty()) {
                app.initializeMediaPlayerinstances(selectedTrack.previewUrl!!)
            } else {
                finish()
            }
            mediaPlayer = app.giveMediaPlayerInteractor()
        } else {
            finish()
        }

        val artwork = findViewById<ImageView>(R.id.artwork)
        val roundingRadius = artwork.resources.getDimension(R.dimen.Artwork_rounding)
        val pixelsForRoundedCorners = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            roundingRadius,
            artwork.resources.displayMetrics
        )
        Glide.with(artwork)
            .load(selectedTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(pixelsForRoundedCorners.toInt()))
            .into(artwork)

        val trackName = findViewById<TextView>(R.id.trackName)
        trackName.text = selectedTrack.trackName

        val artistName = findViewById<TextView>(R.id.artistName)
        artistName.text = selectedTrack.artistName

        val playlistAddButton = findViewById<ImageButton>(R.id.playlistAddButton)
        playlistAddButton.setOnClickListener {
            //TODO
        }

        playAndPauseButton = findViewById(R.id.playAndPauseButton)
        val playTimer = findViewById<TextView>(R.id.playTimer)

        mediaPlayer.preparePlayer()

        playerViewModel.updateState(mediaPlayer.getPlayerState())

        updatePlayTimeHandler = Handler(Looper.getMainLooper())


        updatePlayTimeRunnable = Runnable {

            playTimer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.getCurrentPosition())

            if (playButtonPressed && !mediaPlayer.getIsPlaying()) {

                playTimer.text = "00:00"

                mediaPlayer.setPlayerState(MediaPlayerState.PREPARED)

                playerViewModel.updateState(mediaPlayer.getPlayerState())

                updateUi()

                updatePlayTimeHandler?.removeCallbacksAndMessages(null)

                return@Runnable
            }

            updatePlayTimeHandler?.postDelayed(updatePlayTimeRunnable!!, 350)

            return@Runnable
        }

        playAndPauseButton.setOnClickListener {
            if (!selectedTrack.previewUrl.isNullOrEmpty()) {

                playButtonPressed = !playButtonPressed
                if (playButtonPressed) {
                    updatePlayTimeHandler?.removeCallbacksAndMessages(null)
                    updatePlayTimeHandler?.postDelayed(updatePlayTimeRunnable!!, 350)
                } else {
                    updatePlayTimeHandler?.removeCallbacksAndMessages(null)
                }

                mediaPlayer.playbackControl()
                playerViewModel.updateState(mediaPlayer.getPlayerState())
            } else {
                Toast.makeText(this, "Для трека нет превью...", Toast.LENGTH_SHORT).show()
            }//В задании/макете про этот случай ни слова
        }


        val favoriteButton = findViewById<ImageButton>(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            //TODO
        }


        val trackTime = findViewById<TextView>(R.id.trackTime)
        trackTime.text = selectedTrack.trackTime

        val collectionName = findViewById<TextView>(R.id.collectionName)
        collectionName.text = selectedTrack.collectionName
        val collectionNameField = findViewById<TextView>(R.id.collectionNameField)
        if (selectedTrack.collectionName == "" || selectedTrack.collectionName == null) {
            collectionName.visibility = View.GONE
            collectionNameField.visibility = View.GONE
        }

        val releaseDate = findViewById<TextView>(R.id.releaseDate)
        releaseDate.text = selectedTrack.releaseDate
        val primaryGenreName = findViewById<TextView>(R.id.primaryGenreName)
        primaryGenreName.text = selectedTrack.primaryGenreName

        val country = findViewById<TextView>(R.id.country)
        country.text = selectedTrack.country


        val backButton = findViewById<View>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }


    }

    private fun updateUi() {

        when (playerViewModel.stateLiveData.value) {
            MediaPlayerState.PREPARED -> {
                playAndPauseButton.setImageResource(R.drawable.play_button)
                playButtonPressed = false
            }

            MediaPlayerState.STARTED -> {
                playAndPauseButton.setImageResource(R.drawable.pause_button)
                playButtonPressed = true
            }

            MediaPlayerState.PAUSED -> {
                playAndPauseButton.setImageResource(R.drawable.play_button)
                playButtonPressed = false
            }

            MediaPlayerState.ERROR -> {
                //TODO?
            }

            else -> {}
        }
    }
}