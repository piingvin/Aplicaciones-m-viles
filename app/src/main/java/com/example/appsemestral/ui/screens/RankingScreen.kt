package com.example.appsemestral.ui.screens

/**
 * Pantalla de Ranking Consolidado WIM
 * Muestra a las candidatas evaluadas ordenadas descendentemente por su puntaje total ponderado.
 */
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appsemestral.model.Evaluacion
import com.example.appsemestral.ui.viewmodel.RankingHistorialViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    viewModel: RankingHistorialViewModel,
    onVolver: () -> Unit
) {
    val ranking by viewModel.rankingList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ranking Consolidado WIM") },
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
        if (ranking.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no hay candidatas evaluadas en el ranking.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                itemsIndexed(ranking, key = { _, e -> e.id }) { index, evaluacion ->
                    RankingCardItem(
                        posicion = index + 1,
                        evaluacion = evaluacion,
                        modifier = Modifier.animateItem() // las tarjetas se reordenan con animación
                    )
                }
            }
        }
    }
}

@Composable
fun RankingCardItem(
    posicion: Int,
    evaluacion: Evaluacion,
    modifier: Modifier = Modifier
) {
    val (medallaColor, podioTexto) = when (posicion) {
        1 -> Color(0xFFFFD700) to "🥇 1° LUGAR"
        2 -> Color(0xFFC0C0C0) to "🥈 2° LUGAR"
        3 -> Color(0xFFCD7F32) to "🥉 3° LUGAR"
        else -> MaterialTheme.colorScheme.outline to "$posicion° PUESTO"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = if (posicion <= 3) 4.dp else 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (posicion == 1) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Posición",
                        tint = medallaColor
                    )
                    Text(
                        text = podioTexto,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Puntaje",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${String.format(Locale.US, "%.2f", evaluacion.totalPonderado)} pts",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider()

            Text(
                text = evaluacion.candidataNombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Barra de puntaje que crece al aparecer la tarjeta
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }
            val progreso by animateFloatAsState(
                targetValue = if (visible) (evaluacion.totalPonderado / 5.0).toFloat().coerceIn(0f, 1f) else 0f,
                animationSpec = tween(900),
                label = "progresoRanking"
            )
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Exp: ${evaluacion.puntajeExperiencia}/5 | Form: ${evaluacion.puntajeFormacion}/5 | Ent: ${evaluacion.puntajeEntrevista}/5",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Evaluado el: ${evaluacion.fecha}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
