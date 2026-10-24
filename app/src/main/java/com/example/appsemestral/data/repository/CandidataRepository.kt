package com.example.appsemestral.data.repository

import com.example.appsemestral.data.local.dao.CandidataDao
import com.example.appsemestral.data.local.dao.EvaluacionDao
import com.example.appsemestral.data.local.dao.HistorialDao
import com.example.appsemestral.model.Candidata
import com.example.appsemestral.model.Evaluacion
import com.example.appsemestral.model.HistorialAccion
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CandidataRepository(
    private val candidataDao: CandidataDao,
    private val evaluacionDao: EvaluacionDao,
    private val historialDao: HistorialDao
) {

    private fun fechaActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    // --- Candidatas ---
    fun listarCandidatas(): Flow<List<Candidata>> = candidataDao.listarCandidatas()

    suspend fun buscarCandidata(id: Int): Candidata? = candidataDao.buscarPorId(id)

    suspend fun registrarCandidata(candidata: Candidata): Long {
        val id = candidataDao.insertar(candidata)
        historialDao.insertar(
            HistorialAccion(
                accion = "Registro de Candidata",
                descripcion = "Se registró a la candidata ${candidata.nombre} en el área ${candidata.area}",
                fecha = fechaActual()
            )
        )
        return id
    }

    suspend fun actualizarCandidata(candidata: Candidata) {
        candidataDao.actualizar(candidata)
        historialDao.insertar(
            HistorialAccion(
                accion = "Edición de Candidata",
                descripcion = "Se actualizaron los datos de ${candidata.nombre}",
                fecha = fechaActual()
            )
        )
    }

    suspend fun eliminarCandidata(candidata: Candidata) {
        candidataDao.eliminar(candidata)
        historialDao.insertar(
            HistorialAccion(
                accion = "Eliminación",
                descripcion = "Se eliminó a la candidata ${candidata.nombre}",
                fecha = fechaActual()
            )
        )
    }

    // --- Evaluaciones WIM ---
    fun obtenerRanking(): Flow<List<Evaluacion>> = evaluacionDao.obtenerRanking()

    suspend fun buscarEvaluacionPorCandidata(candidataId: Int): Evaluacion? {
        return evaluacionDao.buscarPorCandidataId(candidataId)
    }

    suspend fun guardarEvaluacion(evaluacion: Evaluacion): Long {
        val id = evaluacionDao.insertar(evaluacion)
        historialDao.insertar(
            HistorialAccion(
                accion = "Evaluación WIM",
                descripcion = "Se evaluó a ${evaluacion.candidataNombre} con puntaje total ${String.format(Locale.US, "%.2f", evaluacion.totalPonderado)}",
                fecha = fechaActual()
            )
        )
        return id
    }

    // --- Historial ---
    fun listarHistorial(): Flow<List<HistorialAccion>> = historialDao.listarHistorial()
}
