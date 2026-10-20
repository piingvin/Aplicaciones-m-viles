package com.example.appsemestral.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    val nombreState by viewModel.nombre.collectAsState()
    val correoState by viewModel.correo.collectAsState()
    val telefonoState by viewModel.telefono.collectAsState()
    val areaState by viewModel.area.collectAsState()
    val experienciaState by viewModel.experiencia.collectAsState()
    val resumenState by viewModel.resumen.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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

            CampoValidado(
                valor = telefonoState.valor,
                onValorChange = { viewModel.onTelefonoChange(it) },
                label = "Teléfono (+56 9 XXXX XXXX)",
                error = telefonoState.error
            )

            CampoValidado(
                valor = areaState.valor,
                onValorChange = { viewModel.onAreaChange(it) },
                label = "Área (Ej: Desarrollo, UI/UX)",
                error = areaState.error
            )

            CampoValidado(
                valor = experienciaState.valor,
                onValorChange = { viewModel.onExperienciaChange(it) },
                label = "Años de Experiencia",
                error = experienciaState.error
            )

            CampoValidado(
                valor = resumenState.valor,
                onValorChange = { viewModel.onResumenChange(it) },
                label = "Resumen del perfil",
                error = resumenState.error
            )

            Button(
                onClick = { viewModel.guardarCandidata(onExito = onVolver) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Candidatura")
            }
        }
    }
}