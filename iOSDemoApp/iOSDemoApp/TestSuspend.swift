//
//  TestSuspend.swift
//  iOSDemoApp
//
//  Created by Pablo Valdes on 10/13/23.
//

import Foundation
import AmadeusDemoKt

class TestSuspend {
    
    var rootComponentFromCallback: Component_toolkitComponent?
    
    /*public func testSuspend() async throws -> Void {
        try await BindingsKt.createRootComponent(onResult: <#T##(Component_toolkitComponent) -> Void#>)
    }*/
    
    public func testSuspend2() async throws -> Component_toolkitComponent  {
        /*try await BindingsKt.createRootComponent(
            onResult: { rootComponent in
                self.rootComponentFromCallback = rootComponent
            }
        )*/
        try await BindingsKt.createRootComponent()
    }
    
    func testClosure() -> Component_toolkitComponent? {
        BindingsKt.requestRootComponent { [unowned self] rootComponent in
             rootComponentFromCallback = rootComponent
        }
        return nil
    }
    
    
    
}
