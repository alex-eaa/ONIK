package com.example.onik.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Settings(context: Context) {

    private val prefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    var param1 by prefs.string()
    var param2 by prefs.int()
    var param3 by prefs.string(
        key = { "KEY_PARAM3" },
        defaultValue = "default"
    )


    private fun SharedPreferences.string(
        defaultValue: String? = null,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, String?> =
        object : ReadWriteProperty<Any, String?> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = getString(key(property), defaultValue)

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: String?
            ) = edit().putString(key(property), value).apply()
        }

    private fun SharedPreferences.int(
        defaultValue: Int = 0,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Int> =
        object : ReadWriteProperty<Any, Int> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = getInt(key(property), defaultValue)

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Int
            ) = edit().putInt(key(property), value).apply()
        }

}