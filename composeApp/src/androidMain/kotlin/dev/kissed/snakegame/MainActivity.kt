package dev.kissed.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.kissed.snake.App
import dev.kissed.snake.game.GameFeatureImpl
import dev.kissed.snake.util.logging.DebugLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    
    private val id = Random.nextInt()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            while(true) {
                delay(3000)
                DebugLog.d("activity[${id}] scope alive")    
            }
        }

        val gameFeature = GameFeatureImpl()
          
        setContent {
            App(gameFeature)
        }
        
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val featureScope = this@repeatOnLifecycle
                gameFeature.run(featureScope)
            }    
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
