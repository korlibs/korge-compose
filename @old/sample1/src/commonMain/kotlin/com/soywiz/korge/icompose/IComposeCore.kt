package com.soywiz.korge.icompose

import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import kotlin.reflect.*

open class C(val view: View) {
    var lastId = 0
    fun <T> ComposeChild(block: C.() -> T) {
    }
}

inline fun <T: View> C.ComposeView(factory: () -> T) {
}

fun <T> C.remember(id: Int = this.lastId++, block: () -> T): KMutableProperty0<T> {
    TODO()
}

fun C.Text(text: String) {
}

fun C.Button(text: String, onClick: () -> Unit) {
    ComposeView({ UIButton() })
}

fun C.Column(block: C.() -> Unit) {
    ComposeView({ UIVerticalStack() })
    ComposeChild(block)
}

fun C.Row(block: C.() -> Unit) {
    ComposeView({ UIHorizontalStack() })
    ComposeChild(block)
}

fun C.Main() {
    var count by remember { 0 }
    Column {
        Text("hello $count")
        Row {
            Button("+") { count++ }
            Button("-") { count-- }
        }
    }
}
