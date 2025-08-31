package com.example.taskprioritizer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.scoring.TaskScorer
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    allTasks: List<Task>,
    pendingTasks: List<Task>,
    completedTasks: List<Task>,
    onAddClick: () -> Unit,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onToggleCompleted: (Task) -> Unit
) {

    var filter by remember { mutableStateOf("Pendientes") }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(pendingTasks) {
        isLoading = false
    }


    val filteredTasks = when (filter) {
        "Pendientes" -> pendingTasks
        "Completadas" -> completedTasks
        "Urgentes" -> pendingTasks.filter {
            TaskScorer.score(it) >= 0.6
        }
        else -> allTasks
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas") },
                actions = {
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text("Filtro: $filter")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("Pendientes", "Completadas", "Urgentes").forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        filter = it
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { inner ->
        when {
            isLoading -> {
                // ⏳ solo al principio cuando aún no hay datos
                Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // 📭 Caso: no hay tareas para este filtro
            filteredTasks.isEmpty() -> {
                Box(
                    Modifier.fillMaxSize().padding(inner),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay tareas para este filtro.")
                }
            }

            // ✅ Caso normal: mostrar lista filtrada
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner)
                        .padding(8.dp)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskRow(
                            task = task,
                            onEdit = onEdit,
                            onDelete = onDelete,
                            onToggleCompleted = onToggleCompleted
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onToggleCompleted: (Task) -> Unit
) {
    val score = TaskScorer.score(task)

    val urgencyLabel = when {
        task.deadlineMillis == null -> "🟢 Sin prisa"
        score >= 0.8 -> "🔴 Muy urgente"
        score >= 0.6 -> "🟠 Urgente"
        score >= 0.4 -> "🟡 Normal"
        else -> "🟢 Sin prisa"
    }

    Row(
        Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            Modifier
                .weight(1f)
                .clickable { onEdit(task) }  // tap para editar
        ) {
            Text(task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            Text(
                "Prioridad: ${task.priority} · Estimación: ${task.estimateMinutes} min" +
                        (task.deadlineMillis?.let { " · Deadline: ${dateFormat.format(it)}" } ?: "")
            )
            Spacer(Modifier.height(2.dp))
            //Text("Score: ${"%.3f".format(score)}")
            Text(urgencyLabel, color = MaterialTheme.colorScheme.primary)
        }

        Column {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggleCompleted(task) }
            )
            IconButton(onClick = { onDelete(task) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}