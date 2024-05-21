package dev.kissed.snake.game

import dev.kissed.snake.models.Direction
import dev.kissed.snake.models.Point
import kotlinx.coroutines.flow.StateFlow

interface GameFeature {
    val states: StateFlow<GameState>

    data class GameState(
        val isRunning: Boolean = false,
        val pace: Long = 300, // ms per tick
        val size: Int = 20,
        val snake: List<Point> = ArrayList(
            Point(0, 0).let { start ->
                ((INITIAL_SNAKE_SIZE - 1) downTo 0).map { start.copy(x = it) }
            }
        ),
        val food: Point = Point(size / 2, size / 2),
        val direction: Direction = Direction.RIGHT,
        val score: Int = 0,
        val tick: Int = 0,
    ) {
        val field: List<List<GameItemState>> by lazy {
            (0 until size).map { y ->
                ArrayList(
                    (0 until size).map { x ->
                        GameItemState(
                            x = x,
                            y = y,
                            isSolid = Point(x, y).let { point -> point in snake || point == food },
                        )
                    }
                )
            }
        }

        data class GameItemState(
            val x: Int,
            val y: Int,
            val isSolid: Boolean,
        )
    }

    sealed interface Action {
        data class ChangeDirection(val direction: Direction) : Action
        data object Play: Action
        data object Pause: Action
    }

    fun dispatch(action: Action)

    companion object {
        private const val INITIAL_SNAKE_SIZE = 2
    }
}