package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.ExternalInteractionHandler

class ExternalInteractionHandlerImpl(private val context: Context) : ExternalInteractionHandler {
    override fun openURL(url: String) {
        val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        linkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(linkIntent)
    }

    override fun sendEmail(recipient : String, subject : String, body: String) {
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = Uri.parse("mailto:")
        sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sendIntent.putExtra(Intent.EXTRA_TEXT, body)
        sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(sendIntent)

    }

    override fun openShareMenu(content: String) {
        val shareBody = content
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.data = Uri.parse("sms:")
        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(smsIntent))
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }
}