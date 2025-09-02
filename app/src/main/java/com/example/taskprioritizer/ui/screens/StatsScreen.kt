package com.example.taskprioritizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskprioritizer.data.local.CompletedPerDay
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.taskprioritizer.domain.model.Task
import com.example.taskprioritizer.domain.scoring.TaskScorer

@Composable
fun UrgencyPieChart(tasks: List<Task>) {
    // Contar las tareas por urgencia
    val counts = tasks.groupBy { task ->
        val score = TaskScorer.score(task)
        when {
            task.deadlineMillis == null -> "Sin prisa"
            score >= 0.8 -> "Muy urgente"
            score >= 0.6 -> "Urgente"
            score >= 0.4 -> "Normal"
            else -> "Sin prisa"
        }
    }.mapValues { it.value.size }

    val total = counts.values.sum().toFloat().coerceAtLeast(1f)

    val categories  = listOf(
        "Muy urgente" to Color.Red,
        "Urgente" to Color(0xFFFF9800),
        "Normal" to Color.Yellow,
        "Sin prisa" to Color.Green
    )

    Canvas(
        modifier = Modifier
            .size(280.dp)
            .padding(16.dp)
    ) {
        var startAngle = 0f
        categories .forEach { (label, color) ->
            val sweep = (counts[label]?.toFloat() ?: 0f) / total * 360f
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                size = Size(size.width, size.height)
            )
            startAngle += sweep
        }
    }

    // Leyenda con etiquetas
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
    ) {
        categories.forEach { (label, color) ->
            val value = counts[label] ?: 0
            if (value > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .padding(end = 6.dp)
                            .background(color, shape = CircleShape)
                    )
                    Text("$label: $value")
                }
            }
        }
    }

    Spacer(Modifier.height(8.dp))
    // Mostrar total debajo
    Text("Total de tareas: ${total.toInt()}")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    completed: List<CompletedPerDay>,
    onBack: () -> Unit,
    pendingTasks: List<Task>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Tareas pendientes por urgencia", style = MaterialTheme.typography.titleMedium)

            if (pendingTasks.isNotEmpty()) {
                UrgencyPieChart(pendingTasks)
            } else {
                Text("No hay tareas pendientes")
            }

            HorizontalDivider()

            Text("Tareas completadas por día", style = MaterialTheme.typography.titleMedium)
            completed.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it.day)
                    Text("${it.count}")
                }
            }
        }
    }
}