package com.example.common

import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.*
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.tween.tweenAsync
import com.soywiz.korim.color.RGBA
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.bezier.Bezier
import com.soywiz.korma.geom.shape.buildVectorPath
import com.soywiz.korma.geom.vector.line
import kotlin.reflect.*

@Composable
expect fun KorgeView(
    modifier: Modifier = Modifier,
    virtualWidth: Int = 512,
    virtualHeight: Int = 512,
    anchor: Anchor = Anchor.TOP_LEFT,
    scaleMode: ScaleMode = ScaleMode.SHOW_ALL,
    bgcolor: RGBA = com.soywiz.korim.color.Colors.DARKCYAN,
    block: suspend Stage.() -> Unit = {}
)

class LambdaKorgeModule(
    virtualWidth: Int = 512,
    virtualHeight: Int = 512,
    anchor: Anchor = Anchor.TOP_LEFT,
    scaleMode: ScaleMode = ScaleMode.SHOW_ALL,
    bgcolor: RGBA = com.soywiz.korim.color.Colors.DARKCYAN,
    val block: suspend Stage.() -> Unit = {}
) : com.soywiz.korge.scene.Module() {
    class DefaultSceneParams(val callback: suspend Stage.() -> Unit, dummy: Unit = Unit)
    class DefaultScene(val params: DefaultSceneParams) : Scene() {
        override suspend fun Container.sceneInit() {
            params.callback(this.stage!!)
        }
    }

    override val mainScene: KClass<out Scene> get() = DefaultScene::class
    override val scaleAnchor: Anchor = anchor
    override val scaleMode: ScaleMode = scaleMode
    override val size: SizeInt = SizeInt(virtualWidth, virtualHeight)
    override val bgcolor: RGBA = bgcolor

    override suspend fun AsyncInjector.configure() {
        mapPrototype { DefaultScene(get()) }
        mapInstance(DefaultSceneParams(block))
    }
}