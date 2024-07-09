package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {

            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = settingsViewModel.checkOnState()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.switchTheme(checked)
        }

        val shareButton = findViewById<ImageButton>(R.id.settings_share_button)

        shareButton.setOnClickListener {
            settingsViewModel.openShareMenu(getString(R.string.share_link))
        }
        val supportButton = findViewById<ImageButton>(R.id.settings_support_button)

        supportButton.setOnClickListener {
            settingsViewModel.sendEmail(getString(R.string.email_recipient),
                getString(R.string.email_subject),
                getString(R.string.email_message))

        }
        val EULAButton = findViewById<ImageButton>(R.id.EULA_button)

        EULAButton.setOnClickListener {
            settingsViewModel.openURL(getString(R.string.EULA_link))
        }
    }
}