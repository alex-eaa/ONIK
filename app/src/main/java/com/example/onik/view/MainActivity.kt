package com.example.onik.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
        initToolbar()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }


    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}