package com.example.appsemestral

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.activity.compose.setContent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.appsemestral.ui.WimApp
import com.example.appsemestral.ui.theme.AppKotlinTheme
import com.example.appsemestral.ui.viewmodel.CandidataFormViewModel
import com.example.appsemestral.ui.viewmodel.CandidataListViewModel
import com.example.appsemestral.ui.viewmodel.EvaluacionViewModel
import com.example.appsemestral.ui.viewmodel.RankingHistorialViewModel
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val appContainer by lazy { (application as WimApplication).container }

    // Android crea y conserva los ViewModels (sobreviven a la rotación de pantalla)
    private val factory by lazy {
        viewModelFactory {
            initializer { CandidataFormViewModel(appContainer.candidataRepository) }
            initializer { CandidataListViewModel(appContainer.candidataRepository) }
            initializer { EvaluacionViewModel(appContainer.candidataRepository) }
            initializer { RankingHistorialViewModel(appContainer.candidataRepository) }
        }
    }
    private val formViewModel: CandidataFormViewModel by viewModels { factory }
    private val listViewModel: CandidataListViewModel by viewModels { factory }
    private val evaluacionViewModel: EvaluacionViewModel by viewModels { factory }
    private val rankingViewModel: RankingHistorialViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        executor = ContextCompat.getMainExecutor(this)

        setContent {
            AppKotlinTheme {
                // rememberSaveable: no vuelve a pedir la huella al rotar el celular
                var estaAutenticado by rememberSaveable { mutableStateOf(false) }

                // remember: la biometría se configura una sola vez, no en cada recomposición
                remember { ConfigurarBiometria { estaAutenticado = true } }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!estaAutenticado) {
                        PantallaBloqueo(onAutenticarClick = { mostrarPromptBiometrico() })
                    } else {
                        WimApp(
                            formViewModel = formViewModel,
                            listViewModel = listViewModel,
                            evaluacionViewModel = evaluacionViewModel,
                            rankingViewModel = rankingViewModel
                        )
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