package com.slapstick.newsboyremaster.util

import android.util.Log
import com.slapstick.newsboyremaster.BuildConfig

fun Any.log(message: String) {
    if (BuildConfig.DEBUG) Log.d(this.javaClass.name, "log: $message")
}