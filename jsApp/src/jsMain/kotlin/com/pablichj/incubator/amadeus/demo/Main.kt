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
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {

        Window("Amadeus API Demo") {
            Text("Loading SQDelight")
            val database = remember(Unit) { mutableStateOf<Database?>(null) }

            val databaseCopy = database.value

            if (databaseCopy != null) {
                println("Pablo::onWasmReady databaseCopy != null")
                val hotelDemoComponent = TreeBuilder.getRootComponent(databaseCopy)
                BrowserComponentRender(
                    rootComponent = hotelDemoComponent,
                    onBackPress = {
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

/*
        BrowserViewportWindow("Amadeus API Demo") {
            val database = remember(key1 = Unit) { mutableStateOf<Database?>(null) }
            val databaseCopy = database.value

            if (databaseCopy != null) {
                val hotelDemoComponent = TreeBuilder.getRootComponent(databaseCopy)

                BrowserComponentRender(
                    rootComponent = hotelDemoComponent,
                    onBackPress = {
                        println("Back press dispatched in root node")
                    }
                )

            } else {
                Text("Loading SQDelight")
            }
            LaunchedEffect(key1 = Unit) {
                database.value = createDatabase(DriverFactory())
            }
        }
*/
    }
}

