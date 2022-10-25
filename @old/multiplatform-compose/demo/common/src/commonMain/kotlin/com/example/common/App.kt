package com.example.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val platformName = getPlatformName()

    CircleShape
    Canvas()
    Box()

    Column {
        Button(onClick = {
            text = "Hello, $platformName"
        }) {
            Text(text)
        }

        Text("HELLO")

        KorgeView(Modifier.width(300.dp).height(300.dp))

        Demo()
    }
}

@Composable
fun Demo() {
    Text("DEMO WORLD")
}
