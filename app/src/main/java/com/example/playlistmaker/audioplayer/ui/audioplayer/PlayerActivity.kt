package com.example.playlistmaker.audioplayer.ui.audioplayer


import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.domain.MediaPlayerState
import com.example.playlistmaker.audioplayer.domain.MediaPlayerListener
import com.example.playlistmaker.audioplayer.domain.datamodels.Track
import java.util.Locale
import com.example.playlistmaker.App
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor


@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity(), MediaPlayerListener {


    private lateinit var mediaPlayer: MediaPlayerInteractor
    lateinit var selectedTrack: Track
    private var playButtonPressed = false
    lateinit var playAndPauseButton: ImageButton
    private var updatePlayTimeHandler: Handler? = null


    //override fun onSaveInstanceState(outState: Bundle) {
    //    super.onSaveInstanceState(outState)}

    //override fun onRestoreInstanceState(savedInstanceState: Bundle) {}

    override fun onPause() {
        super.onPause()
        if (!selectedTrack.previewUrl.isNullOrEmpty()) {
            mediaPlayer.pausePlayer()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (!selectedTrack.previewUrl.isNullOrEmpty()) {
            mediaPlayer.releasePlayer()
            updatePlayTimeHandler?.removeCallbacksAndMessages(null)
        }
    }

    override fun onResume() {
        super.onResume()
        if (playButtonPressed) {
            playButtonPressed = false
            playAndPauseButton.setImageResource(R.drawable.play_button)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val app = applicationContext as App
        if (intent.getParcelableExtra<Track>(Constants.PARCELABLE_TO_PLAYER_KEY) != null) {
            selectedTrack = intent.getParcelableExtra(Constants.PARCELABLE_TO_PLAYER_KEY)!!
            if (!selectedTrack.previewUrl.isNullOrEmpty()){
                app.initializeMediaPlayerinstances(selectedTrack.previewUrl!!)}
            else{finish()}
            mediaPlayer = app.mediaPlayerInteractor
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

        if (playButtonPressed) {
            playAndPauseButton.setImageResource(R.drawable.pause_button)
        } else {
            playAndPauseButton.setImageResource(R.drawable.play_button)
        }

        mediaPlayer.setListener(this)
        mediaPlayer.preparePlayer()

        updatePlayTimeHandler = Handler(Looper.getMainLooper())

        val updatePlayTimeRunnable = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                playTimer.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.getCurrentPosition())


                val playtimeDurationDiscrepancyAllowance = 5L
                if (!mediaPlayer.getIsPlaying() && mediaPlayer.getCurrentPosition() >= (mediaPlayer.getDuration() - playtimeDurationDiscrepancyAllowance)) { //У некоторых песен обман с объявленной
                    //длительностью и ей же по-факту. Playback идёт до конца, для юзера разница при смене иконки с опережением 5мс глазу не заметна
                    playButtonPressed = false
                    playTimer.text = "00:00"
                    onPlayerCompleted()
                    return
                }
                updatePlayTimeHandler?.postDelayed(this, 350)
            }
        }


        if (selectedTrack.previewUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Для трека нет превью...", Toast.LENGTH_SHORT).show()
        }




        playAndPauseButton.setOnClickListener() {
            if (!selectedTrack.previewUrl.isNullOrEmpty()) {
                mediaPlayer.playbackControl()
                if (!playButtonPressed) {
                    playButtonPressed = true
                    playAndPauseButton.setImageResource(R.drawable.pause_button)
                    updatePlayTimeHandler?.removeCallbacksAndMessages(null)
                    updatePlayTimeHandler?.postDelayed(updatePlayTimeRunnable, 350)


                } else {
                    playButtonPressed = false
                    playAndPauseButton.setImageResource(R.drawable.play_button)
                    updatePlayTimeHandler?.removeCallbacksAndMessages(null)
                }
            } else {
                Toast.makeText(this, "Для трека нет превью...", Toast.LENGTH_SHORT).show()
            }
            //В задании/макете про этот случай ни слова

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

    override fun onPlayerPrepared() {
        playAndPauseButton.isEnabled = true
        mediaPlayer.setPlayerState(MediaPlayerState.PREPARED)

    }

    override fun onPlayerCompleted() {
        mediaPlayer.setPlayerState(MediaPlayerState.PREPARED)
        playAndPauseButton.setImageResource(R.drawable.play_button)
    }


}


