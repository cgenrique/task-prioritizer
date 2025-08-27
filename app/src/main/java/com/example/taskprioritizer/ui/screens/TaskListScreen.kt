package com.example.taskprioritizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.scoring.TaskScorer
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    onAddClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { inner ->
        if (tasks.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(inner).padding(24.dp)) {
                Text("No hay tareas. Pulsa + para crear ejemplos.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskRow(task)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun TaskRow(task: Task) {
    val score = TaskScorer.score(task) // solo para mostrar; la ordenación ya viene hecha
    Column(Modifier.fillMaxWidth().padding(8.dp)) {
        Text(task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        Text(
            "Prioridad: ${task.priority} · Estimación: ${task.estimateMinutes} min" +
                    (task.deadlineMillis?.let { " · Deadline: ${dateFormat.format(it)}" } ?: "")
        )
        Spacer(Modifier.height(2.dp))
        Text("Score: ${"%.3f".format(score)}")
    }
}