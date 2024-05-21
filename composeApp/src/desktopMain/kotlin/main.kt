import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.kissed.snake.App
import dev.kissed.snake.game.GameFeature
import dev.kissed.snake.game.GameFeatureImpl
import dev.kissed.snake.util.extensions.childScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

fun main() = application {
    val appScope: CoroutineScope = GlobalScope
    
    val gameFeature = GameFeatureImpl()
    gameFeature.run(appScope.childScope())
    
    Window(onCloseRequest = ::exitApplication, title = "snake") {
        App(gameFeature)
    }
}