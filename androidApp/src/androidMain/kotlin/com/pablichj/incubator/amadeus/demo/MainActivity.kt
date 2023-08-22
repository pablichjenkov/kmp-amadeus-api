package com.pablichj.incubator.amadeus.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.pablichj.incubator.amadeus.storage.DriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import com.pablichj.templato.component.core.AndroidComponentRender
import com.pablichj.templato.component.platform.AndroidBridge
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val androidBridge = AndroidBridge()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val database = createDatabase(DriverFactory(this@MainActivity))
            setContent {
                AndroidComponentRender(
                    rootComponent = TreeBuilder.getRootComponent(database),
                    androidBridge = androidBridge,
                    onBackPress = { finishAffinity() }
                )
            }
        }
    }
}
