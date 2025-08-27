package com.example.taskprioritizer

import android.app.Application
import androidx.room.Room
import com.example.taskprioritizer.data.local.AppDatabase
import com.example.taskprioritizer.domain.repository.TaskRepository
import com.example.taskprioritizer.domain.repository.TaskRepositoryImpl
import com.example.taskprioritizer.domain.usecase.GetPrioritizedTasks
class App : Application() {
    // Service locator simple
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "task_db").build()
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(db.taskDao())
    }

    val getPrioritizedTasks: GetPrioritizedTasks by lazy {
        GetPrioritizedTasks(taskRepository)
    }
}