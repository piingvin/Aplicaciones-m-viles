package com.example.appsemestral.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appsemestral.ui.viewmodel.EvaluacionViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionScreen(
    viewModel: EvaluacionViewModel,
    onVolver: () -> Unit
) {
    val candidatas by viewModel.candidatas.collectAsState()
    val candidataSeleccionada by viewModel.candidataSeleccionada.collectAsState()
    val puntajeExp by viewModel.puntajeExperiencia.collectAsState()
    val puntajeForm by viewModel.puntajeFormacion.collectAsState()
    val puntajeEnt by viewModel.puntajeEntrevista.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()
    val guardadoExitoso by viewModel.guardadoExitoso.collectAsState()

    var dropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(guardadoExitoso) {
        if (guardadoExitoso) {
            viewModel.resetearEstado()
            onVolver()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matriz de Evaluación WIM") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Evaluación de Candidata",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Selector de Candidata
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = candidataSeleccionada?.nombre ?: "Seleccionar Candidata...",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Candidata a evaluar") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    if (candidatas.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay candidatas registradas") },
                            onClick = { dropdownExpanded = false }
                        )
                    } else {
                        candidatas.forEach { candidata ->
                            DropdownMenuItem(
                                text = { Text("${candidata.nombre} (${candidata.area})") },
                                onClick = {
                                    viewModel.seleccionarCandidata(candidata)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Mensaje de Advertencia / Error
            if (mensajeError != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = mensajeError!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Criterios de Evaluación
            CriterioSliderItem(
                titulo = "1. Experiencia Laboral (35%)",
                valorActual = puntajeExp,
                onValorChange = { viewModel.onPuntajeExperienciaChange(it) }
            )

            CriterioSliderItem(
                titulo = "2. Formación y Conocimientos (35%)",
                valorActual = puntajeForm,
                onValorChange = { viewModel.onPuntajeFormacionChange(it) }
            )

            CriterioSliderItem(
                titulo = "3. Entrevista y Habilidades (30%)",
                valorActual = puntajeEnt,
                onValorChange = { viewModel.onPuntajeEntrevistaChange(it) }
            )

            // Tarjeta con Total Ponderado
            val totalPonderado = viewModel.calcularTotalPonderado()
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Puntaje Total",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Total Ponderado WIM:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${String.format(Locale.US, "%.2f", totalPonderado)} / 5.0",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Button(
                onClick = { viewModel.guardarEvaluacion() },
                enabled = candidataSeleccionada != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Evaluación WIM")
            }
        }
    }
}

@Composable
fun CriterioSliderItem(
    titulo: String,
    valorActual: Int,
    onValorChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$valorActual / 5",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = valorActual.toFloat(),
                onValueChange = { onValorChange(it.toInt()) },
                valueRange = 1f..5f,
                steps = 3 // Valores discretos: 1, 2, 3, 4, 5
            )
        }
    }
}
