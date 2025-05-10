package org.example.project.nasa.state

import org.example.project.core.util.CFlow
import org.example.project.core.util.wrap

fun NasaStore.watchState(): CFlow<NasaState> = observeState().wrap()
fun NasaStore.watchSideEffect(): CFlow<NasaSideEffect> = observeSideEffect().wrap()