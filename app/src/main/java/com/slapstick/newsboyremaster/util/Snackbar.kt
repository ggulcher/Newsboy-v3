package com.slapstick.newsboyremaster.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbarAction(message: String, actionText: String, actionFunction: () -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        setAction(actionText) { actionFunction.invoke() }
        show()
    }
}