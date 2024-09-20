package com.example.playlistmaker.newPlaylistCreation.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.editPlaylist.domain.PathToBitmapConverterCallback
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.search.domain.Track
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.InputStream


class NewPlaylistFragment : Fragment() {

    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPress()
            true
        }
    }

    private lateinit var imageView: ImageView
    private lateinit var nameField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var confirmButton: Button


    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var preConfirmBitmap: Bitmap? = null
    private var preConfirmBitmapUri: Uri? = null

    private lateinit var fragmentTitle: TextView

    var modifiedEditPlaylist: Playlist = Playlist(-1, "", "", "", emptyList(), emptyList(), -1, -1)
    var unmodifiedEditPlaylist: Playlist =
        Playlist(-1, "", "", "", emptyList(), emptyList(), -1, -1)

    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bmp = uriToBitmap(requireContext(), uri)
                preConfirmBitmapUri = uri
                setAlbumImage(bmp)
            }

        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                openPhotoPicker()
            }
        }


    companion object {
        fun newInstance() = NewPlaylistFragment()
    }

    override fun onStop() {
        super.onStop()
        arguments?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_new_playlist_creation, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )



        setUpExitConfirmationPopUp()

        confirmButton = view.findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            if (!nameField.text.isNullOrEmpty()) {
                var filePath: String? = null
                Log.d("mytag","${preConfirmBitmap != null},${writePermissionCheck()},${preConfirmBitmapUri!=null},")//todo delete
                if (preConfirmBitmap != null && writePermissionCheck() && preConfirmBitmapUri!=null) {
                    filePath = newPlaylistViewModel.saveAlbumCover(
                        preConfirmBitmapUri!!,
                        nameField.text.toString()
                    )
                }
                Log.d("mytag","${arguments?.getString(Constants.SOURCE_FRAGMENT_KEY)}???")//todo delete
                when (arguments?.getString(Constants.SOURCE_FRAGMENT_KEY)) {

                    Constants.SOURCE_PLAYER -> {
                        Log.d("mytag","SOURCE_PLAYER?")//todo delete
                        newPlaylistViewModel.addNewPlaylist(
                            nameField.text.toString(),
                            descriptionField.text.toString(),
                            filePath
                        )
                        val trackFromExtra: Track? =
                            arguments?.getParcelable<Track>(Constants.PARCELABLE_TO_PLAYER_KEY_TRACK)
                        val args = Bundle()
                        args.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY_TRACK, trackFromExtra)
                        args.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_NEW_PLAYLIST)
                        findNavController().navigate(
                            R.id.action_navigation_new_playlist_to_player,
                            args
                        )
                    }

                    Constants.SOURCE_PLAYLIST -> {
                        Log.d("mytag","SOURCE_PLAYLIST?")//todo delete
                        newPlaylistViewModel.addNewPlaylist(
                            nameField.text.toString(),
                            descriptionField.text.toString(),
                            filePath
                        )
                        val arg = Bundle()
                        arg.putString(Constants.TITLE_TOAST_KEY, nameField.text.toString())
                        arg.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_NEW_PLAYLIST)
                        findNavController().navigate(
                            R.id.action_navigation_new_playlist_to_playlists,
                            arg
                        )
                    }

                    Constants.SOURCE_EDIT_PLAYLIST -> {
                        Log.d("mytag","SOURCE_EDIT_PLAYLIST?")//todo delete
                        modifiedEditPlaylist.playlistTitle = nameField.text.toString()
                        modifiedEditPlaylist.playlistDescriptor = descriptionField.text.toString()
                        if (preConfirmBitmapUri!=null){
                            modifiedEditPlaylist.coverImagePath = filePath ?: ""
                        }
                        newPlaylistViewModel.updatePlaylist(modifiedEditPlaylist)

                        val arg = Bundle()
                        arg.putParcelable(
                            Constants.PARCELABLE_NEW_PLAYLIST_TO_EDIT_PLAYLIST_KEY,
                            modifiedEditPlaylist
                        )
                        findNavController().navigate(
                            R.id.action_navigation_new_playlist_to_edit_playlist,arg
                        )
                    }
                }

            }
        }

        nameField = view.findViewById<EditText>(R.id.nameField)
        descriptionField = view.findViewById<EditText>(R.id.descriptionField)

        imageView = view.findViewById<ImageView>(R.id.image)
        imageView.setOnClickListener {
            if (imageView.background != null) {
                photoPicker()
            }

        }
        fragmentTitle = view.findViewById<TextView>(R.id.fragmentTitle)
        highlightsSetup()

        val backButton = view.findViewById<View>(R.id.backButton)

        backButton.setOnClickListener {
            backPress()
        }


        unmodifiedEditPlaylist =
            arguments?.getParcelable<Playlist>(Constants.PARCELABLE_PLAYLIST_TO_NEW_PLAYLIST_KEY)
                ?: unmodifiedEditPlaylist
        if (unmodifiedEditPlaylist.playlistTitle != "") {
            changeSetUpForModification(unmodifiedEditPlaylist)
            modifiedEditPlaylist = unmodifiedEditPlaylist
        }

        return view
    }

    private fun changeSetUpForModification(unmodifiedEditPlaylist: Playlist) {
        if (unmodifiedEditPlaylist.coverImagePath != "") {
            val bitmapCallback = object : PathToBitmapConverterCallback {
                override suspend fun convertPathToBitmap(bitmap: Any) {

                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap as Bitmap)
                    }
                    preConfirmBitmap = bitmap as Bitmap
                    Log.d("mytag", "Triggered! ${preConfirmBitmap}") //todo
                }
            }
            newPlaylistViewModel.getBitmap(unmodifiedEditPlaylist.coverImagePath, bitmapCallback)

        }

        descriptionField.text =
            Editable.Factory.getInstance().newEditable(unmodifiedEditPlaylist.playlistDescriptor)
        nameField.text =
            Editable.Factory.getInstance().newEditable(unmodifiedEditPlaylist.playlistTitle)
        fragmentTitle.text = getString(R.string.make_edits)
        confirmButton.text = getString(R.string.save)

    }


    private fun highlightsSetup() {
        nameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val targetBackground = if (s.isNullOrEmpty()) {
                    R.drawable.new_playlist_outline_bg_empty
                } else {
                    R.drawable.new_playlist_outline_bg_filled
                }

                if (nameField.background != getDrawable(requireContext(), targetBackground)) {
                    nameField.setBackgroundResource(targetBackground)
                }

                confirmButton.isActivated = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        descriptionField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val targetBackground = if (s.isNullOrEmpty()) {
                    R.drawable.new_playlist_outline_bg_empty
                } else {
                    R.drawable.new_playlist_outline_bg_filled
                }

                if (descriptionField.background != getDrawable(
                        requireContext(),
                        targetBackground
                    )
                ) {
                    descriptionField.setBackgroundResource(targetBackground)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun setUpExitConfirmationPopUp() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.exit_playlist_creation))
            .setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            .setNeutralButton(getString(R.string.cancel_button)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.exit_button)) { dialog, which ->
                popUp()
            }
    }

    private fun setAlbumImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
            imageView.background = null
            imageView.setPadding(0, 0, 0, 0)
            preConfirmBitmap = bitmap
        }

    }

    private fun photoPicker() {
        if (readPermissionCheck()
        ) {
            openPhotoPicker()
        }
    }

    private fun openPhotoPicker() {
        photoPickerLauncher.launch("image/*")
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }


    fun backPress() {
        if (preConfirmBitmap != null || !nameField.text.isNullOrEmpty() || !descriptionField.text.isNullOrEmpty()) {
            confirmDialog.show()
        } else {
            popUp()
        }
    }

    private fun popUp() {
        if (fragmentTitle.text != getString(R.string.new_playlist)) {
            val args = Bundle()
            args.putParcelable(
                Constants.PARCELABLE_NEW_PLAYLIST_TO_EDIT_PLAYLIST_KEY,
                unmodifiedEditPlaylist
            )
            args.putString(Constants.SOURCE_FRAGMENT_KEY,Constants.SOURCE_NEW_PLAYLIST)
            findNavController().navigate(R.id.action_navigation_new_playlist_to_edit_playlist,args)
            Log.d("mytag", "Triggered! ${unmodifiedEditPlaylist.playlistTitle}") //todo
            return
        }
        when (arguments?.getString(Constants.SOURCE_FRAGMENT_KEY)) {
            Constants.SOURCE_PLAYER -> {
                val trackFromExtra: Track? =
                    arguments?.getParcelable<Track>(Constants.PARCELABLE_TO_PLAYER_KEY_TRACK)
                val args = Bundle()
                args.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY_TRACK, trackFromExtra)
                args.putString(Constants.SOURCE_FRAGMENT_KEY, Constants.SOURCE_PLAYER)
                findNavController().navigate(R.id.action_navigation_new_playlist_to_player, args)

            }

            else -> {
                val bundle = Bundle()
                bundle.putString(Constants.NEW_PLAYLIST_CANCEL_KEY, Constants.CANCEL_KEY_ARG)
                findNavController().navigate(
                    R.id.action_navigation_new_playlist_to_playlists,
                    bundle
                )
            }
        }

    }


    private fun writePermissionCheck(): Boolean {
        when (Build.VERSION.SDK_INT) {
            0 - 28 -> {

                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    return (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                } else {
                    return true
                }
            }

            else -> {
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    return (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                } else {
                    return true
                }
            }
        }
    }

    private fun readPermissionCheck(): Boolean {
        when (Build.VERSION.SDK_INT) {
            0 - 28 -> {
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    return (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                } else {
                    return true
                }
            }

            else -> {
                if (checkPermission(Manifest.permission.READ_MEDIA_IMAGES)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    return (checkPermission(Manifest.permission.READ_MEDIA_IMAGES))
                } else {
                    return true
                }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }
}


