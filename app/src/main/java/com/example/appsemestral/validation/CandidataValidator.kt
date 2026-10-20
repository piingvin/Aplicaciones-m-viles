package com.example.appsemestral.validation

// Clase auxiliar para el resultado de la validación
sealed class ValidationResult {
    object Valido : ValidationResult()
    data class Error(val mensaje: String) : ValidationResult()
}

object CandidataValidator {

    fun validarNombre(nombre: String): ValidationResult {
        if (nombre.isBlank()) return ValidationResult.Error("El nombre no puede estar vacío")
        if (nombre.length !in 3..60) return ValidationResult.Error("Debe tener entre 3 y 60 caracteres")
        if (!nombre.contains(" ")) return ValidationResult.Error("Debe incluir nombre y apellido")
        return ValidationResult.Valido
    }

    fun validarCorreo(correo: String): ValidationResult {
        if (correo.isBlank()) return ValidationResult.Error("El correo es obligatorio")
        val dominiosPermitidos = listOf("ejemplo.cl", "example.com", "test.cl")
        val dominio = correo.substringAfter('@', "")
        if (dominio !in dominiosPermitidos) {
            return ValidationResult.Error("Dominio no permitido (use ejemplo.cl, example.com o test.cl)")
        }
        return ValidationResult.Valido
    }

    fun validarTelefono(telefono: String): ValidationResult {
        val regex = Regex("^\\+56 9 \\d{4} \\d{4}$")
        if (!regex.matches(telefono)) {
            return ValidationResult.Error("Formato inválido (Ej: +56 9 XXXX XXXX)")
        }
        return ValidationResult.Valido
    }
}