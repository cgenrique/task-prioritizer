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
import com.example.taskprioritizer.ui.navigation.AppNav

/**
 * Actividad principal de la aplicación.
 *
 * Configura la UI con Jetpack Compose y define [AppNav] como punto de entrada
 * para la navegación entre pantallas.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskPrioritizerTheme {
                AppNav()
            }
        }
    }
}

/**
 * Composable de ejemplo generado por defecto en proyectos Compose.
 * No se utiliza en la lógica principal de la aplicación.
 */
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