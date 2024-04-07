package dev.kissed.snake.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.kissed.snake.models.Direction
import dev.kissed.snake.ui.AppColors

@Composable
fun GameFeatureUI(feature: GameFeature) {
    val state = feature.states.collectAsState()
    Box(
        Modifier
            .fillMaxSize()
            .background(AppColors.gameBackground)
//            .drawBehind { 
//                drawLine(Color.Red, start = Offset.Zero, end = Offset(size.width, size.height))
//                drawLine(Color.Red, start = Offset(0f, size.height), end = Offset(size.width, 0f))
//            }
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
                val k = size.height.toFloat() / size.width.toFloat()
                detectTapGestures { tap ->
                    val lineOne = tap.y - tap.x * k
                    val lineTwo = tap.y - (-tap.x * k + size.height)
                    val tapDirection = when {
                        lineOne < 0 && lineTwo < 0 -> Direction.UP
                        lineOne < 0 && lineTwo >= 0 -> Direction.RIGHT
                        lineOne >= 0 && lineTwo >= 0 -> Direction.DOWN
                        lineOne >= 0 && lineTwo < 0 -> Direction.LEFT
                        else -> error("impossible")
                    }
                    feature.dispatch(GameFeature.Action.ChangeDirection(tapDirection))
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        GameFieldUI(state.value)
    }
}

@Composable
private fun GameFieldUI(state: GameFeature.GameState) {
    Column {
        Text(state.direction.name)
        Box(
            Modifier.size(400.dp)
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

@Composable
private fun GameItem(state: GameFeature.GameState.GameItemState) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(1.dp)
            .background(if (state.isSolid) AppColors.gameBrush else AppColors.gameBrush.copy(alpha = 0.2f))
    )
}