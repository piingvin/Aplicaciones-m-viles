package com.example.appsemestral.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appsemestral.ui.components.CampoValidado
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel

@Composable
fun CandidataFormScreen(
    viewModel: CandidataFormViewModel,
    onVolver: () -> Unit
) {
    // Observamos los estados desde el ViewModel
    val nombreState by viewModel.nombre.collectAsState()
    val correoState by viewModel.correo.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Nueva Candidatura",
                style = MaterialTheme.typography.headlineMedium
            )

            CampoValidado(
                valor = nombreState.valor,
                onValorChange = { viewModel.onNombreChange(it) },
                label = "Nombre Completo (Ej: Ana Pérez)",
                error = nombreState.error
            )

            CampoValidado(
                valor = correoState.valor,
                onValorChange = { viewModel.onCorreoChange(it) },
                label = "Correo Electrónico",
                error = correoState.error
            )

            Button(
                onClick = {
                    viewModel.guardarCandidata(onExito = onVolver)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Candidatura")
            }
        }
    }
}