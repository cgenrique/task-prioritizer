package com.example.taskprioritizer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base de datos principal de la aplicación.
 *
 * Usa Room para gestionar la tabla de tareas definida en [TaskEntity].
 * Expone un único DAO: [TaskDao].
 */
@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false   // así evitamos warnings de schema
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}