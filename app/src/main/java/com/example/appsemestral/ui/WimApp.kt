package com.example.appsemestral.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appsemestral.ui.screens.CandidataFormScreen
import com.example.appsemestral.ui.screens.CandidataListScreen
import com.example.appsemestral.ui.screens.EvaluacionScreen
import com.example.appsemestral.ui.screens.HistorialScreen
import com.example.appsemestral.ui.screens.RankingScreen
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel
import com.example.appsemestral.ui.viewmodel.CandidataListViewModel
import com.example.appsemestral.ui.viewmodel.EvaluacionViewModel
import com.example.appsemestral.ui.viewmodel.RankingHistorialViewModel

/** Pantallas de la app. Las que tienen ícono aparecen en la barra inferior. */
enum class Pantalla(val etiqueta: String, val icono: ImageVector?) {
    LISTA("Candidatas", Icons.Filled.People),
    EVALUACION("Evaluar", Icons.Filled.Star),
    RANKING("Ranking", Icons.Filled.EmojiEvents),
    HISTORIAL("Historial", Icons.Filled.History),
    FORMULARIO("Formulario", null) // pantalla completa, sin barra inferior
}

private val pestanas = Pantalla.entries.filter { it.icono != null }

@Composable
fun WimApp(
    formViewModel: CandidataFormViewModel,
    listViewModel: CandidataListViewModel,
    evaluacionViewModel: EvaluacionViewModel,
    rankingViewModel: RankingHistorialViewModel
) {
    // rememberSaveable: la pantalla actual sobrevive a una rotación
    var pantallaActual by rememberSaveable { mutableStateOf(Pantalla.LISTA) }

    // Botón "atrás": desde cualquier pantalla vuelve a la lista
    BackHandler(enabled = pantallaActual != Pantalla.LISTA) {
        pantallaActual = Pantalla.LISTA
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (pantallaActual != Pantalla.FORMULARIO) {
                NavigationBar {
                    pestanas.forEach { destino ->
                        NavigationBarItem(
                            selected = pantallaActual == destino,
                            onClick = { pantallaActual = destino },
                            icon = {
                                destino.icono?.let { Icon(it, contentDescription = destino.etiqueta) }
                            },
                            label = { Text(destino.etiqueta) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).consumeWindowInsets(padding)) {
            AnimatedContent(
                targetState = pantallaActual,
                transitionSpec = {
                    when {
                        // El formulario entra deslizando desde la derecha
                        targetState == Pantalla.FORMULARIO ->
                            (slideInHorizontally(tween(300)) { it } + fadeIn(tween(300))) togetherWith
                                    (slideOutHorizontally(tween(300)) { -it / 3 } + fadeOut(tween(200)))
                        // y al volver sale hacia la derecha
                        initialState == Pantalla.FORMULARIO ->
                            (slideInHorizontally(tween(300)) { -it / 3 } + fadeIn(tween(300))) togetherWith
                                    (slideOutHorizontally(tween(300)) { it } + fadeOut(tween(200)))
                        // entre pestañas: fundido suave
                        else -> fadeIn(tween(250)) togetherWith fadeOut(tween(150))
                    }
                },
                label = "navegacion"
            ) { pantalla ->
                when (pantalla) {
                    Pantalla.LISTA -> CandidataListScreen(
                        viewModel = listViewModel,
                        onAgregarClick = {
                            formViewModel.limpiarFormulario()
                            pantallaActual = Pantalla.FORMULARIO
                        },
                        onEditarClick = { candidata ->
                            formViewModel.cargarCandidata(candidata)
                            pantallaActual = Pantalla.FORMULARIO
                        }
                    )
                    Pantalla.FORMULARIO -> CandidataFormScreen(
                        viewModel = formViewModel,
                        onVolver = { pantallaActual = Pantalla.LISTA }
                    )
                    Pantalla.EVALUACION -> EvaluacionScreen(
                        viewModel = evaluacionViewModel,
                        onVolver = { pantallaActual = Pantalla.LISTA }
                    )
                    Pantalla.RANKING -> RankingScreen(
                        viewModel = rankingViewModel,
                        onVolver = { pantallaActual = Pantalla.LISTA }
                    )
                    Pantalla.HISTORIAL -> HistorialScreen(
                        viewModel = rankingViewModel,
                        onVolver = { pantallaActual = Pantalla.LISTA }
                    )
                }
            }
        }
    }
}