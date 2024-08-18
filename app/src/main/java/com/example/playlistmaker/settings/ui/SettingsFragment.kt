package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
            true
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        val themeSwitcher = view.findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = settingsViewModel.checkOnState()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.switchTheme(checked)
        }

        val shareButton = view.findViewById<ImageButton>(R.id.settings_share_button)
        shareButton.setOnClickListener {
            settingsViewModel.openShareMenu(getString(R.string.share_link))
        }

        val supportButton = view.findViewById<ImageButton>(R.id.settings_support_button)

        supportButton.setOnClickListener {
            settingsViewModel.sendEmail(
                getString(R.string.email_recipient),
                getString(R.string.email_subject),
                getString(R.string.email_message)
            )

        }

        val EULAButton = view.findViewById<ImageButton>(R.id.EULA_button)

        EULAButton.setOnClickListener {
            settingsViewModel.openURL(getString(R.string.EULA_link))
        }

        return view
    }


}
