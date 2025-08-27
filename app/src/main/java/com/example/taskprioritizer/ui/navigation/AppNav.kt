package com.example.taskprioritizer.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskprioritizer.App
import com.example.taskprioritizer.ui.screens.AddEditTaskScreen
import com.example.taskprioritizer.ui.screens.TaskListScreen
import com.example.taskprioritizer.ui.viewmodel.TaskViewModel
import com.example.taskprioritizer.ui.viewmodel.TaskViewModelFactory

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()

    // 👇 cogemos la Application desde el contexto, sin singletons
    val app = LocalContext.current.applicationContext as App
    val vm: TaskViewModel = viewModel(factory = TaskViewModelFactory(app))

    NavHost(navController, startDestination = "list", modifier = modifier) {
        composable("list") {
            val tasks by vm.tasks.collectAsStateWithLifecycle()
            TaskListScreen(
                tasks = tasks,
                onAddClick = { navController.navigate("add") },
                onEdit = { task ->
                    navController.navigate("edit/${task.id}")
                },
                onDelete = { task -> vm.deleteTask(task) },
                onToggleCompleted = { task -> vm.setCompleted(task.id, !task.completed) }
            )
        }
        composable("add") {
            AddEditTaskScreen(
                task = null,
                onSave = {
                    vm.addTask(it)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val tasks by vm.tasks.collectAsStateWithLifecycle()
            val taskToEdit = tasks.find { it.id == id }

            if (taskToEdit == null) {
                Text("Tarea no encontrada")
            } else {
                AddEditTaskScreen(
                    task = taskToEdit,
                    onSave = {
                        vm.updateTask(it.copy(id = id!!))
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}