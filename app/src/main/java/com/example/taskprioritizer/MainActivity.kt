package com.example.taskprioritizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskprioritizer.ui.theme.TaskPrioritizerTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskprioritizer.ui.screens.TaskListScreen
import com.example.taskprioritizer.ui.theme.TaskPrioritizerTheme
import com.example.taskprioritizer.ui.viewmodel.TaskViewModel
import com.example.taskprioritizer.ui.viewmodel.TaskViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskPrioritizerTheme {
                val vm: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(application as App)
                )
                val tasks by vm.tasks.collectAsStateWithLifecycle()

                TaskListScreen(
                    tasks = tasks,
                    onAddSamples = { vm.addDummyData() }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskPrioritizerTheme {
        Greeting("Android")
    }
}