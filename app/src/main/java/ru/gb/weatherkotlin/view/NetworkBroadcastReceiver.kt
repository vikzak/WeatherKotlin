package ru.gb.weatherkotlin.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

class NetworkBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action
        StringBuilder().apply {
            append("NetworkBroadcastReceiver\n")
            append("Action: ${intent?.action}\n")
            intent?.extras?.let { (intent?.extras!![ConnectivityManager.EXTRA_NETWORK_INFO] as NetworkInfo)
                .also {
                    if (it.state == NetworkInfo.State.CONNECTED) {
                        append("Network ${it.typeName}") }
                }
                if (intent?.extras!!.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                    append("\nThere's no network connectivity") }
            }
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }

        }
    }
}
