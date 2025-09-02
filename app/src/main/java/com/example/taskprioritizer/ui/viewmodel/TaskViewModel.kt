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

    val pendingTasks: StateFlow<List<Task>> =
        getPrioritizedTasks() // ya usaba observePending()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val completedTasks: StateFlow<List<Task>> =
        repo.observeCompleted()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val allTasks: StateFlow<List<Task>> =
        repo.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    // Temporal: meter 3 tareas de prueba para ver cómo ordena
    /*@Suppress("unused")
    fun addDummyData() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repo.add(Task(title = "Entrega práctica", priority = 3, deadlineMillis = now + 24*60*60*1000, estimateMinutes = 120))
            repo.add(Task(title = "Sacar la basura", priority = 1, estimateMinutes = 5))
            repo.add(Task(title = "Proyecto largo", priority = 2, deadlineMillis = now + 10*24*60*60*1000, estimateMinutes = 300))
        }
    }*/

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
}