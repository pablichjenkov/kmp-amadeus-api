import SwiftUI
import UIKit
import AmadeusDemoKt

struct ComposeUIViewController : UIViewControllerRepresentable {

    var iosBridge: IosBridge
    
    func makeUIViewController(context: Context) -> UIViewController {
        let amadeusDemoComponent = BindingsKt.buildAmadeusDemoComponent()

        let mainViewController = BindingsKt.ComponentRenderer(
            rootComponent: amadeusDemoComponent,
            iosBridge: iosBridge
        )

        return mainViewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {

    var iosBridge: IosBridge

    var body: some View {
        ComposeUIViewController(iosBridge: iosBridge)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                //.ignoresSafeArea(.all, edges: .bottom) // If prefered to handle this in compose land

    }
}
/*
struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text("Hello, world!")
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

*/
