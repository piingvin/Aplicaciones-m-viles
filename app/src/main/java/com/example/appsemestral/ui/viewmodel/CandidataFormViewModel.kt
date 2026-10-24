package com.example.appsemestral.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsemestral.data.repository.CandidataRepository
import com.example.appsemestral.model.Candidata
import com.example.appsemestral.validation.CandidataValidator
import com.example.appsemestral.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CandidataFormViewModel(
    private val repository: CandidataRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow(CampoState())
    val nombre: StateFlow<CampoState> = _nombre.asStateFlow()

    private val _correo = MutableStateFlow(CampoState())
    val correo: StateFlow<CampoState> = _correo.asStateFlow()

    private val _telefono = MutableStateFlow(CampoState())
    val telefono: StateFlow<CampoState> = _telefono.asStateFlow()

    private val _area = MutableStateFlow(CampoState())
    val area: StateFlow<CampoState> = _area.asStateFlow()

    private val _experiencia = MutableStateFlow(CampoState())
    val experiencia: StateFlow<CampoState> = _experiencia.asStateFlow()

    private val _resumen = MutableStateFlow(CampoState())
    val resumen: StateFlow<CampoState> = _resumen.asStateFlow()

    private val _uriCv = MutableStateFlow<String>("")
    val uriCv: StateFlow<String> = _uriCv.asStateFlow()

    private val _nombreCv = MutableStateFlow<String?>(null)
    val nombreCv: StateFlow<String?> = _nombreCv.asStateFlow()

    private var idActual: Int = 0

    fun onNombreChange(nuevoValor: String) {
        val resultado = CandidataValidator.validarNombre(nuevoValor)
        _nombre.update { it.con(nuevoValor, resultado) }
    }

    fun onCorreoChange(nuevoValor: String) {
        val resultado = CandidataValidator.validarCorreo(nuevoValor)
        _correo.update { it.con(nuevoValor, resultado) }
    }

    fun onTelefonoChange(nuevoValor: String) {
        val resultado = CandidataValidator.validarTelefono(nuevoValor)
        _telefono.update { it.con(nuevoValor, resultado) }
    }

    fun onAreaChange(nuevoValor: String) {
        _area.update { it.copy(valor = nuevoValor, error = if (nuevoValor.isBlank()) "Ingrese un área" else null) }
    }

    fun onExperienciaChange(nuevoValor: String) {
        val esValido = nuevoValor.toIntOrNull() != null
        _experiencia.update { it.copy(valor = nuevoValor, error = if (!esValido) "Ingrese un número" else null) }
    }

    fun onResumenChange(nuevoValor: String) {
        _resumen.update { it.copy(valor = nuevoValor, error = if (nuevoValor.isBlank()) "Ingrese un resumen" else null) }
    }

    fun onCvSeleccionado(uri: String, nombreArchivo: String?) {
        _uriCv.value = uri
        _nombreCv.value = nombreArchivo ?: "CV_Adjuntado.pdf"
    }

    fun cargarCandidata(candidata: Candidata) {
        idActual = candidata.id
        _nombre.value = CampoState(valor = candidata.nombre)
        _correo.value = CampoState(valor = candidata.correo)
        _telefono.value = CampoState(valor = candidata.telefono)
        _area.value = CampoState(valor = candidata.area)
        _experiencia.value = CampoState(valor = candidata.anosExperiencia.toString())
        _resumen.value = CampoState(valor = candidata.resumen)
        _uriCv.value = candidata.uriCv
        _nombreCv.value = if (candidata.uriCv.isNotBlank()) "CV Guardado" else null
    }

    fun limpiarFormulario() {
        idActual = 0
        _nombre.value = CampoState()
        _correo.value = CampoState()
        _telefono.value = CampoState()
        _area.value = CampoState()
        _experiencia.value = CampoState()
        _resumen.value = CampoState()
        _uriCv.value = ""
        _nombreCv.value = null
    }

    fun guardarCandidata(onExito: () -> Unit, onError: () -> Unit = {}) {
        // Validar antes de guardar
        onNombreChange(_nombre.value.valor)
        onCorreoChange(_correo.value.valor)
        onTelefonoChange(_telefono.value.valor)
        onAreaChange(_area.value.valor)
        onExperienciaChange(_experiencia.value.valor)
        onResumenChange(_resumen.value.valor)

        val sinErrores = _nombre.value.error == null && _correo.value.error == null &&
                _telefono.value.error == null && _area.value.error == null &&
                _experiencia.value.error == null && _resumen.value.error == null

        val camposLlenos = _nombre.value.valor.isNotBlank() && _correo.value.valor.isNotBlank()

        if (sinErrores && camposLlenos) {
            viewModelScope.launch {
                val candidata = Candidata(
                    id = idActual,
                    nombre = _nombre.value.valor,
                    correo = _correo.value.valor,
                    telefono = _telefono.value.valor,
                    area = _area.value.valor,
                    anosExperiencia = _experiencia.value.valor.toIntOrNull() ?: 0,
                    resumen = _resumen.value.valor,
                    enlacePerfil = "",
                    uriCv = _uriCv.value,
                    registradaPor = "Dirección"
                )
                if (idActual == 0) {
                    repository.registrarCandidata(candidata) // Es nueva
                } else {
                    repository.actualizarCandidata(candidata) // Es edición
                }
                onExito()
            }
        } else {
            onError() // avisa a la pantalla para que anime el botón
        }
    }
}