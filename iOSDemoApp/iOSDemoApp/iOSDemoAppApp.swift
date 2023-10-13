import SwiftUI
import AmadeusDemoKt

@main
struct iOSDemoAppApp: App {

    let iosBridge: IosBridge
    let appLifecycleDispatcher: AppLifecycleDispatcher
       
       init() {
           appLifecycleDispatcher = SwiftAppLifecycleDispatcher()
           
           iosBridge = BindingsKt.createIosBridgeWithSwiftAppLifecycleDispatcher(appLifecycleDispatcher: appLifecycleDispatcher)
           //appLifecycleDispatcher = iosBridge.appLifecycleDispatcher
       }
       
       var body: some Scene {
           WindowGroup {
               ZStack {
                   Color.white.ignoresSafeArea(.all) // status bar color
                   ComposeUIViewController(iosBridge: iosBridge)
                   // ContentView(iosBridge: iosBridge)
                       .onReceive(NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)) { _ in
                           print("application_willEnterForeground")
                       }
                       .onReceive(NotificationCenter.default.publisher(for: UIApplication.didBecomeActiveNotification)) { _ in
                           print("application_didBecomeActive")
                           appLifecycleDispatcher.dispatchAppLifecycleEvent(
                               appLifecycleEvent: .start
                           )
                       }
                       .onReceive(NotificationCenter.default.publisher(for: UIApplication.willResignActiveNotification)) { _ in
                           print("application_willResignActive")
                       }.onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)) { _ in
                           print("application_didEnterBackground")
                           appLifecycleDispatcher.dispatchAppLifecycleEvent(
                               appLifecycleEvent: .stop
                           )
                       }
               }.preferredColorScheme(.light)
               
           }
       }

}
