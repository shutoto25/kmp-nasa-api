import SwiftUI
import shared

struct ApodView: View {
    @StateObject private var viewModel = ApodViewModel()
    
    var body: some View {
        Group {
            if viewModel.isLoading {
                ProgressView()
            } else if let error = viewModel.error {
                Text(error)
                    .foregroundColor(.red)
            } else if let apod = viewModel.apod {
                ScrollView {
                    VStack(alignment: .leading, spacing: 16) {
                        AsyncImage(url: URL(string: apod.url)) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                        } placeholder: {
                            ProgressView()
                        }
                        .frame(height: 300)
                        
                        HStack {
                            Text(apod.title)
                                .font(.title)
                            
                            Spacer()
                            
                            Button(action: {
                                viewModel.toggleFavorite(apod)
                            }) {
                                Image(systemName: viewModel.isFavorite(apod) ? "heart.fill" : "heart")
                                    .foregroundColor(viewModel.isFavorite(apod) ? .red : .gray)
                            }
                        }
                        
                        Text(apod.date)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                        
                        Text(apod.explanation)
                            .font(.body)
                    }
                    .padding()
                }
            }
        }
        .onAppear {
            viewModel.loadTodayImage()
        }
    }
} 