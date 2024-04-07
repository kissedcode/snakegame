import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.kissed.snake.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "snake") {
        App()
    }
}