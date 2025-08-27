package com.example.taskprioritizer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.repository.TaskRepository
import com.example.taskprioritizer.domain.usecase.GetPrioritizedTasks
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repo: TaskRepository,
    getPrioritizedTasks: GetPrioritizedTasks
) : ViewModel() {

    // Lista ya ordenada por score (flujo que observa la BD)
    val tasks: StateFlow<List<Task>> =
        getPrioritizedTasks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Temporal: meter 3 tareas de prueba para ver cómo ordena
    @Suppress("unused")
    fun addDummyData() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repo.add(Task(title = "Entrega práctica", priority = 3, deadlineMillis = now + 24*60*60*1000, estimateMinutes = 120))
            repo.add(Task(title = "Sacar la basura", priority = 1, estimateMinutes = 5))
            repo.add(Task(title = "Proyecto largo", priority = 2, deadlineMillis = now + 10*24*60*60*1000, estimateMinutes = 300))
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repo.add(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repo.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }

    fun setCompleted(id: Int, completed: Boolean) {
        viewModelScope.launch {
            repo.setCompleted(id, completed)

            // Borrar si lleva +7 días completada
            val sevenDays = 7 * 24 * 60 * 60 * 1000L
            val now = System.currentTimeMillis()
            tasks.value.filter { it.completed && now - it.createdAtMillis > sevenDays }
                .forEach { repo.delete(it) }
        }
    }
}