package com.example.taskprioritizer

import android.app.Application
import androidx.room.Room
import com.example.taskprioritizer.data.local.AppDatabase

class App : Application() {
    // Service locator simple
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "task_db").build()
    }
}