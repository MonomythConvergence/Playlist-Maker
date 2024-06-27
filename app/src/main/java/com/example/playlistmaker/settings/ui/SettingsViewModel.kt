package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.ThemeSwitchRepository
import com.example.playlistmaker.sharing.domain.ExternalInteractionHandler

class SettingsViewModel (private val themeSwitchRepository: ThemeSwitchRepository, private val externalInteractionHandler : ExternalInteractionHandler) : ViewModel() {

    fun openURL(url : String){
        externalInteractionHandler.openURL(url)
    }
    fun sendEmail(address : String, subject: String, content : String){
        externalInteractionHandler.sendEmail(address, subject, content)
    }
    fun openShareMenu(shareLink : String){
        externalInteractionHandler.openShareMenu(shareLink)
    }

    fun switchTheme(switchStateOn: Boolean) {
        themeSwitchRepository.switchTheme(switchStateOn)
    }

    fun checkOnState(): Boolean{

        return themeSwitchRepository.checkOnState()
    }
}