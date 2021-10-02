package com.example.onik.view

import android.Manifest
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.example.onik.R
import com.example.onik.viewmodel.MainBroadcastReceiver
import com.example.onik.viewmodel.Settings


class MainActivity : AppCompatActivity() {

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> runContactFragment()

                ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS) -> {
                    Toast.makeText(this, "НЕЛЬЗЯ ПОКАЗАТЬ КОНТАКТЫ. ВЫ НЕ ДАЛИ РАЗРЕШЕНИЕ",
                        Toast.LENGTH_LONG).show()
                }

                else -> {
                    Toast.makeText(this, "ВЫ НЕ ДАЛИ ДОСТУП К КОНТАКТАМ", Toast.LENGTH_LONG).show()
                }
            }
        }

    private val permissionGeoResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> runMapsFragment()

                ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(this, "Для использования геолокации предоставьте разрешение!",
                        Toast.LENGTH_LONG).show()
                }

                else -> {
                    Toast.makeText(this, "ВЫ НЕ ДАЛИ ДОСТУП К ГЕОДОКАЦИИ", Toast.LENGTH_LONG).show()
                }
            }
        }

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

        val searchText: SearchView? =
            menu?.findItem(R.id.action_search)?.actionView as SearchView?
        searchText?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container,
                        MoviesSearchFragment.newInstance(Bundle().apply {
                            putString(MoviesSearchFragment.BUNDLE_SEARCH_QUERY_EXTRA, query)
                        }))
                    .addToBackStack(null)
                    .commit()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, MySettingsFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.action_show_favorites -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, MoviesFavoritesFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.action_contacts -> {
                permissionResult.launch(Manifest.permission.READ_CONTACTS)
                true
            }
            R.id.action_maps -> {
                permissionGeoResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun runContactFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, ContentProviderFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun runMapsFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, GoogleMapsFragment())
            .addToBackStack(null)
            .commit()
    }
}