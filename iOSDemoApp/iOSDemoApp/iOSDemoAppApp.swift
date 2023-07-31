//
//  iOSDemoAppApp.swift
//  iOSDemoApp
//
//  Created by Pablo Valdes on 7/31/23.
//

import SwiftUI
import AmadeusDemoKt

@main
struct iOSDemoAppApp: App {

    let iosBridge = BindingsKt.createPlatformBridge()
       
       //let left = Int32(0)//Int32(safeAreaInsets.left.rounded())
       //let top = Int32(40)//Int32(safeAreaInsets.top.rounded())
       //let right = Int32(0)//Int32(safeAreaInsets.right.rounded())
       //let bottom = Int32(24)//Int32(safeAreaInsets.bottom.rounded())
       init() {
           iosBridge.safeAreaInsets.start = Int32(0)//left
           iosBridge.safeAreaInsets.top = Int32(0) //top
           iosBridge.safeAreaInsets.end = Int32(0)//right
           iosBridge.safeAreaInsets.bottom = Int32(0)//bottom
       }
       
       var body: some Scene {
           WindowGroup {
               ContentView(iosBridge: iosBridge)
                   .onReceive(NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)) { _ in
                       print("application_willEnterForeground")
                   }
                   .onReceive(NotificationCenter.default.publisher(for: UIApplication.didBecomeActiveNotification)) { _ in
                       print("application_didBecomeActive")
                       iosBridge.appLifecycleDispatcher.dispatchAppLifecycleEvent(
                           appLifecycleEvent: .start
                       )
                   }
                   .onReceive(NotificationCenter.default.publisher(for: UIApplication.willResignActiveNotification)) { _ in
                       print("application_willResignActive")
                   }.onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)) { _ in
                       print("application_didEnterBackground")
                       iosBridge.appLifecycleDispatcher.dispatchAppLifecycleEvent(
                           appLifecycleEvent: .stop
                       )
                   }
           }
       }

    /*var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }*/
}
