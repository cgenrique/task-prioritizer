package com.example.taskprioritizer.domain.repository

import com.example.taskprioritizer.data.local.TaskDao
import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.model.toDomain
import com.example.taskprioritizer.domain.model.toEntity
import com.example.taskprioritizer.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación de [TaskRepository] que utiliza Room como fuente de datos.
 *
 * Recibe un [TaskDao] y adapta las entidades de base de datos [TaskEntity]
 * al modelo de dominio [Task].
 */
class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {

    override fun observeAll(): Flow<List<Task>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observePending(): Flow<List<Task>> =
        dao.observePending().map { list -> list.map { it.toDomain() } }

    override fun observeCompleted(): Flow<List<Task>> =
        dao.observeCompleted().map { list -> list.map { it.toDomain() } }

    override suspend fun add(task: Task): Int =
        dao.insert(task.toEntity()).toInt()

    override suspend fun update(task: Task) {
        dao.update(task.toEntity())
    }

    override suspend fun delete(task: Task) {
        dao.delete(task.toEntity())
    }

    override suspend fun setCompleted(id: Int, completed: Boolean, completedAtMillis: Long?) {
        dao.setCompleted(id, completed, completedAtMillis)
    }

    override fun completedPerDay() = dao.completedPerDay()
}