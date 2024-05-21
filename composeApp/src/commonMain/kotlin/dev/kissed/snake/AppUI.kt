package dev.kissed.snake

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import dev.kissed.snake.game.GameFeature
import dev.kissed.snake.game.GameFeatureImpl
import dev.kissed.snake.game.GameFeatureUI
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(gameFeature: GameFeature) {
    MaterialTheme {
        GameFeatureUI(gameFeature)
    }
}