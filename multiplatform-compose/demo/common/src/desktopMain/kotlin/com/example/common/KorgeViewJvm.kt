package com.example.common

import androidx.compose.runtime.*
import androidx.compose.ui.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korio.async.*
import com.soywiz.korma.geom.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

@Composable
actual fun KorgeView(
    modifier: Modifier,
    virtualWidth: Int,
    virtualHeight: Int,
    anchor: com.soywiz.korma.geom.Anchor,
    scaleMode: ScaleMode,
    bgcolor: RGBA,
    block: suspend Stage.() -> Unit
) {
    Text("TO DO KORGE")
    /*
    launchImmediately(newSingleThreadContext("korge")) {
        Korge(Korge.Config(LambdaKorgeModule(
            virtualWidth = virtualWidth,
            virtualHeight = virtualHeight,
            anchor = anchor,
            scaleMode = scaleMode,
            bgcolor = bgcolor,
            block = block
        )))
    }

     */
}
