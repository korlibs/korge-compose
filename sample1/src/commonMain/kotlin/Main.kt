import androidx.compose.runtime.*
import com.soywiz.korge.*
import com.soywiz.korge.compose.*

suspend fun main() = Korge(width = 256, height = 64) {
    setComposeContent {
        var count by remember { mutableStateOf(0) }
        VStack {
            Text("$count")
            HStack {
                Button("-") { count-- }
                Button("+") { count++ }
            }
        }
    }
}
