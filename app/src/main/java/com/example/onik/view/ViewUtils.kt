package com.example.onik.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import com.example.onik.R
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showSnackbar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE,
) {

    Snackbar.make(this, text, length)
        .setAction(actionText) { action(this) }
        .show()

}

fun Snackbar.setDefaultText(): Snackbar = this.setText(R.string.msgNotDownloaded)

fun Snackbar.setDefaultActionText(action: (View) -> Unit): Snackbar = this.setAction(R.string.buttonReloadText, action)


fun View.hideKeyboard(): Boolean = try {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
} catch (e: RuntimeException) {
    false
}