package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.MediaPlayerState
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.ui.SearchFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlayerFragment : Fragment() {

    private var playButtonPressed = false
    private lateinit var playAndPauseButton: ImageButton
    private lateinit var playTimer: TextView
    private var updatePlayTimeHandler: Handler? = null
    private var updatePlayTimeRunnable: Runnable? = null
    private lateinit var selectedTrack: Track
    val playerViewModel: PlayerViewModel by viewModel()
    private lateinit var view: View

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
            true
        }
    }


    companion object {
        fun newInstance() = PlayerFragment()
    }

    override fun onStop() {
        super.onStop()

        if (!selectedTrack.previewUrl.isNullOrEmpty()) {

            playerViewModel.pausePlayer()
            playButtonPressed = false
            playAndPauseButton.setImageResource(R.drawable.play_button)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        backPressedCallback.remove()

        if (!selectedTrack.previewUrl.isNullOrEmpty()) {
            playerViewModel.releasePlayer()
            updatePlayTimeHandler?.removeCallbacksAndMessages(null)
        }
        playerViewModel.stateLiveData.removeObserver(Observer {
            updateUi()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.fragment_player, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        val trackFromExtra: Track? = arguments?.getParcelable<Track>(Constants.PARCELABLE_TO_PLAYER_KEY)
        if (trackFromExtra != null) selectedTrack = trackFromExtra
        else returnToSearch()


        playerViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            updateUi()
        })


        initializeMediaPlayer()

        setArtworkThruGlide()

        val trackName = view.findViewById<TextView>(R.id.trackName)
        trackName.text = selectedTrack.trackName

        val artistName = view.findViewById<TextView>(R.id.artistName)
        artistName.text = selectedTrack.artistName

        val playlistAddButton = view.findViewById<ImageButton>(R.id.playlistAddButton)
        playlistAddButton.setOnClickListener {
            //TODO
        }

        playAndPauseButton = view.findViewById(R.id.playAndPauseButton)
        playTimer = view.findViewById<TextView>(R.id.playTimer)

        setupPlayTimeHandler()

        playAndPauseButton.setOnClickListener {
            if (!selectedTrack.previewUrl.isNullOrEmpty()) {

                playButtonPressed = !playButtonPressed
                if (playButtonPressed) {
                    updatePlayTimeHandler?.removeCallbacksAndMessages(null)
                    updatePlayTimeHandler?.postDelayed(updatePlayTimeRunnable!!, 350)
                } else {
                    updatePlayTimeHandler?.removeCallbacksAndMessages(null)
                }

                playerViewModel.playbackControl()
                playerViewModel.updateState()
            } else {
                Toast.makeText(requireContext(), "Для трека нет превью...", Toast.LENGTH_SHORT).show()
            }//В задании/макете про этот случай ни слова
        }


        val favoriteButton = view.findViewById<ImageButton>(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            //TODO
        }


        val trackTime = view.findViewById<TextView>(R.id.trackTime)
        trackTime.text = selectedTrack.trackTime

        val collectionName = view.findViewById<TextView>(R.id.collectionName)
        collectionName.text = selectedTrack.collectionName

        val collectionNameField = view.findViewById<TextView>(R.id.collectionNameField)
        if (selectedTrack.collectionName == "" || selectedTrack.collectionName == null) {
            collectionName.visibility = View.GONE
            collectionNameField.visibility = View.GONE
        }

        val releaseDate = view.findViewById<TextView>(R.id.releaseDate)
        releaseDate.text = selectedTrack.releaseDate

        val primaryGenreName = view.findViewById<TextView>(R.id.primaryGenreName)
        primaryGenreName.text = selectedTrack.primaryGenreName

        val country = view.findViewById<TextView>(R.id.country)
        country.text = selectedTrack.country

        val backButton = view.findViewById<View>(R.id.backButton)

        backButton.setOnClickListener {
            returnToSearch()
        }

        return view
    }

    private fun initializeMediaPlayer() {
        playerViewModel.preparePlayer(selectedTrack.previewUrl ?: "")
        playerViewModel.updateState()
    }

    private fun setArtworkThruGlide(): ImageView {
        val artworkView = view.findViewById<ImageView>(R.id.artwork)
        val roundingRadius = artworkView.resources.getDimension(R.dimen.Artwork_rounding)
        val pixelsForRoundedCorners = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            roundingRadius,
            artworkView.resources.displayMetrics
        )
        Glide.with(artworkView)
            .load(selectedTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(pixelsForRoundedCorners.toInt()))
            .into(artworkView)
        return artworkView
    }

    private fun setupPlayTimeHandler() {
        updatePlayTimeHandler = Handler(Looper.getMainLooper())

        updatePlayTimeRunnable = Runnable {

            playTimer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(playerViewModel.getCurrentPosition())

            if (playButtonPressed && !playerViewModel.getIsPlaying()) {

                playTimer.text = "00:00"

                playerViewModel.setPlayerState(MediaPlayerState.PREPARED)

                playerViewModel.updateState()

                updateUi()

                updatePlayTimeHandler?.removeCallbacksAndMessages(null)

                return@Runnable
            }

            updatePlayTimeHandler?.postDelayed(updatePlayTimeRunnable!!, 350)

            return@Runnable
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

    private fun returnToSearch(){
        findNavController().navigate(R.id.action_navigation_player_back_to_search,
            )
    }

}
