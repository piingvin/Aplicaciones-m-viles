# WIM 100 - Sistema Digital de Postulación y Evaluación de Candidatas

Aplicación Android nativa desarrollada en **Kotlin** con **Jetpack Compose** y **Material Design 3**, diseñada para digitalizar la postulación, evaluación y selección de candidatas del programa WIM 100.

---

## Integrantes
- Diego Saavedra
- Antonia Ávila

 **Tablero de Trello (Historias de Usuario):** https://trello.com/b/ncs4eHLJ/aplicaciones-moviles-app

---

##  Características y Funcionalidades Principales

### 1.  Seguridad y Autenticación Biométrica
- Acceso restringido mediante lectura de huella digital (`BiometricPrompt`).
- Retención segura de sesión entre rotaciones de pantalla (`rememberSaveable`).

### 2. Registro de Candidatas y Selector Nativo de CV
- Formulario de inscripción con validación de campos en tiempo real (Nombre, Correo, Teléfono, Área y Resumen).
- Integración con el explorador nativo de archivos del teléfono (`Storage Access Framework`) para adjuntar el Curriculum Vitae (CV) en formato PDF o documento.

### 3.  Matriz de Evaluación WIM
- Evaluación objetiva con asignación de notas de 1 a 5 en 3 criterios estratégicos:
  - **Experiencia Laboral:** 35%
  - **Formación y Conocimientos:** 35%
  - **Entrevista y Habilidades:** 30%
- Cálculo en   del **Puntaje Total Ponderado**.
- Validación doble para evitar evaluaciones duplicadas sobre la misma candidata.

### 4. Ranking Consolidado
- Vista del ranking general ordenado automáticamente de mayor a menor puntaje ponderado.
- Destacado visual para las primeras posiciones del podio ( 1° Lugar,  2° Lugar,  3° Lugar).

### 5. Trazabilidad e Historial de Acciones
- Módulo de auditoría que registra automáticamente cada acción realizada en la aplicación (Registros, Evaluaciones, Ediciones y Eliminaciones) con fecha y hora.

### 6. Animaciones Funcionales (10% Rúbrica)
- Transiciones fluidas entre pantallas mediante `AnimatedContent`.
- Animación de sacudida (*shake animation*) en los botones de formulario ante errores de validación.
- Manejo nativo del botón atrás con `BackHandler`.

---

## Arquitectura y Tecnologías Utilizadas

- **Lenguaje:** Kotlin 2.0+
- **Interfaz de Usuario:** Jetpack Compose & Material Design 3
- **Patrón de Arquitectura:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Persistencia de Datos:** Room Database (SQLite, versión 2) con KSP
- **Asincronía:** Kotlin Coroutines & StateFlow
- **Inyección de Dependencias:** Inyección Manual basada en Contenedor (`AppContainer`)
- **Seguridad y Recursos Nativos:** AndroidX BiometricPrompt & SAF File Picker

---

## Cómo Ejecutar el Proyecto

1. Clona el repositorio en tu máquina local:
   ```bash
   git clone https://github.com/tu-usuario/Aplicaciones-m-viles.git
   ```
2. Abre el proyecto en **Android Studio (Ladybug / Quail o superior)**.
3. Sincroniza los archivos de Gradle (`Sync Project with Gradle Files`).
4. Conecta un dispositivo físico o ejecuta un emulador Android (API 24 o superior).
5. Compila y ejecuta la aplicación (`Run 'app'`).
