package com.pablichj.incubator.amadeus.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.macaosoftware.component.BrowserComponentRender
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponentDefaults
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AppBottomNavigationViewModelFactory
import kotlinx.coroutines.Dispatchers

@Composable
fun AmadeusDemoWebPage(
    database: Database,
    onBackPress: () -> Boolean
) {
    val rootComponent = remember(database) {
        BottomNavigationComponent(
            viewModelFactory = AppBottomNavigationViewModelFactory(
                diContainer = DiContainer(database),
                BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                    dispatcher = Dispatchers.Main
                ),
                onBackPress = onBackPress
            ),
            content = BottomNavigationComponentDefaults.BottomNavigationComponentView
        )
    }
    BrowserComponentRender(
        rootComponent = rootComponent
    )
}
