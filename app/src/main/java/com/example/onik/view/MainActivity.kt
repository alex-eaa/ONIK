package com.example.onik.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.onik.R
import com.example.onik.viewmodel.Settings

class MainActivity : AppCompatActivity() {

    private val settings: Settings by lazy { Settings(baseContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", settings.param1.toString())
        Log.d("MainActivity", settings.param2.toString())
        settings.param3?.let { Log.d("MainActivity", it) }

        settings.param1 = "ZZZAAA"
        settings.param2 = 100333
        settings.param3 ="Save PARAM3: VN4585"


        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}