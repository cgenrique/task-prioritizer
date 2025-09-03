# 📋 TaskPrioritizer

Una aplicación móvil desarrollada en **Android Studio** con **Kotlin** y **Jetpack Compose**, pensada para gestionar tareas de forma sencilla pero inteligente.  
Forma parte del proyecto de la asignatura **Gestión de Información en Dispositivos Móviles (GIDM)**.

---

## 🚀 Características principales

- ➕ **Añadir tareas** con título, descripción, prioridad, duración estimada y fecha/hora límite.
- ✏️ **Editar tareas** existentes.
- ✅ **Marcar como completadas** (se eliminan automáticamente tras 7 días).
- ❌ **Eliminar tareas** manualmente.
- 🔍 **Filtros de visualización**: ver tareas pendientes, completadas o urgentes.
- 📊 **Estadísticas**:
    - Gráfico circular de tareas pendientes clasificadas por urgencia.
    - Gráfico de barras con tareas completadas por día.
- 🎨 **Interfaz moderna** basada en Material3 (Jetpack Compose).

---

## 🧠 Algoritmo de priorización

Cada tarea recibe un **score** en función de tres factores:

1. ⚡ **Prioridad** (alta, media, baja).
2. ⏳ **Urgencia** (cuánto falta para la fecha límite).
3. 📏 **Duración estimada** (favorece tareas cortas, los *quick wins*).

La fórmula combina los factores con pesos:

\[
Score = 0.4 \cdot Prioridad + 0.4 \cdot Urgencia + 0.2 \cdot Tamaño
\]

Según este score, las tareas se clasifican en:
- 🔴 Muy urgente (score ≥ 0.8)
- 🟠 Urgente (score ≥ 0.6)
- 🟡 Normal (score ≥ 0.4)
- 🟢 Sin prisa (score < 0.4 o sin fecha límite)

---

## 🗄️ Arquitectura y tecnologías utilizadas

El proyecto sigue una estructura por capas:

- **📂 data.local** → Persistencia con **Room** (DAO, entidades, base de datos).
- **📂 domain.model** → Modelo de dominio y mapeadores entre entidad y modelo.
- **📂 domain.repository** → Interfaz del repositorio + implementación.
- **📂 domain.scoring** → Algoritmo de priorización de tareas.
- **📂 domain.usecase** → Caso de uso para obtener tareas priorizadas.
- **📂 ui.navigation** → Navegación con **NavHost** y pantallas Compose.
- **📂 ui.screens** → Pantallas principales: lista de tareas, edición y estadísticas.
- **📂 ui.viewmodel** → Lógica de presentación con **ViewModel** + **Coroutines**.

### 🔧 Librerías principales
- [Room](https://developer.android.com/training/data-storage/room) → Persistencia en BD local.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) → Interfaz declarativa moderna.
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) → Navegación entre pantallas.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) → Manejo de concurrencia y flujos.

---

## 📱 Flujo de uso de la aplicación

1. 🏁 **Inicio** → se muestra la lista de tareas.
2. ➕ **Añadir** → crear una nueva tarea con todos sus campos.
3. ✏️ **Editar** → modificar una tarea existente.
4. ✅ **Completar** → marcar como completada (se elimina automáticamente tras 7 días).
5. 📊 **Estadísticas** → consultar gráficos de urgencia y tareas completadas.

---

## 📸 Interfaz de usuario

- 🏠 **Lista de tareas** con filtros y acciones rápidas.
- ✍️ **Pantalla de alta/edición** con formulario completo.
- 📊 **Pantalla de estadísticas** con gráficos visuales.

---

## 🔮 Posibles mejoras futuras

- 🔔 **Notificaciones** para avisar de tareas próximas a vencer.
- ☁️ **Sincronización online** con servicios en la nube.
- 🤖 **Sistemas de recomendación** para detectar tareas recurrentes.
- 📈 **Más visualizaciones** (líneas de productividad, tiempos invertidos, etc.).

---

## 👨‍💻 Autor

Proyecto desarrollado por **Enrique Camacho García** para la asignatura de Gestión de Información 
en Dispositivos Móviles (GIDM) como parte del plan de estudios del Master Universitario en 
Ingeniería Informática (MUII) por la Universidad de Granada (UGR).  

---
