package dev.kissed.util.common.logging

import android.util.Log

actual object DebugLog {
    actual fun d(msg: String) {
        Log.d("DEBUG", msg)
    }
}