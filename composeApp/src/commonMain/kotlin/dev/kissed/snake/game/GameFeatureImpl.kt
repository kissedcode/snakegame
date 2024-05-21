package dev.kissed.snake.game

import dev.kissed.snake.game.GameFeature.Action
import dev.kissed.snake.game.GameFeature.GameState
import dev.kissed.snake.models.Direction
import dev.kissed.snake.models.Point
import dev.kissed.util.common.logging.DebugLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random

internal class GameFeatureImpl : GameFeature {

    private val _states = MutableStateFlow(GameState())
    private var state: GameState
        get() = _states.value
        private set(value) {
            _states.value = value
        }

    override val states: StateFlow<GameState> = _states

    private var gameJob: Job = Job().also { it.complete() }

    private lateinit var scope: CoroutineScope
    
    fun run(scope: CoroutineScope) {
        this.scope = scope

        scope.launch {
            states
                .map { it.isRunning }
                .distinctUntilChanged()
                .collect {
                    if (it) resume() else pause()
                }
        }
        scope.launch {
            while (true) {
                delay(1000)
                DebugLog.d("game feature: i'm alive, ${Random.nextInt()}")
            }
        }
    }

    private fun resume() {
        gameJob = scope.launch {
            while (true) {
                delay(state.pace)
                tick()
            }
        }
    }

    private fun pause() {
        gameJob.cancel()
    }

    private fun tick() {
        DebugLog.d("tick ${state.tick}")
        
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
            tick = state.tick + 1,
        )
    }

    override fun dispatch(action: Action) {
        when (action) {
            is Action.ChangeDirection -> {
                if (action.direction.isHorizontal != state.direction.isHorizontal) {
                    state = state.copy(direction = action.direction)
                }
            }

            Action.Play -> {
                state = state.copy(isRunning = true)
            }

            Action.Pause -> {
                state = state.copy(isRunning = false)
            }
        }
    }
}