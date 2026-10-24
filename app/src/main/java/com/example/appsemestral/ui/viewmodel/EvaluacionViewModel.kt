package com.example.appsemestral.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsemestral.data.repository.CandidataRepository
import com.example.appsemestral.model.Candidata
import com.example.appsemestral.model.Evaluacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EvaluacionViewModel(
    private val repository: CandidataRepository
) : ViewModel() {

    val candidatas: StateFlow<List<Candidata>> = repository.listarCandidatas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _candidataSeleccionada = MutableStateFlow<Candidata?>(null)
    val candidataSeleccionada: StateFlow<Candidata?> = _candidataSeleccionada.asStateFlow()

    private val _puntajeExperiencia = MutableStateFlow(3)
    val puntajeExperiencia: StateFlow<Int> = _puntajeExperiencia.asStateFlow()

    private val _puntajeFormacion = MutableStateFlow(3)
    val puntajeFormacion: StateFlow<Int> = _puntajeFormacion.asStateFlow()

    private val _puntajeEntrevista = MutableStateFlow(3)
    val puntajeEntrevista: StateFlow<Int> = _puntajeEntrevista.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso.asStateFlow()

    fun seleccionarCandidata(candidata: Candidata) {
        _candidataSeleccionada.value = candidata
        _mensajeError.value = null
        _guardadoExitoso.value = false

        viewModelScope.launch {
            val evaluacionExistente = repository.buscarEvaluacionPorCandidata(candidata.id)
            if (evaluacionExistente != null) {
                _mensajeError.value = "Atención: Esta candidata ya tiene una evaluación registrada (${String.format("%.2f", evaluacionExistente.totalPonderado)} pts)."
            }
        }
    }

    fun onPuntajeExperienciaChange(nuevo: Int) {
        _puntajeExperiencia.value = nuevo
    }

    fun onPuntajeFormacionChange(nuevo: Int) {
        _puntajeFormacion.value = nuevo
    }

    fun onPuntajeEntrevistaChange(nuevo: Int) {
        _puntajeEntrevista.value = nuevo
    }

    fun calcularTotalPonderado(): Double {
        val exp = _puntajeExperiencia.value * 0.35
        val form = _puntajeFormacion.value * 0.35
        val ent = _puntajeEntrevista.value * 0.30
        return exp + form + ent
    }

    fun guardarEvaluacion() {
        val candidata = _candidataSeleccionada.value
        if (candidata == null) {
            _mensajeError.value = "Por favor, seleccione una candidata para evaluar"
            return
        }

        viewModelScope.launch {
            val evaluacionExistente = repository.buscarEvaluacionPorCandidata(candidata.id)
            if (evaluacionExistente != null) {
                _mensajeError.value = "No se puede duplicar la evaluación de ${candidata.nombre}"
                return@launch
            }

            val total = calcularTotalPonderado()
            val evaluacion = Evaluacion(
                candidataId = candidata.id,
                candidataNombre = candidata.nombre,
                puntajeExperiencia = _puntajeExperiencia.value,
                puntajeFormacion = _puntajeFormacion.value,
                puntajeEntrevista = _puntajeEntrevista.value,
                totalPonderado = total,
                fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            )

            repository.guardarEvaluacion(evaluacion)
            _guardadoExitoso.value = true
            _mensajeError.value = null
        }
    }

    fun resetearEstado() {
        _candidataSeleccionada.value = null
        _puntajeExperiencia.value = 3
        _puntajeFormacion.value = 3
        _puntajeEntrevista.value = 3
        _mensajeError.value = null
        _guardadoExitoso.value = false
    }
}
