package com.example.onik.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                // TODO
                return true
            }
            R.id.action_main -> {
                // TODO
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchText: SearchView? = menu?.findItem(R.id.action_search)?.actionView as SearchView?
        searchText?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show();
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        return true
    }


    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}