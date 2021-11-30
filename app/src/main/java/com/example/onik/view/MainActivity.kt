package com.example.onik.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.onik.R
import com.example.onik.services.PUSH_KEY_ID_MOVIE
import com.example.onik.viewmodel.MainBroadcastReceiver


class MainActivity : AppCompatActivity() {

    private val receiver: MainBroadcastReceiver by lazy { MainBroadcastReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContentView(R.layout.main_activity)
        initToolbar()

        intent.extras?.let {
            it.getString(PUSH_KEY_ID_MOVIE)?.let { idMovie ->
                try {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MovieFragment.newInstance(Bundle().apply {
                            putInt(MovieFragment.BUNDLE_EXTRA, idMovie.toInt())
                        }))
                        .addToBackStack(null)
                        .commit()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        }

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
        menuInflater.inflate(R.menu.menu, menu)

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
            R.id.action_home -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, MainFragment.newInstance())
                    .commit()
                true
            }
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
}