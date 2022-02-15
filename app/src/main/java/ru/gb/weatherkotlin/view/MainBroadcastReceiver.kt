package ru.gb.weatherkotlin.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MainBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        intent.action

        StringBuilder().apply {
            append("MainBroadcastReceiver\n")
            append("Action: ${intent.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        }

    }
}