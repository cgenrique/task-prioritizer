package com.example.taskprioritizer.domain.model

import com.example.taskprioritizer.data.local.TaskEntity

fun TaskEntity.toDomain() = Task(
    id, title, description, category, deadlineMillis,
    estimateMinutes, priority, createdAtMillis, completed
)

fun Task.toEntity() = com.example.taskprioritizer.data.local.TaskEntity(
    id, title, description, category, deadlineMillis,
    estimateMinutes, priority, createdAtMillis, completed
)