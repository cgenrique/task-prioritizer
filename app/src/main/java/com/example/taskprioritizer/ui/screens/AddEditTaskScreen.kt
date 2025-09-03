package com.example.taskprioritizer.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskprioritizer.domain.model.Task
import java.util.*

/**
 * Pantalla de alta y edición de tareas.
 *
 * Si recibe una [task], se inicializa en modo edición.
 * Si no, se muestra como formulario para añadir una nueva tarea.
 *
 * Permite configurar:
 * - Título
 * - Descripción
 * - Prioridad (baja, media, alta)
 * - Duración estimada en minutos
 * - Fecha y hora límite
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    task: Task? = null,
    onSave: (Task) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember(task) { mutableStateOf(task?.title ?: "") }
    var description by remember(task) { mutableStateOf(task?.description ?: "") }
    var priority by remember(task) { mutableStateOf(task?.priority ?: 2) } // 1..3
    var estimateMinutes by remember(task) { mutableStateOf((task?.estimateMinutes ?: 30).toString()) }
    var deadlineMillis by remember(task) { mutableStateOf<Long?>(task?.deadlineMillis) }

    val context = LocalContext.current

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(if (task == null) "Nueva tarea" else "Editar tarea",
            style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Muestra de prioridad seleccionada (los botones de debajo cambian el valor)
        OutlinedTextField(
            value = when (priority) { 1 -> "Baja"; 2 -> "Media"; else -> "Alta" },
            onValueChange = {},
            label = { Text("Prioridad") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { priority = 1 }) { Text("Baja") }
            Button(onClick = { priority = 2 }) { Text("Media") }
            Button(onClick = { priority = 3 }) { Text("Alta") }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estimateMinutes,
            onValueChange = { estimateMinutes = it },
            label = { Text("Duración (min)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Selector de fecha límite
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, day ->
                val cal = Calendar.getInstance()
                cal.set(year, month, day)

                // Cuando seleccionas fecha, abrimos también el selector de hora
                val timePicker = android.app.TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        cal.set(Calendar.SECOND, 0)
                        cal.set(Calendar.MILLISECOND, 0)
                        deadlineMillis = cal.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true // formato 24h
                )
                timePicker.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        Button(onClick = { datePicker.show() }) {
            Text(deadlineMillis?.let {
                "Fecha límite: ${Date(it)}"
            } ?: "Seleccionar fecha límite")
        }

        Spacer(Modifier.height(16.dp))

        // Botón de acción (nuevo)
        if (deadlineMillis != null) {
            OutlinedButton(onClick = { deadlineMillis = null }) {
                Text("Quitar fecha")
            }

            Spacer(Modifier.height(16.dp))
        }

        // Botones de acción
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onCancel) { Text("Cancelar") }
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            Task(
                                id = task?.id ?: 0, // si edito, conservo id
                                title = title,
                                description = description.takeIf { it.isNotBlank() },
                                priority = priority,
                                estimateMinutes = estimateMinutes.toIntOrNull() ?: 30,
                                deadlineMillis = deadlineMillis,
                                createdAtMillis = task?.createdAtMillis ?: System.currentTimeMillis(),
                                completed = task?.completed ?: false
                            )
                        )
                    }
                }
            ) { Text(if (task == null) "Guardar" else "Actualizar") }
        }
    }
}