package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import com.example.playlistmaker.App
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import com.example.playlistmaker.sharing.data.ExternalInteractionHandlerImpl
import com.example.playlistmaker.sharing.domain.ExternalInteractionHandler
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private val themeViewModel: ThemeViewModel by viewModels() {
        ThemeViewModelFactory(
            ThemeSwitchRepositoryImpl(this))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val externalInteractionHandler: ExternalInteractionHandler =
            ExternalInteractionHandlerImpl(this)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {

            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = themeViewModel.checkOnState()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            themeViewModel.switchTheme(checked)
        }

        val shareButton = findViewById<ImageButton>(R.id.settings_share_button)

        shareButton.setOnClickListener {
            externalInteractionHandler.openShareMenu(getString(R.string.share_link))
        }
        val supportButton = findViewById<ImageButton>(R.id.settings_support_button)

        supportButton.setOnClickListener {
            externalInteractionHandler.sendEmail(
                getString(R.string.email_recipient),
                getString(R.string.email_subject),
                getString(R.string.email_message)
            )
        }
        val EULAButton = findViewById<ImageButton>(R.id.EULA_button)

        EULAButton.setOnClickListener {
            externalInteractionHandler.openURL(getString(R.string.EULA_link))
        }
    }
}