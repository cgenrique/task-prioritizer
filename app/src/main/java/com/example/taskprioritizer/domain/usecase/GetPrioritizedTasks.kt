package com.example.taskprioritizer.domain.usecase

import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.repository.TaskRepository
import com.example.taskprioritizer.domain.scoring.TaskScorer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPrioritizedTasks(
    private val repo: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> =
        repo.observePending().map { tasks ->
            tasks.sortedByDescending { TaskScorer.score(it) }
        }
}