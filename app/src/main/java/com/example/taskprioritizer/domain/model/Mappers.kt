package com.example.taskprioritizer.domain.model

import com.example.taskprioritizer.data.local.TaskEntity

/**
 * Funciones de extensión para convertir entre [TaskEntity] (capa de datos)
 * y [Task] (capa de dominio).
 */

/**
 * Convierte un [TaskEntity] de base de datos en un objeto de dominio [Task].
 */
fun TaskEntity.toDomain() = Task(
    id, title, description, category, deadlineMillis,
    estimateMinutes, priority, createdAtMillis, completed, completedAtMillis
)

/**
 * Convierte un objeto de dominio [Task] en una entidad [TaskEntity]
 * lista para guardarse en la base de datos.
 */
fun Task.toEntity() = com.example.taskprioritizer.data.local.TaskEntity(
    id, title, description, category, deadlineMillis,
    estimateMinutes, priority, createdAtMillis, completed, completedAtMillis
)