import SwiftUI

@main
struct iOSApp: App {

    init() {
        // 依存関係を初期化
        _ = AppDependencies.shared
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}