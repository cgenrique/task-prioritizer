package com.example.taskprioritizer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false   // así evitamos warnings de schema
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}