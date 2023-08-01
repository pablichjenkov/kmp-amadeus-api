package com.pablichj.incubator.amadeus.demo

import com.pablichj.incubator.amadeus.storage.DriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import com.pablichj.templato.component.core.Component
import com.pablichj.templato.component.core.IosComponentRender
import com.pablichj.templato.component.platform.DefaultAppLifecycleDispatcher
import com.pablichj.templato.component.platform.IosBridge
import com.pablichj.templato.component.platform.SafeAreaInsets
import kotlinx.coroutines.runBlocking
import platform.UIKit.UIViewController

fun ComponentRenderer(
    rootComponent: Component,
    iosBridge: IosBridge
): UIViewController = IosComponentRender(rootComponent, iosBridge)

fun buildAmadeusDemoComponent(): Component {
    return runBlocking {
        val db = createDatabase(DriverFactory())
        TreeBuilder.getRootComponent(db)
    }
}

fun createPlatformBridge(): IosBridge {
    return IosBridge(
        appLifecycleDispatcher = DefaultAppLifecycleDispatcher(),
        safeAreaInsets = SafeAreaInsets()
    )
}