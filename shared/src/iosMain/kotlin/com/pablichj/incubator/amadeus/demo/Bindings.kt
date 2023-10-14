package com.pablichj.incubator.amadeus.demo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import com.macaosoftware.component.IosComponentRender
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.navbar.BottomNavigationComponent
import com.macaosoftware.component.navbar.BottomNavigationComponentDefaults
import com.macaosoftware.platform.AppLifecycleDispatcher
import com.macaosoftware.platform.DefaultAppLifecycleDispatcher
import com.macaosoftware.platform.IosBridge
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AppBottomNavigationViewModelFactory
import com.pablichj.incubator.amadeus.storage.IosDriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.UIKit.UIViewController

fun AmadeusDemoViewController(
    iosBridge: IosBridge
): UIViewController =
    ComposeUIViewController {

        var database by remember(Unit) { mutableStateOf<Database?>(null) }
        database?.let {
            println("AmadeusDemoViewController Database Ready")
            val rootComponent = BottomNavigationComponent(
                viewModelFactory = AppBottomNavigationViewModelFactory(
                    diContainer = DiContainer(it),
                    BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                        dispatcher = Dispatchers.Main
                    )
                ),
                content = BottomNavigationComponentDefaults.BottomNavigationComponentView
            )
            IosComponentRender(rootComponent, iosBridge)
        }
        LaunchedEffect(Unit) {
            println("AmadeusDemoViewController.LaunchedEffect, Initializing ...")
            database = createDatabase(IosDriverFactory())
        }
    }


fun createIosBridge(): IosBridge {
    return IosBridge(
        appLifecycleDispatcher = DefaultAppLifecycleDispatcher()
    )
}

fun createIosBridgeWithSwiftAppLifecycleDispatcher(
    appLifecycleDispatcher: AppLifecycleDispatcher
): IosBridge {
    return IosBridge(
        appLifecycleDispatcher = appLifecycleDispatcher
    )
}

suspend fun createRootComponent(): Component {
    val database = createDatabase(IosDriverFactory())
    return BottomNavigationComponent(
        viewModelFactory = AppBottomNavigationViewModelFactory(
            diContainer = DiContainer(database),
            BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                dispatcher = Dispatchers.Main
            )
        ),
        content = BottomNavigationComponentDefaults.BottomNavigationComponentView
    )
}

fun requestRootComponent(
    onResult: (Component) -> Unit
) {
    GlobalScope.launch {
        val database = createDatabase(IosDriverFactory())
        val rootComponent = BottomNavigationComponent(
            viewModelFactory = AppBottomNavigationViewModelFactory(
                diContainer = DiContainer(database),
                BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                    dispatcher = Dispatchers.Main
                )
            ),
            content = BottomNavigationComponentDefaults.BottomNavigationComponentView
        )
        onResult(rootComponent)
    }
}
