package com.example.taskprioritizer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val category: String = "General",
    val deadlineMillis: Long? = null,
    val estimateMinutes: Int = 30,
    val priority: Int = 2,                // 1=baja, 2=media, 3=alta
    val createdAtMillis: Long = System.currentTimeMillis(),
    val completed: Boolean = false
)