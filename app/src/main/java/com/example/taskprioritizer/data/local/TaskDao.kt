package com.example.taskprioritizer.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DTO auxiliar para representar el número de tareas completadas por día.
 */
data class CompletedPerDay(val day: String, val count: Int)

/**
 * DAO (Data Access Object) para la entidad [TaskEntity].
 *
 * Define las operaciones CRUD sobre la tabla de tareas,
 * así como consultas específicas para tareas pendientes,
 * completadas y estadísticas de uso.
 */
@Dao
interface TaskDao {

    // Observa todas las tareas, ordenadas por fecha de creación descendente.

    @Query("SELECT * FROM tasks ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    //Observa únicamente las tareas pendientes (no completadas).
    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY createdAtMillis DESC")
    fun observePending(): Flow<List<TaskEntity>>

    // Observa únicamente las tareas completadas.
    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY createdAtMillis DESC")
    fun observeCompleted(): Flow<List<TaskEntity>>

    /**
     * Inserta una nueva tarea.
     * Si ya existe una con el mismo id, se reemplaza.
     * @return id de la fila insertada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    // Actualiza una tarea existente.
    @Update
    suspend fun update(task: TaskEntity)

    // Elimina una tarea.
    @Delete
    suspend fun delete(task: TaskEntity)


    /**
     * Marca una tarea como completada o no completada,
     * registrando la marca de tiempo si corresponde.
     */
    @Query("UPDATE tasks SET completed = :completed, completedAtMillis = :completedAtMillis WHERE id = :id")
    suspend fun setCompleted(id: Int, completed: Boolean, completedAtMillis: Long?)


    /**
     * Obtiene el número de tareas completadas agrupadas por día.
     * Devuelve un flujo reactivo que se actualiza al cambiar los datos.
     */
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