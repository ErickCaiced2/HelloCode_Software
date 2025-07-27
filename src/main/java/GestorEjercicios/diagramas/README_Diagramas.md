# Diagramas del Módulo GestorEjercicios

Este directorio contiene los diagramas UML que documentan la arquitectura y el comportamiento del módulo GestorEjercicios.

## 📊 Diagramas Disponibles

### 1. **Diagrama de Clases** (`DiagramaClasesGestorEjercicios.puml`)
**Descripción**: Muestra la estructura completa de clases, interfaces y enums del módulo.

**Contenido**:
- **Interfaces**: `IGestorEjercicios`, `AdaptadorEjercicios`, `EstrategiaLeccion`
- **Clases Principales**: `GestorEjerciciosEntry`, `GestorEjerciciosPrincipal`, `Leccion`
- **Adaptadores**: `AdaptadorEjercicioSeleccion`, `AdaptadorEjercicioCompletarCodigo`
- **Estrategias**: `EstrategiaLeccionNormal`, `EstrategiaLeccionPrueba`
- **Modelos**: `GestorProgresoUsuario`, `ResultadoEvaluacion`
- **Enums**: `TipoLeccion`, `TipoEjercicio`, `NivelDificultad`, `LenguajeProgramacion`

**Patrones de Diseño Mostrados**:
- 🏭 **Factory Pattern**: `FabricaAdaptadores`, `FabricaEstrategiasLeccion`
- 🔄 **Strategy Pattern**: `EstrategiaLeccion` y sus implementaciones
- 🔌 **Adapter Pattern**: `AdaptadorEjercicios` y sus implementaciones
- 🎯 **Singleton Pattern**: `GestorEjerciciosPrincipal`

### 2. **Diagrama de Secuencia Completo** (`DiagramaSecuenciaGestorEjercicios.puml`)
**Descripción**: Muestra todas las interacciones detalladas entre los componentes del módulo.

**Secuencias Documentadas**:
1. **Inicialización del Módulo**
2. **Creación de Lección Normal**
3. **Creación de Lección de Prueba**
4. **Ejecución de Lección**
5. **Completar Lección**
6. **Consulta de Estadísticas**
7. **Consulta de Progreso**
8. **Obtener Lecciones**
9. **Obtener Lección por ID**
10. **Evaluación de Ejercicio**

### 3. **Diagrama de Secuencia Simplificado** (`DiagramaSecuenciaSimplificado.puml`)
**Descripción**: Versión simplificada que muestra los flujos principales de uso.

**Flujos Principales**:
- **Flujo Principal**: Crear y Completar Lección
- **Flujo Alternativo**: Lección de Prueba
- **Flujo de Consulta**: Obtener información

### 4. **Diagrama de Componentes** (`DiagramaComponentesGestorEjercicios.puml`)
**Descripción**: Muestra la arquitectura de componentes y su integración con módulos externos.

**Componentes Documentados**:
- **Componentes Principales**: Entry, Principal, GestorLecciones, etc.
- **Paquetes**: Adaptadores, Estrategias, Integración, Modelos, Enums
- **Módulos Externos**: Modulo_Usuario, Modulo_Ejercicios, Modulo_Gamificacion

## 🎨 Convenciones de Colores

### Diagrama de Clases
- 🔵 **Azul Claro**: Clases principales
- 🟢 **Verde Claro**: Interfaces
- 🟡 **Amarillo Claro**: Enums

### Diagrama de Secuencia
- 🔵 **Azul**: Participantes y actores
- 📝 **Flechas**: Interacciones entre componentes

### Diagrama de Componentes
- 🔵 **Azul Claro**: Componentes principales
- 🟢 **Verde Claro**: Interfaces
- 🟡 **Amarillo Claro**: Paquetes

## 🔧 Cómo Generar los Diagramas

### Usando PlantUML Online
1. Copia el contenido de cualquier archivo `.puml`
2. Ve a [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)
3. Pega el código y genera la imagen

### Usando PlantUML Local
```bash
# Instalar PlantUML
java -jar plantuml.jar DiagramaClasesGestorEjercicios.puml
java -jar plantuml.jar DiagramaSecuenciaGestorEjercicios.puml
java -jar plantuml.jar DiagramaSecuenciaSimplificado.puml
java -jar plantuml.jar DiagramaComponentesGestorEjercicios.puml
```

### Usando VS Code
1. Instala la extensión "PlantUML"
2. Abre cualquier archivo `.puml`
3. Presiona `Alt+Shift+D` para generar el diagrama

## 📋 Relaciones Clave Documentadas

### 1. **Patrón Singleton**
```
GestorEjerciciosEntry --> GestorEjerciciosPrincipal
```
- `GestorEjerciciosEntry` es el punto de entrada
- `GestorEjerciciosPrincipal` es la implementación singleton

### 2. **Patrón Factory**
```
FabricaAdaptadores --> AdaptadorEjercicioSeleccion
FabricaAdaptadores --> AdaptadorEjercicioCompletarCodigo
FabricaEstrategiasLeccion --> EstrategiaLeccionNormal
FabricaEstrategiasLeccion --> EstrategiaLeccionPrueba
```

### 3. **Patrón Strategy**
```
Leccion --> EstrategiaLeccion
EstrategiaLeccionNormal ..|> EstrategiaLeccion
EstrategiaLeccionPrueba ..|> EstrategiaLeccion
```

### 4. **Patrón Adapter**
```
Leccion --> AdaptadorEjercicios
AdaptadorEjercicioSeleccion ..|> AdaptadorEjercicios
AdaptadorEjercicioCompletarCodigo ..|> AdaptadorEjercicios
```

## 🔄 Flujos de Interacción Principales

### 1. **Creación de Lección**
```
Módulo Externo → GestorEjerciciosEntry → GestorEjerciciosPrincipal → 
FabricaAdaptadores → FabricaEstrategiasLeccion → Leccion → GestorLecciones
```

### 2. **Completar Lección**
```
Módulo Externo → GestorEjerciciosEntry → GestorEjerciciosPrincipal → 
GestorProgresoUsuario → ProgresoUsuario → Usuario
```

### 3. **Consulta de Estadísticas**
```
Módulo Externo → GestorEjerciciosEntry → GestorEjerciciosPrincipal → 
GestorProgresoUsuario → EstadisticasUsuario
```

## 📈 Beneficios de la Documentación Visual

1. **Comprensión Rápida**: Los diagramas permiten entender la arquitectura de un vistazo
2. **Comunicación Efectiva**: Facilita la comunicación entre desarrolladores
3. **Mantenimiento**: Ayuda a identificar dependencias y relaciones
4. **Onboarding**: Nuevos desarrolladores pueden entender el sistema rápidamente
5. **Diseño**: Sirve como referencia para futuras modificaciones

## 🔍 Puntos Clave de la Arquitectura

### **Independencia del Módulo**
- El módulo puede funcionar sin depender de otros módulos
- Interfaz clara de integración (`IGestorEjercicios`)
- Punto de entrada único (`GestorEjerciciosEntry`)

### **Extensibilidad**
- Fácil agregar nuevos tipos de ejercicios mediante adaptadores
- Fácil agregar nuevos tipos de lecciones mediante estrategias
- Sistema de fábricas para creación de componentes

### **Gestión de Estado**
- Seguimiento individual de progreso por usuario
- Sistema de experiencia y conocimiento
- Integración con reputación de usuarios

### **Patrones de Diseño**
- **Singleton**: Gestión centralizada
- **Factory**: Creación de componentes
- **Strategy**: Comportamientos diferentes
- **Adapter**: Interfaz unificada

## 📝 Notas de Mantenimiento

- Los diagramas se actualizan automáticamente cuando se modifica el código
- Mantener consistencia entre diagramas y código
- Documentar nuevos patrones o componentes agregados
- Revisar diagramas en cada iteración del desarrollo

## 🚀 Próximos Pasos

1. **Diagramas de Actividad**: Para flujos de negocio complejos
2. **Diagramas de Estado**: Para el ciclo de vida de las lecciones
3. **Diagramas de Despliegue**: Para arquitectura de sistema
4. **Diagramas de Casos de Uso**: Para interacciones con usuarios finales 