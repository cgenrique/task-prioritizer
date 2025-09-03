package com.example.taskprioritizer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una tarea en la base de datos local.
 *
 * Cada tarea contiene información básica como título, descripción,
 * prioridad, duración estimada, fecha límite y estado de completado.
 */
@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // identificador único autogenerado
    val title: String,                                // título de la tarea
    val description: String? = null,                  // descripción opcional
    val category: String = "General",                 // categoría de la tarea
    val deadlineMillis: Long? = null,                 // fecha/hora límite en milisegundos desde epoch
    val estimateMinutes: Int = 30,                    // duración estimada en minutos
    val priority: Int = 2,                            // 1=baja, 2=media, 3=alta
    val createdAtMillis: Long = System.currentTimeMillis(), // fecha de creación
    val completed: Boolean = false,                   // estado: true si completada
    val completedAtMillis: Long? = null               // marca de tiempo al completarse
)