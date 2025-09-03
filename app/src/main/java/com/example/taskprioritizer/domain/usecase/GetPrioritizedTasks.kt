package com.example.taskprioritizer.domain.usecase

import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.repository.TaskRepository
import com.example.taskprioritizer.domain.scoring.TaskScorer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Caso de uso para obtener las tareas pendientes ordenadas por prioridad.
 *
 * Utiliza el repositorio [TaskRepository] y la lógica de [TaskScorer]
 * para devolver las tareas ordenadas de mayor a menor score.
 */
class GetPrioritizedTasks(
    private val repo: TaskRepository
) {
    /**
     * Devuelve un flujo de tareas pendientes priorizadas.
     */
    operator fun invoke(): Flow<List<Task>> =
        repo.observePending().map { tasks ->
            tasks.sortedByDescending { TaskScorer.score(it) }
        }
}