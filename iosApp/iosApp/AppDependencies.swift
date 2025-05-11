import Foundation
import Shared

class AppDependencies {
    static let shared = AppDependencies()

    private let koinDependencies: IosAppDependencies

    private init() {
        // 依存関係のインスタンスを取得
        koinDependencies = IosAppDependencies.Companion().create()
    }

    // NasaStoreを取得
    var nasaStore: NasaStore {
        return koinDependencies.nasaStore
    }

    // PlatformStorageを取得
    var platformStorage: PlatformStorage {
        return koinDependencies.platformStorage
    }
}