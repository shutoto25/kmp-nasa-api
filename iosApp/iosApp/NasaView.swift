import SwiftUI
import Shared

struct NasaView: View {
    @ObservedObject var viewModel = NasaViewModel()

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if viewModel.state.loading {
                    ProgressView()
                        .frame(maxWidth: .infinity, minHeight: 200)
                } else if let apod = viewModel.state.apod {
                    Text(apod.title)
                        .font(.title)
                        .fontWeight(.bold)

                    if let copyright = apod.copyright {
                        Text("© \(copyright)")
                            .font(.caption)
                    }

                    Text(apod.date)
                        .font(.subheadline)

                    if apod.mediaType == "image", let url = URL(string: apod.url) {
                        AsyncImage(url: url) { phase in
                            switch phase {
                            case .empty:
                                ProgressView()
                            case .success(let image):
                                image
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                            case .failure:
                                Image(systemName: "photo")
                                    .imageScale(.large)
                            @unknown default:
                                EmptyView()
                            }
                        }
                        .frame(maxWidth: .infinity)
                    }

                    Text(apod.explanation)
                        .font(.body)
                } else {
                    Text("天文学の写真を読み込むには下のボタンをタップしてください。")
                        .multilineTextAlignment(.center)
                        .frame(maxWidth: .infinity, minHeight: 200)
                }

                Button(action: {
                    viewModel.loadApod(forceUpdate: true)
                }) {
                    Text("更新")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
                .padding(.top, 16)

                if let errorMessage = viewModel.state.errorMessage {
                    Text(errorMessage)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.red)
                        .cornerRadius(8)
                }
            }
            .padding()
        }
        .navigationTitle("NASA 今日の一枚")
        .toolbar {
            Button(action: {
                viewModel.loadApod(forceUpdate: true)
            }) {
                Image(systemName: "arrow.clockwise")
            }
        }
        .onAppear {
            if viewModel.state.apod == nil {
                viewModel.loadApod(forceUpdate: false)
            }
        }
    }
}

class NasaViewModel: ObservableObject {
    @Published var state: NasaState

    private let store: NasaStore
    private var stateWatcher: Closeable?
    private var effectWatcher: Closeable?

    init() {
        let nasaStore = NasaStoreHelper.shared.getStore()
        self.store = nasaStore
        self.state = nasaStore.observeState().value as! NasaState

        stateWatcher = nasaStore.watchState().watch { [weak self] state in
            self?.state = state as! NasaState
        }

        effectWatcher = nasaStore.watchSideEffect().watch { [weak self] effect in
            if let error = effect as? NasaSideEffect.Error {
                // エラーハンドリング
                print("Error: \(error.error.message ?? "Unknown error")")
            }
        }
    }

    func loadApod(forceUpdate: Bool, date: String? = nil) {
        store.dispatch(action: NasaAction.LoadApod(forceUpdate: forceUpdate, date: date))
    }

    deinit {
        stateWatcher?.close()
        effectWatcher?.close()
    }
}

// シングルトンヘルパーでストアへのアクセスを管理
class NasaStoreHelper {
    static let shared = NasaStoreHelper()

    private var nasaStore: NasaStore?

    private init() {}

    func getStore() -> NasaStore {
        if let store = nasaStore {
            return store
        }

        let store = NasaStore(nasaRepository: getRepository())
        nasaStore = store
        return store
    }

    private func getRepository() -> NasaRepository {
        return NasaRepository(
            apiClient: NasaApiClient(
                httpClient: HttpClientFactory().create(),
                json: JsonFactory().create()
            ),
            storage: NasaStorage(
                settings: SettingsFactory().create(),
                json: JsonFactory().create()
            )
        )
    }
}

// ファクトリークラス
class HttpClientFactory {
    func create() -> HttpClient {
        return HttpClient()
    }
}

class JsonFactory {
    func create() -> Json {
        return Json()
    }
}

class SettingsFactory {
    func create() -> Settings {
        return Settings()
    }
}