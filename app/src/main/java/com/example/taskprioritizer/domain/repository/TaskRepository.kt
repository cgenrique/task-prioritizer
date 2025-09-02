package com.example.taskprioritizer.domain.repository

import com.example.taskprioritizer.domain.model.Task
import kotlinx.coroutines.flow.Flow


interface TaskRepository {
    fun observeAll(): Flow<List<Task>>
    fun observePending(): Flow<List<Task>>

    fun observeCompleted(): Flow<List<Task>>
    suspend fun add(task: Task): Int
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
    suspend fun setCompleted(id: Int, completed: Boolean, completedAtMillis: Long?)
}