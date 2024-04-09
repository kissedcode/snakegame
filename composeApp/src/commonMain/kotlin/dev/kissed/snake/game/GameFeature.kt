package dev.kissed.snake.game

import dev.kissed.snake.models.Direction
import dev.kissed.snake.models.Point
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameFeature {

    private val _states = MutableStateFlow(GameState())
    val states: StateFlow<GameState> = _states
    var state: GameState
        get() = _states.value
        private set(value) {
            _states.value = value
        }

    data class GameState(
        val isRunning: Boolean = true,
        val pace: Long = 300, // ms per tick
        val size: Int = 20,
        val snake: List<Point> = ArrayList(
            Point(0, 0).let { start ->
                (0..INITIAL_SNAKE_SIZE).map { start.copy(x = it) }
            }
        ),
        val food: Point = Point(size / 2, size / 2),
        val direction: Direction = Direction.RIGHT,
        val score: Int = 0,
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
    }

    init {
        GlobalScope.launch {
            while (true) {
                delay(state.pace)
                tick()
            }
        }
    }

    private fun tick() {
        if (!state.isRunning) return

        val oldSnake = state.snake
        val oldSnakeHead = oldSnake[0]

        val newSnakeHead = when (state.direction) {
            Direction.LEFT -> oldSnakeHead.copy(x = (oldSnakeHead.x - 1).mod(state.size))
            Direction.UP -> oldSnakeHead.copy(y = (oldSnakeHead.y - 1).mod(state.size))
            Direction.RIGHT -> oldSnakeHead.copy(x = (oldSnakeHead.x + 1).mod(state.size))
            Direction.DOWN -> oldSnakeHead.copy(y = (oldSnakeHead.y + 1).mod(state.size))
        }
        val goal = newSnakeHead == state.food

        val newSnake = Array(size = if (goal) oldSnake.size + 1 else oldSnake.size) {
            oldSnake[(it - 1).coerceAtLeast(0)]
        }
            .also {
                it[0] = newSnakeHead
            }
            .toList()

        val newFood = if (goal) {
            var foodCandidate: Point
            do {
                foodCandidate = Point(x = Random.nextInt(state.size), y = Random.nextInt(state.size))
            } while (foodCandidate in newSnake)
            foodCandidate
        } else {
            state.food
        }
        
        val newScore = if (goal) state.score + 1 else state.score

        state = state.copy(
            snake = newSnake,
            food = newFood,
            score = newScore,
        )
    }

    fun dispatch(action: Action) {
        when (action) {
            is Action.ChangeDirection -> {
                if (action.direction.isHorizontal != state.direction.isHorizontal) {
                    state = state.copy(direction = action.direction)
                }
            }
        }
    }
    
    companion object {
        private const val INITIAL_SNAKE_SIZE = 2
    }
}