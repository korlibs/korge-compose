package com.soywiz.korge.compose

import androidx.compose.runtime.*
import com.soywiz.korev.Key
import com.soywiz.korge.annotations.KorgeExperimental
import com.soywiz.korge.input.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.vector.GpuShapeView
import com.soywiz.korim.bitmap.Bitmaps
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.text.*
import com.soywiz.korim.vector.Context2d
import com.soywiz.korma.geom.*

@Composable
fun Text(text: String, color: RGBA = Colors.WHITE, onClick: () -> Unit = {}) {
    ComposeKorgeView({
        TextBlock(RichTextData(text), height = UIButton.DEFAULT_HEIGHT).also {
            it.align = TextAlignment.MIDDLE_LEFT
            it.padding = Margin(0.0, 8.0)
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
fun VStack(width: Double = UI_DEFAULT_WIDTH, adjustSize: Boolean = false, content: @Composable () -> Unit) {
    ComposeKorgeView(
        { UIVerticalStack(width, adjustSize = adjustSize) },
        {
            set(width) { this.width = it }
            set(adjustSize) { this.adjustSize = it }
        },
        content
    )
}

@Composable
fun HStack(height: Double = UI_DEFAULT_HEIGHT, content: @Composable () -> Unit) {
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

@Composable
fun Image(bitmap: BmpSlice?, modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    ComposeKorgeView(
        { UIImage(100.0, 100.0, Bitmaps.transparent, ScaleMode.SHOW_ALL, com.soywiz.korma.geom.Anchor.CENTER) },
        {
            set(bitmap) { this.bitmap = bitmap ?: Bitmaps.transparent }
            set(modifier) { applyModifiers(modifier) }
        },
        content
    )
}

@Composable
fun Scrollable(width: Double = UI_DEFAULT_WIDTH, height: Double = UI_DEFAULT_WIDTH, content: @Composable () -> Unit = {}) {
    ComposeKorgeView(
        { UIScrollable(width, height) },
        {
            set(width) { this.width = width }
            set(height) { this.height = height }
        },
        content
    )
}
