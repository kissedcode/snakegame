package dev.kissed.snake.util.logging

actual object DebugLog {
    actual fun d(msg: String) {
        System.out.println(msg)
    }
}