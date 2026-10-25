package com.example.appsemestral.validation

// Clase auxiliar para el resultado de la validación
sealed class ValidationResult {
    object Valido : ValidationResult()
    data class Error(val mensaje: String) : ValidationResult()
}

object CandidataValidator {

    fun validarNombre(nombre: String): ValidationResult {
        val regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$".toRegex()

        return when {
            nombre.isBlank() -> ValidationResult.Error("El nombre no puede estar vacio")
            nombre.length < 3 -> ValidationResult.Error("Debe tener al menos 3 caracteres")
            !nombre.matches(regexNombre) -> ValidationResult.Error("Solo se permiten letras y espacios")
            else -> ValidationResult.Valido
        }
    }

    fun validarCorreo(correo: String): ValidationResult {
        // Expresión regular estándar para correos generales (Gmail, Duoc, etc.)
        val regexCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

        return when {
            correo.isBlank() -> ValidationResult.Error("El correo no puede estar vacío")
            !correo.matches(regexCorreo) -> ValidationResult.Error("Ingrese un correo electrónico válido")
            else -> ValidationResult.Valido
        }
    }

    fun validarTelefono(telefono: String): ValidationResult {
        // Solo validamos que sean exactamente 8 dígitos
        val regex = Regex("^\\d{8}$")
        return if (regex.matches(telefono)) {
            ValidationResult.Valido
        } else {
            ValidationResult.Error("Debe contener 8 dígitos")
        }
    }
}
