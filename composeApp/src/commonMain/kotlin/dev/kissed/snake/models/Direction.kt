package dev.kissed.snake.models

enum class Direction(val isHorizontal: Boolean) {
    LEFT(isHorizontal = true),
    UP(isHorizontal = false),
    RIGHT(isHorizontal = true), 
    DOWN(isHorizontal = false)
}