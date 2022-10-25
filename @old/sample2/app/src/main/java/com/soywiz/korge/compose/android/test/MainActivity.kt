package com.soywiz.korge.compose.android.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.defaultDecayAnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import com.soywiz.klock.seconds
import com.soywiz.korge.android.KorgeAndroidView
import com.soywiz.korge.compose.android.test.ui.theme.KorgeComposeAndroidTestTheme
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.tween.tweenAsync
import com.soywiz.korge.view.*
import com.soywiz.korim.color.RGBA
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.bezier.Bezier
import com.soywiz.korma.geom.shape.buildVectorPath
import com.soywiz.korma.geom.vector.curve
import com.soywiz.korma.geom.vector.line
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KorgeComposeAndroidTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column {
                        Greeting("Android")
                        Greeting("Android")
                        Greeting("Android")
                        Greeting("Android")
                        KorgeView(Modifier.height(300.dp)) {
                            val rect = solidRect(100, 100, com.soywiz.korim.color.Colors.RED)
                            text("HELLO: ${views.virtualWidth}, ${views.virtualHeight}, ${views.nativeWidth}, ${views.nativeHeight}")
                            launchImmediately {
                                while (true) {
                                    tween(rect::pos[buildVectorPath {
                                        moveTo(0.0, 0.0)
                                        lineTo(200.0, 200.0)
                                        quadTo(0.0, 200.0, 0.0, 0.0)
                                        close()
                                    }], time = 2.seconds)
                                }
                            }
                        }
                        Greeting("Android")
                        Greeting("Android")
                        Greeting("Android")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", color = Color.Green)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KorgeComposeAndroidTestTheme {
        Column {
            defaultDecayAnimationSpec().
            androidx.compose.animation.core.Animatable(Color.Red).animateTo().animateTo()
            FloatingActionButton()
            Modifier.fillMaxSize()
            Canvas(modifier = , onDraw = )
            Greeting("Android")
            Greeting("Android")
            Greeting("Android")
            KorgeView { solidRect(100, 100, com.soywiz.korim.color.Colors.RED) }
        }
    }
}

@Composable
fun KorgeView(
    modifier: Modifier = Modifier,
    virtualWidth: Int = 512,
    virtualHeight: Int = 512,
    anchor: Anchor = Anchor.TOP_LEFT,
    scaleMode: ScaleMode = ScaleMode.SHOW_ALL,
    bgcolor: RGBA = com.soywiz.korim.color.Colors.DARKCYAN,
    block: suspend Stage.() -> Unit
) {
    class DefaultSceneParams(val callback: suspend Stage.() -> Unit, dummy: Unit = Unit)
    class DefaultScene(val params: DefaultSceneParams) : Scene() {
        override suspend fun Container.sceneInit() {
            params.callback(this.stage!!)
        }
    }
    AndroidView(
        factory = {
            KorgeAndroidView(it).also {
                it.loadModule(object : Module() {
                    override val mainScene: KClass<out Scene> get() = DefaultScene::class
                    override val scaleAnchor: Anchor = anchor
                    override val scaleMode: ScaleMode = scaleMode
                    override val size: SizeInt = SizeInt(virtualWidth, virtualHeight)
                    override val bgcolor: RGBA = bgcolor

                    override suspend fun AsyncInjector.configure() {
                        mapPrototype { DefaultScene(get()) }
                        mapInstance(DefaultSceneParams(block))
                    }
                })
            }
        },
        modifier = modifier,
        update = {
            //println("UPDATE")
        }
    )
}