package com.example.appsemestral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.appsemestral.ui.screens.CandidataFormScreen
import com.example.appsemestral.ui.screens.CandidataListScreen
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel
import com.example.appsemestral.ui.viewmodel.CandidataListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as WimApplication).container

        // Instanciamos ambos ViewModels con el repositorio
        val formViewModel = CandidataFormViewModel(appContainer.candidataRepository)
        val listViewModel = CandidataListViewModel(appContainer.candidataRepository)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Control simple de pantallas (Lista o Formulario)
                    var pantallaActual by remember { mutableStateOf("lista") }

                    when (pantallaActual) {
                        "lista" -> {
                            CandidataListScreen(
                                viewModel = listViewModel,
                                onAgregarClick = {
                                    formViewModel.limpiarFormulario() // Limpia los campos antes de abrir
                                    pantallaActual = "formulario"
                                }
                            )
                        }
                        "formulario" -> {
                            CandidataFormScreen(
                                viewModel = formViewModel,
                                onVolver = {
                                    formViewModel.limpiarFormulario() // Limpia al regresar
                                    pantallaActual = "lista"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}