import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korev.Key
import com.soywiz.korge.component.*
import com.soywiz.korge.compose.*
import com.soywiz.korge.compose.Button
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.color.*
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korim.paint.*
import com.soywiz.korio.async.delay
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.vector.*
import kotlin.coroutines.cancellation.CancellationException

class MainComposable : Scene() {
    override suspend fun SContainer.sceneMain() {
        setComposeContent(this) {
            var width by remember { mutableStateOf(width.toInt()) }
            var height by remember { mutableStateOf(height.toInt()) }
            LaunchedEffect(true) {
                onStageResized { w, h ->
                    //println("RESIZED: $w, $h")
                    width = w
                    height = h
                    this@sceneMain.setSize(w.toDouble(), h.toDouble())
                }
            }
            MainApp(width, height)
        }
    }
}

@Composable
private fun MainApp(width: Int, height: Int) {
    //println("MainApp, width=$width, height=$height")
    //App()
    var color by remember { mutableStateOf(Colors.GREEN) }
    //val color2 by remember { Animatable(Colors.RED) }
    var count by remember { mutableStateOf(0) }
    var ratio by remember { mutableStateOf(0.0) }
    var bitmap by remember { mutableStateOf<BmpSlice?>(null) }

    //LaunchedEffect(true) { color2.animateTo(Colors.GREEN) }

    LaunchedEffect(count) {
        println("LaunchedEffect=$count..started")
        try {
            val nsteps = 20
            for (n in 0..nsteps) {
                val r = n.toDouble() / nsteps.toDouble()
                ratio = r
                color = r.interpolate(Colors.RED, Colors.WHITE)
                delay(10.milliseconds)
            }
            println("LaunchedEffect=$count..ended")
        } catch (e: CancellationException) {
            println("LaunchedEffect=$count..cancelled")
        }
        //stage!!.tween(::color[Colors.BLUE])
    }
    LaunchedEffect(true) {
        while (true) {
            bitmap = resourcesVfs["korge.png"].readBitmapSlice()
            delay(2.0.seconds)
            bitmap = resourcesVfs["korim.png"].readBitmapSlice()
            delay(2.0.seconds)
        }
    }
    VStack(width.toDouble(), adjustSize = true) {
        Text("$count", color)
        HStack {
            Button("-") { count-- }
            Button("+") { count += 2 }
        }
        Text("this is a reallly long text to see how, korge-compose handles long texts and figure out if this works or not")
        Canvas(color) {
            fillStroke(color, Stroke(Colors.YELLOWGREEN, thickness = 4.0)) {
                //roundRect(0.0, 0.0, 100.0, 100.0, 50 * ratio, 50 * ratio)
                star(8, 45.0, 100.0, x = 100.0, y = 100.0)
            }
        }
        Scrollable() {
            VStack {
                for (n in 0 until 10) {
                    Button("$n")
                }
            }
        }
        HStack {
            Image(bitmap)
            Image(bitmap)
        }
        Image(bitmap, modifier = Modifier.size(64.0).clip())
        OkErrorComponent()
    }
    Box(
        Modifier
            .anchor(Anchor.BOTTOM_RIGHT)
            //.padding(16.0)
            .backgroundColor(color)
            .size(width * 0.3, 200.0)
            .clickable {
                color = Colors.GREEN
                count++
            }
    ) {
        Box(Modifier.backgroundColor(Colors.RED).size(50.0))
    }
    Box(
        Modifier
            .anchor(Anchor.BOTTOM_LEFT)
            //.padding(16.0)
            .backgroundColor(color)
            .size(width * 0.3, 200.0)
            .clickable {
                color = Colors.GREEN
                count--
            }
    )
    Box(
        Modifier
            .anchor(Anchor.BOTTOM_LEFT)
            //.padding(16.0)
            .backgroundColor(color)
            .fillMaxWidth()
            .clickable {
                color = Colors.GREEN
            }
    )
    KeyDown(Key.DOWN) { count-- }
    KeyDown(Key.UP) { count++ }

}

@Composable
fun OkErrorComponent() {
    var ok by remember { mutableStateOf<Boolean?>(null) }
    val color = remember { Animatable(Colors.DARKGREY) }
    LaunchedEffect(ok) {
        color.animateTo(
            when (ok) {
                null -> Colors.DARKGREY
                true -> Colors.GREEN
                false -> Colors.RED
            }
        )
    }
    VStack {
        //Box(Modifier.backgroundColor(color.value))
        Button("ok") { ok = true }
        Button("error") { ok = false }
        Custom(color.value) {
            Button("demo")
        }
    }
}

@Composable
fun Custom(color: RGBA, content: @Composable () -> Unit = {}) {
    ComposeKorgeView({
        CustomView().also {
            it.solidRect.color = color
        }
    }, {
        set(color) { this.solidRect.color = color }
    }) {
        content()
    }
}

class CustomView : Container() {
    val solidRect = solidRect(100, 100, Colors.DARKOLIVEGREEN).also {
        it.zIndex = -100.0
    }

}
