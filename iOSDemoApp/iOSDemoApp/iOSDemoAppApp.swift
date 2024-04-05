import SwiftUI
import AmadeusApiDemoKt

@main
struct iOSDemoAppApp: App {
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                Color.white.ignoresSafeArea(.all) // status bar color
                ComposeUIViewController()
            }.preferredColorScheme(.light)
            
        }
    }
    
}
