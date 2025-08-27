package com.example.taskprioritizer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskprioritizer.App

class TaskViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(app.taskRepository, app.getPrioritizedTasks) as T
    }
}