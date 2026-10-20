package com.example.appsemestral.validation

// Clase auxiliar para el resultado de la validación
sealed class ValidationResult {
    object Valido : ValidationResult()
    data class Error(val mensaje: String) : ValidationResult()
}

object CandidataValidator {

    fun validarNombre(nombre: String): ValidationResult {
        return when {
            nombre.isBlank() -> ValidationResult.Error("El nombre no puede estar vacío")
            nombre.length < 3 -> ValidationResult.Error("Debe tener al menos 3 caracteres")
            else -> ValidationResult.Valido
        }
    }

    fun validarCorreo(correo: String): ValidationResult {
        // Expresión regular estándar para correos generales (Gmail, Duoc, etc.)
        val regexCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$".toRegex()

        return when {
            correo.isBlank() -> ValidationResult.Error("El correo no puede estar vacío")
            !correo.matches(regexCorreo) -> ValidationResult.Error("Ingrese un correo electrónico válido")
            else -> ValidationResult.Valido
        }
    }

    fun validarTelefono(telefono: String): ValidationResult {
        val regex = Regex("^\\+56 9 \\d{4} \\d{4}$")
        if (!regex.matches(telefono)) {
            return ValidationResult.Error("Formato inválido (Ej: +56 9 XXXX XXXX)")
        }
        return ValidationResult.Valido
    }
}
