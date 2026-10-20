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

    fun guardarCandidata(onExito: () -> Unit) {
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
                val nuevaCandidata = Candidata(
                    nombre = _nombre.value.valor,
                    correo = _correo.value.valor,
                    telefono = _telefono.value.valor,
                    area = _area.value.valor,
                    anosExperiencia = _experiencia.value.valor.toIntOrNull() ?: 0,
                    resumen = _resumen.value.valor,
                    enlacePerfil = "",
                    uriCv = "",
                    registradaPor = "Dirección"
                )
                repository.registrarCandidata(nuevaCandidata)
                onExito()
            }
        }
    }
}