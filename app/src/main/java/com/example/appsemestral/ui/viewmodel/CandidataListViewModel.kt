package com.example.appsemestral.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsemestral.data.repository.CandidataRepository
import com.example.appsemestral.model.Candidata
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CandidataListViewModel(
    private val repository: CandidataRepository
) : ViewModel() {

    // Convierte el Flow del DAO en un StateFlow que la UI puede observar
    val candidatas: StateFlow<List<Candidata>> = repository.listarCandidatas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun eliminarCandidata(candidata: Candidata) {
        viewModelScope.launch {
            repository.eliminarCandidata(candidata)
        }
    }
}