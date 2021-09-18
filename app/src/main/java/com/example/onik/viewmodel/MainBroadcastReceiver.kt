package com.example.onik.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast


class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val connectionState = when (cm.activeNetworkInfo) {
            null -> "Not connected"
            else -> "Connected"
        }
        Log.d("Connectivity", "connectionState = $connectionState")

        Toast.makeText(context, connectionState, Toast.LENGTH_LONG).show()

    }
}