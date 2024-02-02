package com.pablichj.incubator.amadeus.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.macaosoftware.component.BrowserComponentRender
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponentDefaults
import com.macaosoftware.plugin.JsBridge
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AppBottomNavigationViewModelFactory
import kotlinx.coroutines.Dispatchers

@Composable
fun AmadeusDemoWebPage(
    database: Database
) {
    val jsBridge = remember(Unit) {
        JsBridge()
    }
    val rootComponent = remember(database) {
        BottomNavigationComponent(
            viewModelFactory = AppBottomNavigationViewModelFactory(
                diContainer = DiContainer(database),
                BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                    dispatcher = Dispatchers.Main
                )
            ),
            content = BottomNavigationComponentDefaults.BottomNavigationComponentView
        )
    }
    BrowserComponentRender(
        rootComponent = rootComponent,
        jsBridge = jsBridge,
        onBackPress = {
            println("Back press dispatched in root node")
        }
    )
}
