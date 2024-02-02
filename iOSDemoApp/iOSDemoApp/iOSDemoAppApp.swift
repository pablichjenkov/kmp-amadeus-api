import SwiftUI
import AmadeusApiDemoKt

@main
struct iOSDemoAppApp: App {

    let iosBridge: IosBridge
    let platformLifecyclePlugin: AppLifecycleDispatcher
       
       init() {
           platformLifecyclePlugin = SwiftAppLifecycleDispatcher()
           
           iosBridge = BindingsKt.createIosBridgeWithSwiftAppLifecycleDispatcher(platformLifecyclePlugin: platformLifecyclePlugin)
           //platformLifecyclePlugin = iosBridge.platformLifecyclePlugin
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
                           platformLifecyclePlugin.dispatchAppLifecycleEvent(
                               appLifecycleEvent: .start
                           )
                       }
                       .onReceive(NotificationCenter.default.publisher(for: UIApplication.willResignActiveNotification)) { _ in
                           print("application_willResignActive")
                       }.onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)) { _ in
                           print("application_didEnterBackground")
                           platformLifecyclePlugin.dispatchAppLifecycleEvent(
                               appLifecycleEvent: .stop
                           )
                       }
               }.preferredColorScheme(.light)
               
           }
       }

}
