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

    fun onNombreChange(nuevoValor: String) {
        val resultado = CandidataValidator.validarNombre(nuevoValor)
        _nombre.update { it.con(nuevoValor, resultado) }
    }

    fun onCorreoChange(nuevoValor: String) {
        val resultado = CandidataValidator.validarCorreo(nuevoValor)
        _correo.update { it.con(nuevoValor, resultado) }
    }

    fun guardarCandidata(onExito: () -> Unit) {
        val nombreValido = CandidataValidator.validarNombre(_nombre.value.valor) is ValidationResult.Valido
        val correoValido = CandidataValidator.validarCorreo(_correo.value.valor) is ValidationResult.Valido

        if (nombreValido && correoValido) {
            viewModelScope.launch {
                val nuevaCandidata = Candidata(
                    nombre = _nombre.value.valor,
                    correo = _correo.value.valor,
                    telefono = "+56 9 1234 5678", // Faltan campos en el form, los completaremos luego
                    area = "Desarrollo",
                    anosExperiencia = 3,
                    resumen = "Resumen de prueba",
                    enlacePerfil = "https://www.linkedin.com/in/prueba",
                    uriCv = "",
                    registradaPor = "Dirección"
                )
                repository.registrarCandidata(nuevaCandidata)
                onExito()
            }
        }
    }
}