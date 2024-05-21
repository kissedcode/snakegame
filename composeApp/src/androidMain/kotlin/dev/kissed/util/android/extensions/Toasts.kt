package dev.kissed.util.android.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread

@MainThread
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}