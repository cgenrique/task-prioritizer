package com.example.taskprioritizer.domain.repository

import com.example.taskprioritizer.data.local.CompletedPerDay
import com.example.taskprioritizer.domain.model.Task
import kotlinx.coroutines.flow.Flow


/**
 * Interfaz de repositorio para la gestión de tareas.
 *
 * Define las operaciones disponibles desde la capa de dominio,
 * desacoplando la lógica de la fuente de datos concreta (Room en este caso).
 */
interface TaskRepository {
    // Observa todas las tareas (pendientes y completadas).
    fun observeAll(): Flow<List<Task>>

    //Observa solo las tareas pendientes.
    fun observePending(): Flow<List<Task>>

    // Observa solo las tareas completadas.
    fun observeCompleted(): Flow<List<Task>>

    // Inserta una nueva tarea y devuelve su id.
    suspend fun add(task: Task): Int

    // Actualiza una tarea existente.
    suspend fun update(task: Task)

    // Elimina una tarea.
    suspend fun delete(task: Task)

    /**
     * Cambia el estado de completado de una tarea.
     * @param id identificador de la tarea
     * @param completed nuevo estado
     * @param completedAtMillis marca temporal de completado (o null si se desmarca)
     */
    suspend fun setCompleted(id: Int, completed: Boolean, completedAtMillis: Long?)

    // Devuelve las tareas completadas agrupadas por día.
    fun completedPerDay(): Flow<List<CompletedPerDay>>
}