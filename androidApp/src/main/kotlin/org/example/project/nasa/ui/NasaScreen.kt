package org.example.project.nasa.ui

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
import org.example.project.nasa.state.NasaAction
import org.example.project.nasa.state.NasaStore
import org.koin.compose.koinInject

@Composable
fun NasaScreen() {
    val nasaStore: NasaStore = koinInject()
    val state by nasaStore.observeState().collectAsState()

    LaunchedEffect(Unit) {
        if (state.apod == null) {
            nasaStore.dispatch(NasaAction.LoadApod(forceUpdate = false))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NASA 天文学の写真") },
                actions = {
                    IconButton(onClick = {
                        nasaStore.dispatch(NasaAction.LoadApod(forceUpdate = true))
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
                if (state.loading) {
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
                        onClick = { nasaStore.dispatch(NasaAction.LoadApod(forceUpdate = true)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("更新")
                    }
                }

                state.errorMessage?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        backgroundColor = MaterialTheme.colors.error
                    ) {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.onError,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}