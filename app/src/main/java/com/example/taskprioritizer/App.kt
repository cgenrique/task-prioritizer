package com.example.taskprioritizer

import android.app.Application
import androidx.room.Room
import com.example.taskprioritizer.data.local.AppDatabase
import com.example.taskprioritizer.domain.repository.TaskRepository
import com.example.taskprioritizer.domain.repository.TaskRepositoryImpl
import com.example.taskprioritizer.domain.usecase.GetPrioritizedTasks

/**
 * Clase [Application] de la app.
 *
 * Actúa como service locator sencillo para proveer dependencias:
 * - Base de datos (Room)
 * - Repositorio de tareas
 * - Caso de uso de priorización de tareas
 */
class App : Application() {
    // Base de datos local Room.
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "task_db").build()
    }

    // Repositorio de tareas basado en Room.
    val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(db.taskDao())
    }

    // Caso de uso para obtener tareas priorizadas.
    val getPrioritizedTasks: GetPrioritizedTasks by lazy {
        GetPrioritizedTasks(taskRepository)
    }
}