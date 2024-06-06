package com.pablichj.incubator.amadeus.demo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.storage.DesktopDriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun main() {
    val windowState = WindowState(size = DpSize(500.dp, 800.dp))
    singleWindowApplication(
        title = "Amadeus Desktop Demo",
        state = windowState,
        undecorated = true
    ) {
        var database by remember { mutableStateOf<Database?>(null) }
        database?.let {
            ComposeDesktopApplication(windowState, it)
        }
        LaunchedEffect(Unit) {
            database = withContext(Dispatchers.Default) {
                createDatabase(DesktopDriverFactory())
            }
        }
    }
}
