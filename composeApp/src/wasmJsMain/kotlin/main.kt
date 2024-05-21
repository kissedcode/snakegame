import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.kissed.snake.App
import dev.kissed.snake.game.GameFeatureImpl
import dev.kissed.snake.util.extensions.childScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appScope: CoroutineScope = GlobalScope

    val gameFeature = GameFeatureImpl()
    gameFeature.run(appScope.childScope())
    
    CanvasBasedWindow(canvasElementId = "ComposeTarget") { App(gameFeature) }
}