# Módulo GestorEjercicios

## Descripción

El módulo **GestorEjercicios** es un sistema independiente y reutilizable que permite empaquetar ejercicios en grupos llamados "lecciones". Está diseñado para ser utilizado por otros módulos del sistema HelloCode, proporcionando una interfaz clara y flexible para la gestión de ejercicios educativos.

## Características Principales

### 🎯 **Modularidad e Independencia**
- Módulo completamente independiente que puede ser usado por otros módulos
- Interfaz de integración clara y bien definida
- Patrón Singleton para gestión centralizada

### 📚 **Tipos de Lecciones**
- **Lecciones Normales**: Para aprendizaje progresivo (15 XP, 5 conocimiento)
- **Lecciones de Prueba**: Para evaluación (30 XP, 0 conocimiento)
- **Lecciones Personalizadas**: Configurables con XP y conocimiento personalizados

### 🧩 **Sistema de Adaptadores**
- **AdaptadorEjercicioSeleccion**: Para ejercicios de selección múltiple
- **AdaptadorEjercicioCompletarCodigo**: Para ejercicios de completar código
- **FabricaAdaptadores**: Crea adaptadores automáticamente según el tipo de ejercicio

### 👤 **Gestión de Usuarios**
- Seguimiento de progreso individual por usuario
- Sistema de experiencia y conocimiento
- Estadísticas detalladas de rendimiento
- Integración con el módulo de usuarios existente

### 🎮 **Sistema de Progreso**
- Experiencia ganada basada en rendimiento
- Conocimiento proporcional al porcentaje de aciertos
- Bonus por buen rendimiento (≥80%: +20% XP)
- Penalización por bajo rendimiento (<60%: 50% XP)

## Arquitectura

### Estructura de Paquetes

```
GestorEjercicios/
├── integracion/
│   └── IGestorEjercicios.java          # Interfaz principal de integración
├── adaptadores/
│   ├── AdaptadorEjercicios.java        # Interfaz base para adaptadores
│   ├── AdaptadorEjercicioSeleccion.java # Adaptador para selección múltiple
│   ├── AdaptadorEjercicioCompletarCodigo.java # Adaptador para completar código
│   └── FabricaAdaptadores.java         # Fábrica de adaptadores
├── model/
│   ├── Leccion.java                    # Modelo de lección mejorado
│   ├── GestorLecciones.java            # Gestor de lecciones
│   ├── GestorProgresoUsuario.java      # Gestión de progreso de usuarios
│   └── ResultadoEvaluacion.java        # Resultados de evaluación
├── strategy/
│   ├── EstrategiaLeccion.java          # Interfaz de estrategias
│   ├── EstrategiaLeccionNormal.java    # Estrategia para lecciones normales
│   ├── EstrategiaLeccionPrueba.java    # Estrategia para lecciones de prueba
│   └── FabricaEstrategiasLeccion.java  # Fábrica de estrategias
├── enums/
│   ├── TipoLeccion.java                # Tipos de lección
│   ├── TipoEjercicio.java              # Tipos de ejercicio
│   ├── NivelDificultad.java            # Niveles de dificultad
│   └── LenguajeProgramacion.java       # Lenguajes de programación
├── GestorEjerciciosPrincipal.java      # Implementación principal
├── GestorEjerciciosEntry.java          # Punto de entrada para otros módulos
└── ejemplo/
    └── EjemploUsoGestorEjercicios.java # Ejemplo de uso completo
```

## Uso Rápido

### 1. Inicialización

```java
// Inicializar el módulo
GestorEjerciciosEntry.inicializar();
```

### 2. Crear Lecciones

```java
// Crear lección normal
Leccion leccionNormal = GestorEjerciciosEntry.crearLeccionNormal(
    "Fundamentos de Java", 
    ejerciciosSeleccion
);

// Crear lección de prueba
Leccion leccionPrueba = GestorEjerciciosEntry.crearLeccionPrueba(
    "Evaluación de Sintaxis", 
    ejerciciosCompletar
);

// Crear lección personalizada
Leccion leccionPersonalizada = GestorEjerciciosEntry.crearLeccion(
    "Lección Avanzada",
    ejercicios,
    TipoLeccion.NORMAL,
    25, // XP personalizada
    10  // Conocimiento personalizado
);

// Crear lección con dificultad y lenguaje específicos
Leccion leccionJavaAvanzado = GestorEjerciciosEntry.crearLeccionNormal(
    "Java Avanzado - POO",
    ejercicios,
    NivelDificultad.AVANZADO,
    LenguajeProgramacion.JAVA
);

// Crear lección de prueba con dificultad y lenguaje específicos
Leccion leccionPythonPrueba = GestorEjerciciosEntry.crearLeccionPrueba(
    "Python Básico - Variables",
    ejercicios,
    NivelDificultad.BASICO,
    LenguajeProgramacion.PYTHON
);
```

### 3. Gestionar Progreso de Usuarios

```java
// Marcar lección como completada
GestorEjerciciosEntry.marcarLeccionCompletada(leccion, usuario, aciertos);

// Obtener progreso del usuario
double progreso = GestorEjerciciosEntry.obtenerProgresoUsuario(leccion, usuario);

// Obtener estadísticas completas
IGestorEjercicios.EstadisticasUsuario estadisticas = 
    GestorEjerciciosEntry.obtenerEstadisticasUsuario(usuario);
```

### 4. Consultar Información

```java
// Obtener todas las lecciones
List<Leccion> lecciones = GestorEjerciciosEntry.obtenerTodasLasLecciones();

// Obtener lección específica
Leccion leccion = GestorEjerciciosEntry.obtenerLeccion(id);

// Obtener estado del módulo
String estado = GestorEjerciciosEntry.obtenerEstadoModulo();
```

### 5. Información de Dificultad y Lenguaje

```java
// Obtener información de dificultad y lenguaje
NivelDificultad dificultad = leccion.getDificultad();
LenguajeProgramacion lenguaje = leccion.getLenguaje();

// Mostrar información completa de la lección
System.out.println(leccion.obtenerResumen());
// Salida: "Lección 'Java Avanzado - POO' (NORMAL) - AVANZADO - JAVA con 5 ejercicios."

// Filtrar lecciones por dificultad o lenguaje
List<Leccion> leccionesJava = todasLasLecciones.stream()
    .filter(l -> l.getLenguaje() == LenguajeProgramacion.JAVA)
    .collect(Collectors.toList());

List<Leccion> leccionesAvanzadas = todasLasLecciones.stream()
    .filter(l -> l.getDificultad() == NivelDificultad.AVANZADO)
    .collect(Collectors.toList());
```

## Integración con Otros Módulos

### Módulo de Ejercicios
El GestorEjercicios puede recibir ejercicios de cualquier tipo del módulo de ejercicios:

```java
// Ejercicios de selección múltiple
List<EjercicioSeleccion> ejerciciosSeleccion = EjercicioRepository.cargarEjerciciosSeleccion();

// Ejercicios de completar código
List<EjercicioCompletarCodigo> ejerciciosCompletar = EjercicioRepository.cargarEjerciciosCompletarCodigo();

// Crear lección con ejercicios mixtos
List<Object> ejerciciosMixtos = new ArrayList<>();
ejerciciosMixtos.addAll(ejerciciosSeleccion);
ejerciciosMixtos.addAll(ejerciciosCompletar);

Leccion leccion = GestorEjerciciosEntry.crearLeccionNormal("Lección Mixta", ejerciciosMixtos);
```

### Módulo de Usuarios
Integración completa con el sistema de usuarios existente:

```java
// Usar cualquier tipo de usuario
Usuario usuario = new UsuarioComunidad("usuario", "pass", "Nombre", "email@test.com");
Usuario admin = new UsuarioAdministrador("admin", "pass", "Admin", "admin@test.com");

// El sistema maneja automáticamente la reputación para UsuarioComunidad
GestorEjerciciosEntry.marcarLeccionCompletada(leccion, usuario, 4);
```

### Módulo de Gamificación
Los datos de experiencia y conocimiento pueden ser utilizados por el módulo de gamificación:

```java
// Obtener estadísticas para gamificación
IGestorEjercicios.EstadisticasUsuario stats = 
    GestorEjerciciosEntry.obtenerEstadisticasUsuario(usuario);

int experiencia = stats.getExperienciaTotal();
int conocimiento = stats.getConocimientoTotal();
double porcentajeAcierto = stats.getPorcentajeAcierto();
```

## Sistema de Experiencia y Conocimiento

### Cálculo de Experiencia
- **Rendimiento ≥80%**: Experiencia base + 20% bonus
- **Rendimiento 60-79%**: Experiencia base
- **Rendimiento <60%**: 50% de la experiencia base

### Cálculo de Conocimiento
- El conocimiento se otorga proporcionalmente al porcentaje de aciertos
- Ejemplo: 80% de aciertos = 80% del conocimiento disponible

### Integración con Reputación
- Para usuarios de tipo `UsuarioComunidad`, la experiencia se convierte automáticamente en reputación
- Fórmula: `reputacion += experienciaGanada / 10`

## Patrones de Diseño Utilizados

### 🏭 **Factory Pattern**
- `FabricaAdaptadores`: Crea adaptadores según el tipo de ejercicio
- `FabricaEstrategiasLeccion`: Crea estrategias según el tipo de lección

### 🔄 **Strategy Pattern**
- `EstrategiaLeccion`: Diferentes comportamientos para lecciones normales y de prueba
- Fácil extensión para nuevos tipos de lecciones

### 🔌 **Adapter Pattern**
- `AdaptadorEjercicios`: Interfaz unificada para diferentes tipos de ejercicios
- Permite agregar nuevos tipos de ejercicios sin modificar el código existente

## Nuevas Características: Dificultad y Lenguaje

### 🎯 **Niveles de Dificultad**
- **BASICO**: Ejercicios introductorios y fundamentales
- **INTERMEDIO**: Ejercicios con conceptos más avanzados
- **AVANZADO**: Ejercicios complejos y desafiantes

### 💻 **Lenguajes de Programación**
- **JAVA**: Ejercicios específicos de Java
- **PYTHON**: Ejercicios específicos de Python
- **JAVASCRIPT**: Ejercicios específicos de JavaScript

### 🔍 **Cálculo Automático**
- **Dificultad**: Se calcula automáticamente basándose en la dificultad predominante de los ejercicios
- **Lenguaje**: Se determina automáticamente según el lenguaje más frecuente en los ejercicios
- **Personalización**: Se pueden especificar explícitamente al crear lecciones

### 📊 **Información Enriquecida**
- Las lecciones ahora incluyen información completa de dificultad y lenguaje
- El método `obtenerResumen()` muestra toda la información relevante
- Facilita el filtrado y búsqueda de lecciones por criterios específicos

### 🎯 **Singleton Pattern**
- `GestorEjerciciosPrincipal`: Instancia única del gestor
- Gestión centralizada de lecciones y progreso

## Extensibilidad

### Agregar Nuevos Tipos de Ejercicios

1. Crear el nuevo tipo de ejercicio en el módulo de ejercicios
2. Crear un adaptador que implemente `AdaptadorEjercicios`
3. Agregar el adaptador a `FabricaAdaptadores`

```java
public class AdaptadorNuevoEjercicio implements AdaptadorEjercicios {
    // Implementar métodos de la interfaz
}

// En FabricaAdaptadores
public static AdaptadorEjercicios crearAdaptador(NuevoEjercicio ejercicio) {
    return new AdaptadorNuevoEjercicio(ejercicio);
}
```

### Agregar Nuevos Tipos de Lecciones

1. Agregar el nuevo tipo al enum `TipoLeccion`
2. Crear una nueva estrategia que implemente `EstrategiaLeccion`
3. Agregar la estrategia a `FabricaEstrategiasLeccion`

## Ejemplo Completo

Ver `EjemploUsoGestorEjercicios.java` para un ejemplo completo de uso que incluye:
- Creación de diferentes tipos de lecciones
- Gestión de progreso de usuarios
- Consulta de estadísticas
- Demostración de todas las funcionalidades

## Ventajas del Diseño Mejorado

1. **Independencia**: El módulo puede funcionar sin depender de otros módulos
2. **Reutilización**: Otros módulos pueden usar fácilmente el gestor
3. **Extensibilidad**: Fácil agregar nuevos tipos de ejercicios y lecciones
4. **Mantenibilidad**: Código bien estructurado y documentado
5. **Escalabilidad**: Puede manejar múltiples usuarios y lecciones simultáneamente
6. **Integración**: Se integra perfectamente con el sistema existente

## Próximos Pasos

1. **Persistencia**: Implementar guardado de progreso en base de datos
2. **Notificaciones**: Sistema de notificaciones para logros
3. **Analytics**: Reportes detallados de rendimiento
4. **API REST**: Interfaz web para gestión de lecciones
5. **Machine Learning**: Recomendaciones personalizadas de lecciones 