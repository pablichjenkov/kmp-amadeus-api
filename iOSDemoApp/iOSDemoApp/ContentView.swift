import SwiftUI
import UIKit
import ComposeApp

struct ComposeUIViewController : UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        return AmadeusApiKt.AmadeusDemoViewController(
            onBackPress: { exit(0) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {

    var body: some View {
        ComposeUIViewController()
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
