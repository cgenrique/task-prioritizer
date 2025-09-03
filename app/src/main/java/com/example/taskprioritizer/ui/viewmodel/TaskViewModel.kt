package com.example.taskprioritizer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskprioritizer.data.local.CompletedPerDay
import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.repository.TaskRepository
import com.example.taskprioritizer.domain.usecase.GetPrioritizedTasks
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.map


/**
 * ViewModel principal que gestiona el estado de la lista de tareas.
 *
 * Funciones principales:
 * - Exponer listas de tareas (todas, pendientes, completadas).
 * - Añadir, actualizar y eliminar tareas.
 * - Marcar tareas como completadas.
 * - Eliminar automáticamente tareas completadas hace más de 7 días.
 * - Proveer estadísticas de tareas completadas por día.
 */
class TaskViewModel(
    private val repo: TaskRepository,
    getPrioritizedTasks: GetPrioritizedTasks
) : ViewModel() {

    // Lista de tareas pendientes, priorizadas según el score.
    val pendingTasks: StateFlow<List<Task>> =
        getPrioritizedTasks() // ya usaba observePending()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Lista de tareas completadas.
    val completedTasks: StateFlow<List<Task>> =
        repo.observeCompleted()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Lista de todas las tareas (pendientes + completadas).
    val allTasks: StateFlow<List<Task>> =
        repo.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Añadir una nueva tarea.
    fun addTask(task: Task) {
        viewModelScope.launch {
            repo.add(task)
        }
    }

    // Añadir una nueva tarea.
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repo.update(task)
        }
    }

     // Eliminar una tarea existente.
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }

    /**
     * Marcar una tarea como completada o desmarcarla.
     * Si está completada desde hace más de 7 días, se elimina automáticamente.
     */
    fun setCompleted(id: Int, completed: Boolean) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val completedAt = if (completed) now else null

            repo.setCompleted(id, completed, completedAt)

            // 🔴 Borrar tareas completadas hace más de 7 días
            val sevenDays = 7 * 24 * 60 * 60 * 1000L
            val oldCompleted = completedTasks.value.filter {
                it.completed && it.completedAtMillis != null && now - it.completedAtMillis > sevenDays
            }
            oldCompleted.forEach { repo.delete(it) }
        }
    }

    // Estadísticas de tareas completadas agrupadas por día.
    val completedStats: StateFlow<List<CompletedPerDay>> =
        repo.completedPerDay()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

}