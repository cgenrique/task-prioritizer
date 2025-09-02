package com.example.taskprioritizer.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val category: String = "General",
    val deadlineMillis: Long? = null,
    val estimateMinutes: Int = 30,
    val priority: Int = 2,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val completedAtMillis: Long? = null

)
