package com.example.appsemestral

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.appsemestral.ui.screens.CandidataFormScreen
import com.example.appsemestral.ui.screens.CandidataListScreen
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel
import com.example.appsemestral.ui.viewmodel.CandidataListViewModel
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as WimApplication).container
        val formViewModel = CandidataFormViewModel(appContainer.candidataRepository)
        val listViewModel = CandidataListViewModel(appContainer.candidataRepository)

        executor = ContextCompat.getMainExecutor(this)

        setContent {
            MaterialTheme {
                // Estado para saber si la app está bloqueada o desbloqueada
                var estaAutenticado by remember { mutableStateOf(false) }

                // Configuramos la biometría y qué hacer cuando sea exitosa
                ConfigurarBiometria { estaAutenticado = true }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!estaAutenticado) {
                        // Muestra la pantalla de bloqueo
                        PantallaBloqueo(
                            onAutenticarClick = { mostrarPromptBiometrico() }
                        )
                    } else {
                        // Muestra tu aplicación normal
                        var pantallaActual by remember { mutableStateOf("lista") }

                        when (pantallaActual) {
                            "lista" -> {
                                CandidataListScreen(
                                    viewModel = listViewModel,
                                    onAgregarClick = {
                                        formViewModel.limpiarFormulario()
                                        pantallaActual = "formulario"
                                    },
                                    onEditarClick = { candidataSeleccionada ->
                                        formViewModel.cargarCandidata(candidataSeleccionada) // Carga los datos existentes
                                        pantallaActual = "formulario" // Abre el form
                                    }
                                )
                            }
                            "formulario" -> {
                                CandidataFormScreen(
                                    viewModel = formViewModel,
                                    onVolver = {
                                        formViewModel.limpiarFormulario()
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

    private fun ConfigurarBiometria(onExito: () -> Unit) {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "¡Autenticación exitosa!", Toast.LENGTH_SHORT).show()
                    onExito() // Cambia el estado para mostrar la app
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de sesión seguro")
            .setSubtitle("Usa tu huella para acceder a las candidatas")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun mostrarPromptBiometrico() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "El dispositivo no tiene lector de huellas", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Lector de huellas no disponible", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No tienes huellas registradas en tu celular", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error biométrico desconocido", Toast.LENGTH_LONG).show()
            }
        }
    }
}

// Pantalla inicial antes de entrar
@Composable
fun PantallaBloqueo(onAutenticarClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "App Privada",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAutenticarClick) {
            Text("Ingresar con Biometría")
        }
    }
}