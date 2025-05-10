package org.example.project.core.di

import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.example.project.nasa.NasaApiClient
import org.example.project.nasa.NasaRepository
import org.example.project.nasa.state.NasaStore
import org.example.project.nasa.storage.NasaStorage
import org.koin.dsl.module

val nasaModule = module {
    // NASA API関連
    single { NasaApiClient(get(), get()) }
    single { NasaStorage(get(), get()) }
    single { NasaRepository(get(), get()) }
    single { NasaStore(get()) }
}