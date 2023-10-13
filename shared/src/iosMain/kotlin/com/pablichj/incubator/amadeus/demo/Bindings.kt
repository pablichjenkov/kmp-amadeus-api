package com.pablichj.incubator.amadeus.demo

import com.macaosoftware.component.IosComponentRender
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.navbar.BottomNavigationComponent
import com.macaosoftware.component.navbar.BottomNavigationComponentDefaults
import com.macaosoftware.platform.AppLifecycleDispatcher
import com.macaosoftware.platform.DefaultAppLifecycleDispatcher
import com.macaosoftware.platform.IosBridge
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AppBottomNavigationViewModelFactory
import com.pablichj.incubator.amadeus.storage.IosDriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import platform.UIKit.UIViewController

fun ComponentRenderer(
    rootComponent: Component,
    iosBridge: IosBridge
): UIViewController = IosComponentRender(rootComponent, iosBridge)

fun buildAmadeusDemoComponent(): Component {
    return runBlocking {
        val database = createDatabase(IosDriverFactory())
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
