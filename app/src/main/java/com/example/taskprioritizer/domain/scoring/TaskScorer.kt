package com.example.taskprioritizer.domain.scoring

import com.example.taskprioritizer.domain.model.Task
import kotlin.math.max
import kotlin.math.min

object TaskScorer {
    private const val W_PRIORITY = 0.4
    private const val W_URGENCY  = 0.4
    private const val W_SIZE     = 0.2

    fun score(task: Task, nowMillis: Long = System.currentTimeMillis()): Double {
        val priorityNorm = when (task.priority) {
            1 -> 1.0 / 3.0
            2 -> 2.0 / 3.0
            else -> 1.0
        }

        val urgency = task.deadlineMillis?.let { deadline ->
            val days = max(0.0, (deadline - nowMillis) / 86_400_000.0)
            1.0 / (1.0 + days) // 1 si vencida/ya, 0→ si falta mucho
        } ?: 0.2 // sin fecha: pequeña urgencia base

        val sizeBonus = 1 - min(1.0, task.estimateMinutes / 120.0) // quick wins

        return W_PRIORITY * priorityNorm + W_URGENCY * urgency + W_SIZE * sizeBonus
    }
}