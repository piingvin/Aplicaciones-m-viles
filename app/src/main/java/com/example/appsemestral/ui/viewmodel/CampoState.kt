package com.example.appsemestral.ui.viewmodel

import com.example.appsemestral.validation.ValidationResult

data class CampoState(
    val valor: String = "",
    val error: String? = null
) {
    fun con(nuevoValor: String, resultado: ValidationResult): CampoState {
        return this.copy(
            valor = nuevoValor,
            error = if (resultado is ValidationResult.Error) resultado.mensaje else null
        )
    }
}