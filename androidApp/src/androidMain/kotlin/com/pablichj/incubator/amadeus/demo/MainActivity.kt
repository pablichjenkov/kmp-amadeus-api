package com.pablichj.incubator.amadeus.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.macaosoftware.component.AndroidComponentRender
import com.macaosoftware.component.navbar.NavBarComponent
import com.macaosoftware.component.navbar.NavBarComponentDefaults
import com.macaosoftware.platform.AndroidBridge
import com.pablichj.incubator.amadeus.storage.DriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val androidBridge = AndroidBridge()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val database = createDatabase(DriverFactory(this@MainActivity))
            val navBarComponent = NavBarComponent(
                navBarStatePresenter = NavBarComponentDefaults.createNavBarStatePresenter(
                    dispatcher = Dispatchers.Main
                ),
                componentViewModel = AppNavBarViewModel(database),
                content = NavBarComponentDefaults.NavBarComponentView
            )
            setContent {
                AndroidComponentRender(
                    rootComponent = navBarComponent,
                    androidBridge = androidBridge,
                    onBackPress = { finishAffinity() }
                )
            }
        }
    }
}
