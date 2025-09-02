package com.example.taskprioritizer.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DTOs para los resultados
data class CompletedPerDay(val day: String, val count: Int)

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY createdAtMillis DESC")
    fun observePending(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY createdAtMillis DESC")
    fun observeCompleted(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("UPDATE tasks SET completed = :completed, completedAtMillis = :completedAtMillis WHERE id = :id")
    suspend fun setCompleted(id: Int, completed: Boolean, completedAtMillis: Long?)


    // 📊 Tareas completadas por día
    @Query("""
        SELECT strftime('%d/%m', completedAtMillis/1000, 'unixepoch') as day, 
               COUNT(*) as count
        FROM tasks
        WHERE completed = 1 AND completedAtMillis IS NOT NULL
        GROUP BY day
        ORDER BY completedAtMillis
    """)
    fun completedPerDay(): Flow<List<CompletedPerDay>>
}