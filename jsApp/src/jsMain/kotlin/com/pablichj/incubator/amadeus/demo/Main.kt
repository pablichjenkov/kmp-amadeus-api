package com.pablichj.incubator.amadeus.demo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.storage.DriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import com.pablichj.incubator.uistate3.BrowserComponentRender
import com.pablichj.incubator.uistate3.BrowserViewportWindow
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow("Amadeus API Demo") {
            val database = remember(this@BrowserViewportWindow) { mutableStateOf<Database?>(null) }
            val databaseCopy = database.value ?: return@BrowserViewportWindow
            val hotelDemoComponent = TreeBuilder.getRootComponent(databaseCopy)
            BrowserComponentRender(
                rootComponent = hotelDemoComponent,
                onBackPressEvent = {
                    println("Back press dispatched in root node")
                }
            )
            LaunchedEffect(this@BrowserViewportWindow) {
                database.value = createDatabase(DriverFactory())
            }
        }
    }
}

