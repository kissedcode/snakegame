package dev.kissed.snake.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.kissed.snake.game.GameFeature.GameState
import dev.kissed.snake.models.Direction
import dev.kissed.snake.ui.AppColors
import kotlin.math.abs

@Composable
fun GameFeatureUI(feature: GameFeature) {
    val state = feature.states.collectAsState()
    Box(
        Modifier
            .fillMaxSize()
            .background(AppColors.gameBackground)
            .padding(16.dp)
            .focusTarget()
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.DirectionDown -> Direction.DOWN 
                        Key.DirectionUp -> Direction.UP 
                        Key.DirectionRight -> Direction.RIGHT 
                        Key.DirectionLeft -> Direction.LEFT
                        else -> null
                    }?.let {
                        feature.dispatch(GameFeature.Action.ChangeDirection(it))
                        return@onKeyEvent true
                    }
                }
                return@onKeyEvent false
            }
            .pointerInput(Unit) {
//                val k = size.height.toFloat() / size.width.toFloat()
//                detectTapGestures { tap ->
//                    val lineOne = tap.y - tap.x * k
//                    val lineTwo = tap.y - (-tap.x * k + size.height)
//                    val tapDirection = when {
//                        lineOne < 0 && lineTwo < 0 -> Direction.UP
//                        lineOne < 0 && lineTwo >= 0 -> Direction.RIGHT
//                        lineOne >= 0 && lineTwo >= 0 -> Direction.DOWN
//                        lineOne >= 0 && lineTwo < 0 -> Direction.LEFT
//                        else -> error("impossible")
//                    }
//                    feature.dispatch(GameFeature.Action.ChangeDirection(tapDirection))
//                }
                awaitEachGesture { 
                    val down = awaitFirstDown()
                    var direction: Direction? = null
                    drag(down.id) {
                        val dx = it.position.x - it.previousPosition.x
                        val dy = it.position.y - it.previousPosition.y
                        
                        direction = if (abs(dx) > abs(dy)) {
                            if (dx > 0) Direction.RIGHT else Direction.LEFT
                        } else {
                            if (dy > 0) Direction.DOWN else Direction.UP
                        }
                    }
                    direction?.let { feature.dispatch(GameFeature.Action.ChangeDirection(it)) }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        GameFieldUI(state.value)
    }
    
    LaunchedEffect(Unit) { 
        feature.dispatch(GameFeature.Action.Play)
    }
}

@Composable
private fun GameFieldUI(state: GameState) {
    Column {
        Text("Score: ${state.score}")
        BoxWithConstraints() {
            Box(
                Modifier
                    .size(minOf(maxHeight, maxWidth))
                    .border(BorderStroke(2.dp, AppColors.gameBrush))
                    .padding(4.dp),
            ) {
                Column {
                    state.field.forEach { row ->
                        Row(
                            Modifier.weight(1f)
                        ) {
                            row.forEach { item ->
                                Box(
                                    Modifier.weight(1f)
                                ) {
                                    GameItem(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameItem(state: GameState.GameItemState) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(1.dp)
            .background(if (state.isSolid) AppColors.gameBrush else AppColors.gameBrush.copy(alpha = 0.2f))
    )
}