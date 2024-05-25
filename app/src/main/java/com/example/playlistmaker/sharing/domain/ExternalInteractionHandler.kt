package com.example.playlistmaker.sharing.domain

interface ExternalInteractionHandler {
fun openURL(url : String)
fun sendEmail(recipient : String, subject : String, body: String)
fun openShareMenu(content: String)
}