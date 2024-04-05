import Foundation
import AmadeusApiDemoKt

class TestSuspend {
    
    var rootComponentFromCallback: Component_toolkitComponent?
    
    /*public func testSuspend() async throws -> Void {
     try await BindingsKt.createRootComponent(onResult: <#T##(Component_toolkitComponent) -> Void#>)
     }*/
    
    public func testSuspend2() async throws -> Component_toolkitComponent  {
        try await AmadeusApiKt.createRootComponent(
            onBackPress: { exit(0) }
        )
    }
    
    func testClosure() {
        AmadeusApiKt.requestRootComponent (
            onResult: { [unowned self] rootComponent in
                rootComponentFromCallback = rootComponent
            },
            onBackPress: { exit(0) }
        )
        
    }
    
}
