package com.example.appsemestral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.appsemestral.ui.screens.CandidataFormScreen
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel

class MainActivity : ComponentActivity() { // Ojo, la guía dice FragmentActivity para biometría, pero usaremos ComponentActivity temporalmente para que pruebes tu parte
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtenemos el repositorio desde nuestro contenedor
        val appContainer = (application as WimApplication).container
        val viewModel = CandidataFormViewModel(appContainer.candidataRepository)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CandidataFormScreen(
                        viewModel = viewModel,
                        onVolver = { /* Por ahora no hace nada, luego agregaremos navegación */ }
                    )
                }
            }
        }
    }
}