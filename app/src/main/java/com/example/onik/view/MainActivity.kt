package com.example.onik.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.onik.R
import com.example.onik.viewmodel.MainBroadcastReceiver
import com.example.onik.viewmodel.Settings

class MainActivity : AppCompatActivity() {

    private val receiver: MainBroadcastReceiver by lazy { MainBroadcastReceiver() }

    private val settings: Settings by lazy { Settings(baseContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        Log.d("MainActivity", settings.param1.toString())
        Log.d("MainActivity", settings.param2.toString())
        settings.param3?.let { Log.d("MainActivity", it) }

        settings.param1 = "ZZZAAA"
        settings.param2 = 100333
        settings.param3 = "Save PARAM3: VN4585"


        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}