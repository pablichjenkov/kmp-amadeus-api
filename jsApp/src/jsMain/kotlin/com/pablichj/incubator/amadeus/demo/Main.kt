package com.pablichj.incubator.amadeus.demo

import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.storage.DriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import com.pablichj.templato.component.core.BrowserComponentRender
import com.pablichj.templato.component.core.BrowserViewportWindow
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
/*
        Window("Amadeus API Demo") {
            Text("Loading SQDelight")
            val database = remember(Unit) { mutableStateOf<Database?>(null) }

            val databaseCopy = database.value

            if (databaseCopy != null) {
                println("Pablo::onWasmReady databaseCopy != null")
                val hotelDemoComponent = TreeBuilder.getRootComponent(databaseCopy)
                BrowserComponentRender(
                    rootComponent = hotelDemoComponent,
                    onBackPressEvent = {
                        println("Back press dispatched in root node")
                    }
                )
            } else {
                println("Pablo::onWasmReady databaseCopy == null")
                Text("Loading SQDelight")
            }
            LaunchedEffect(Unit) {
                println("Pablo::onWasmReady.LaunchedEffect")
                database.value = createDatabase(DriverFactory())
            }
        }
*/
        BrowserViewportWindow("Amadeus API Demo") {
            val database = remember(this@BrowserViewportWindow) { mutableStateOf<Database?>(null) }
            val databaseCopy = database.value

            if (databaseCopy != null) {
                val hotelDemoComponent = TreeBuilder.getRootComponent(databaseCopy)
                BrowserComponentRender(
                    rootComponent = hotelDemoComponent,
                    onBackPressEvent = {
                        println("Back press dispatched in root node")
                    }
                )
            } else {
                Text("Loading SQDelight")
            }
            LaunchedEffect(this@BrowserViewportWindow) {
                database.value = createDatabase(DriverFactory())
            }
        }
    }
}

