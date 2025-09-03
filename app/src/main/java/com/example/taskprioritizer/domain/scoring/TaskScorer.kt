package com.example.taskprioritizer.domain.scoring

import com.example.taskprioritizer.domain.model.Task
import kotlin.math.max
import kotlin.math.min

/**
 * Objeto encargado de calcular la puntuación (score) de una tarea.
 *
 * El score combina tres factores con distintos pesos:
 * - Prioridad asignada por el usuario (40%)
 * - Urgencia según la fecha límite (40%)
 * - Tamaño estimado de la tarea en minutos (20%)
 *
 * La puntuación resultante se utiliza para clasificar las tareas
 * en categorías como "Muy urgente", "Urgente", "Normal" o "Sin prisa".
 */
object TaskScorer {
    private const val W_PRIORITY = 0.4
    private const val W_URGENCY  = 0.4
    private const val W_SIZE     = 0.2

    /**
     * Calcula la puntuación de una tarea [Task].
     *
     * @param task tarea a evaluar
     * @param nowMillis instante actual en milisegundos (por defecto, hora del sistema)
     * @return score entre 0 y 1 (cuanto mayor, más urgente/prioritaria)
     */
    fun score(task: Task, nowMillis: Long = System.currentTimeMillis()): Double {
        // Normalización de prioridad (1= baja, 2= media, 3= alta)
        val priorityNorm = when (task.priority) {
            1 -> 1.0 / 3.0
            2 -> 2.0 / 3.0
            else -> 1.0
        }

        // Urgencia en función de la fecha límite
        val urgency = task.deadlineMillis?.let { deadline ->
            val days = max(0.0, (deadline - nowMillis) / 86_400_000.0)
            1.0 / (1.0 + days) // 1 si vencida/ya, 0→ si falta mucho
        } ?: 0.2 // sin fecha: pequeña urgencia base

        // Bonus si la tarea es corta (quick win)
        val sizeBonus = 1 - min(1.0, task.estimateMinutes / 120.0) // quick wins

        return W_PRIORITY * priorityNorm + W_URGENCY * urgency + W_SIZE * sizeBonus
    }
}