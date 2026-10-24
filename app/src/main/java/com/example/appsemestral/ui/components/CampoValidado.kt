package com.example.appsemestral.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

private class UltimoMensaje { var texto: String = "" }

@Composable
fun CampoValidado(
    valor: String,
    onValorChange: (String) -> Unit,
    label: String,
    error: String?,
    modifier: Modifier = Modifier
) {
    // Guarda el último error para que el texto no desaparezca de golpe mientras sale animado
    val ultimo = remember { UltimoMensaje() }
    if (error != null) ultimo.texto = error
    val esValido = error == null && valor.isNotBlank()

    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = {
            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(text = ultimo.texto, color = MaterialTheme.colorScheme.error)
            }
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = error != null || esValido,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                if (error != null) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Campo válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}
