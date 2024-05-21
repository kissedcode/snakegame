package dev.kissed.util.common.logging

actual object DebugLog {
    actual fun d(msg: String) {
        System.out.println(msg)
    }
}