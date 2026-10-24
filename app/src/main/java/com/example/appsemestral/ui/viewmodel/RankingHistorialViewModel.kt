package com.example.appsemestral.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsemestral.data.repository.CandidataRepository
import com.example.appsemestral.model.Evaluacion
import com.example.appsemestral.model.HistorialAccion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class RankingHistorialViewModel(
    private val repository: CandidataRepository
) : ViewModel() {

    val rankingList: StateFlow<List<Evaluacion>> = repository.obtenerRanking()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val historialList: StateFlow<List<HistorialAccion>> = repository.listarHistorial()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
