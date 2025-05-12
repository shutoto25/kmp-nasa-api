package org.example.project.core.util

import android.util.Log

internal actual fun platformLog(level: String, tag: String, message: String) {
    when (level) {
        "VERBOSE" -> Log.v(tag, message)
        "DEBUG" -> Log.d(tag, message)
        "INFO" -> Log.i(tag, message)
        "WARN" -> Log.w(tag, message)
        "ERROR" -> Log.e(tag, message)
        else -> Log.i(tag, message)
    }
}