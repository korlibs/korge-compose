import androidx.compose.runtime.*
import com.soywiz.kds.*
import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.compose.*
import com.soywiz.korge.time.*
import com.soywiz.korge.tween.*
import com.soywiz.korim.color.*
import kotlin.coroutines.cancellation.*
import kotlin.reflect.*

suspend fun main() = Korge(width = 256, height = 64) {
    setComposeContent {
        var color by remember { mutableStateOf(Colors.RED) }
        var count by remember { mutableStateOf(0) }
        LaunchedEffect(count) {
            println("LaunchedEffect=$count..started")
            try {
                val nsteps = 20
                for (n in 0..nsteps) {
                    val ratio = n.toDouble() / nsteps.toDouble()
                    color = ratio.interpolate(Colors.RED, Colors.BLUE)
                    delay(10.milliseconds)
                }
                println("LaunchedEffect=$count..ended")
            } catch (e: CancellationException) {
                println("LaunchedEffect=$count..cancelled")
            }
            //stage!!.tween(::color[Colors.BLUE])
        }
        VStack {
            Text("$count", color)
            HStack {
                Button("-") { count-- }
                Button("+") { count++ }
            }
        }
    }
}
