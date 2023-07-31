//
//  ContentView.swift
//  iOSDemoApp
//
//  Created by Pablo Valdes on 7/31/23.
//

import SwiftUI
import UIKit
import AmadeusDemoKt

struct ComposeView : UIViewControllerRepresentable {

    var iosBridge: IosBridge

    func makeUIViewController(context: Context) -> UIViewController {
        let amadeusDemoRootComponent = BindingsKt.buildAmadeusDemoComponent()

        let mainViewController = BindingsKt.ComponentRenderer(
            rootComponent: amadeusDemoRootComponent,
            iosBridge: iosBridge
        )

        return mainViewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {

    var iosBridge: IosBridge

    var body: some View {
        ComposeView(iosBridge: iosBridge)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler

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
