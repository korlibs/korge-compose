package korlibs.korge.compose

import androidx.compose.runtime.*
import korlibs.event.Key
import korlibs.korge.annotations.KorgeExperimental
import korlibs.korge.input.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.vector.GpuShapeView
import korlibs.image.bitmap.Bitmaps
import korlibs.image.bitmap.BmpSlice
import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.image.text.*
import korlibs.image.vector.Context2d
import korlibs.math.geom.*

@Composable
fun Text(text: String, color: RGBA = Colors.WHITE, onClick: () -> Unit = {}) {
    ComposeKorgeView({
        TextBlock(RichTextData(text), size = Size(100, 32)).also {
            it.align = TextAlignment.MIDDLE_LEFT
            it.padding = Margin(0f, 8f)
            it.wordWrap = true
        }
        //UIText("DUMMY", height = UIButton.DEFAULT_HEIGHT).also {
        //    println("Created UIText")
        //}
    }) {
        set(text) { this.text = RichTextData(it) }
        set(color) { this.colorMul = it }
        set(onClick) {
            this.onClick { onClick() }
        }
    }
}

@Composable
fun Button(text: String, onClick: () -> Unit = {}) {
    ComposeKorgeView({
        UIButton().also {
            println("Created UIButton")
        }
    }) {
        set(text) { this.text = it }
        set(onClick) { this.onClick { onClick() } }
    }
}

@Composable
fun VStack(width: Float = UI_DEFAULT_SIZE.width, adjustSize: Boolean = false, content: @Composable () -> Unit) {
    ComposeKorgeView(
        { UIVerticalStack(width, adjustSize = adjustSize) },
        {
            set(width) { this@set.width = it }
            //set(adjustSize) { this@set.adjustSize = it }
        },
        content
    )
}

@Composable
fun HStack(height: Float = UI_DEFAULT_SIZE.height, content: @Composable () -> Unit) {
    ComposeKorgeView(
        { UIHorizontalStack(height) },
        {
            set(height) { this.height = it }
        },
        content
    )
}

@Composable
@Deprecated("Let's use Modifier instead")
fun KeyDown(key: Key, onPress: (Key) -> Unit = {}) {
    KeyDown { if (it == key) onPress(it) }
}

@Composable
@Deprecated("Let's use Modifier instead")
fun KeyDown(onPress: (Key) -> Unit = {}) {
    ComposeNode<DummyView, NodeApplier>({
        DummyView()
    }) {
        set(onPress) { this.keys.down { onPress(it.key) } }
    }
}

/**
 * [keys] determine if the object changed to redraw it if required.
 */
@OptIn(KorgeExperimental::class)
@Composable
fun Canvas(vararg keys: Any?, onDraw: Context2d.() -> Unit = {}) {
    ComposeKorgeView({
        GpuShapeView().also {
            it.updateShape { onDraw() }
        }
    }) {
        set(keys.toList()) {
            this.updateShape(onDraw)
        }
    }
}


@Composable
fun Box(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    ComposeKorgeView(
        { SolidRect(100.0, 100.0, Colors.WHITE) },
        {
            set(modifier) { applyModifiers(modifier) }
        },
        content
    )
}

@Suppress("RemoveRedundantQualifierName")
@Composable
fun Image(bitmap: BmpSlice?, modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    ComposeKorgeView(
        { UIImage(Size(100, 100), Bitmaps.transparent, ScaleMode.SHOW_ALL, korlibs.math.geom.Anchor.CENTER) },
        {
            set(bitmap) { this.bitmap = bitmap ?: Bitmaps.transparent }
            set(modifier) { applyModifiers(modifier) }
        },
        content
    )
}

@Composable
// java.lang.NullPointerException: Parameter specified as non-null is null: method korlibs.korge.compose.ComposeWidgetsKt.Scrollable-0ZRKUbw, parameter size
//fun Scrollable(size: Size = UI_DEFAULT_SIZE, content: @Composable () -> Unit = {}) {
fun Scrollable(size: Size, content: @Composable () -> Unit = {}) {
    ComposeKorgeView(
        { UIScrollable(size) },
        {
            set(size) { this.size = size }
        },
        content
    )
}
