package com.pablichj.incubator.amadeus.demo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.storage.BrowserDriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Amadeus API Demo") {
            var database by remember(Unit) { mutableStateOf<Database?>(null) }
            database?.let {
                println("JS_Main::onWasmReady databaseCopy != null")
                AmadeusDemoWebPage(it)
            }
            LaunchedEffect(Unit) {
                println("JS_main::onWasmReady.LaunchedEffect, Initializing ...")
                database = createDatabase(BrowserDriverFactory())
            }
        }
    }
}
