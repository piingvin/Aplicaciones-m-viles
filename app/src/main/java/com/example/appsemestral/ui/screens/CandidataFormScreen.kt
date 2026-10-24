package com.example.appsemestral.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import com.example.appsemestral.ui.components.CampoValidado
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel
import com.example.appsemestral.util.FilePickerUtils

@Composable
fun CandidataFormScreen(
    viewModel: CandidataFormViewModel,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    // Animación: el botón "tiembla" cuando el formulario tiene errores
    val scope = rememberCoroutineScope()
    val sacudida = remember { Animatable(0f) }
    val sacudirBoton: () -> Unit = {
        scope.launch {
            repeat(3) {
                sacudida.animateTo(14f, tween(45))
                sacudida.animateTo(-14f, tween(45))
            }
            sacudida.animateTo(0f, tween(45))
        }
    }

    val nombreState by viewModel.nombre.collectAsState()
    val correoState by viewModel.correo.collectAsState()
    val telefonoState by viewModel.telefono.collectAsState()
    val areaState by viewModel.area.collectAsState()
    val experienciaState by viewModel.experiencia.collectAsState()
    val resumenState by viewModel.resumen.collectAsState()
    val nombreCv by viewModel.nombreCv.collectAsState()

    // Selector nativo de archivos (ej: PDF, DOC, etc.)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val nombreArchivo = FilePickerUtils.obtenerNombreArchivo(context, uri)
            viewModel.onCvSeleccionado(uri.toString(), nombreArchivo)
        }
    }

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

            // Sección para Adjuntar Curriculum Vitae (CV)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Curriculum Vitae (CV)",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (nombreCv != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "CV Adjunto",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Adjuntado: $nombreCv",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Text(
                            text = "No se ha adjuntado ningún archivo CV.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedButton(
                        onClick = { filePickerLauncher.launch("*/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AttachFile,
                            contentDescription = "Adjuntar CV"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (nombreCv == null) "Adjuntar Documento CV" else "Cambiar Documento CV")
                    }
                }
            }

            Button(
                onClick = { viewModel.guardarCandidata(onExito = onVolver, onError = sacudirBoton) },
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(sacudida.value.roundToInt(), 0) }
            ) {
                Text("Guardar Candidatura")
            }
        }
    }
}

