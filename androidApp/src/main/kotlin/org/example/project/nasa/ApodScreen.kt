package org.example.project.nasa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.example.project.nasa.presentation.ApodViewModel
import org.example.project.nasa.presentation.mvi.ApodEffect
import org.example.project.nasa.presentation.mvi.ApodIntent
import org.koin.androidx.compose.koinViewModel

/**
 * APOD（Astronomy Picture of the Day）画面
 */
@Composable
fun ApodScreen() {
    val viewModel: ApodViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    
    // 副作用を処理
    LaunchedEffect(key1 = true) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ApodEffect.Error -> {
                    // エラー処理（例: Snackbarの表示など）
                    println("エラーが発生しました: ${effect.error.message}")
                }
                is ApodEffect.CacheCleared -> {
                    // キャッシュクリア完了通知（例: Toastの表示など）
                    println("キャッシュがクリアされました")
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NASA 天文学の写真") },
                actions = {
                    IconButton(onClick = {
                        viewModel.processIntent(ApodIntent.LoadApod(forceUpdate = true))
                    }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "更新")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val apod = state.apod
                    if (apod != null) {
                        Text(
                            text = apod.title,
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        apod.copyright?.let {
                            Text(
                                text = "© $it",
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        
                        Text(
                            text = apod.date,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        if (apod.mediaType == "image") {
                            AsyncImage(
                                model = apod.url,
                                contentDescription = apod.title,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(bottom = 16.dp)
                            )
                        }
                        
                        Text(
                            text = apod.explanation,
                            style = MaterialTheme.typography.body1
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Text(
                            text = "天文学の写真を読み込むには「更新」ボタンをタップしてください。",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    Button(
                        onClick = { viewModel.processIntent(ApodIntent.LoadApod(forceUpdate = true)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("更新")
                    }
                }
                
                state.errorMessage?.let { errorMessage ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        backgroundColor = MaterialTheme.colors.error
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colors.onError
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            TextButton(
                                onClick = { viewModel.processIntent(ApodIntent.ClearError) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colors.onError
                                )
                            ) {
                                Text("閉じる")
                            }
                        }
                    }
                }
            }
        }
    }
} 