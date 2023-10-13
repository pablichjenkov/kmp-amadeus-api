import AmadeusDemoKt

class SwiftAppLifecycleDispatcher : AppLifecycleDispatcher {
    
    var currentListener: Component_toolkitAppLifecycleCallback?
    var lastEvent: AppLifecycleEvent?
    
    func dispatchAppLifecycleEvent(appLifecycleEvent: AppLifecycleEvent) {
        print("SwiftAppLifecycleDispatcher::dispatchAppLifecycleEvent \(appLifecycleEvent) event")
        lastEvent = appLifecycleEvent
        
        currentListener?.onEvent(appLifecycleEvent: appLifecycleEvent)
    }
    
    func subscribe(
        appLifecycleCallback: Component_toolkitAppLifecycleCallback
    ) {
        currentListener = appLifecycleCallback
        let lastEventCopy = lastEvent
        guard let lastEventCopy = lastEvent else {
          return
        }
        appLifecycleCallback.onEvent(appLifecycleEvent: lastEventCopy)
        
    }
    
    func unsubscribe(
        appLifecycleCallback: Component_toolkitAppLifecycleCallback
    ) {
        currentListener = nil
    }

}
