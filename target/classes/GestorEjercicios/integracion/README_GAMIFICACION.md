# 🎮 INTEGRACIÓN CON MÓDULO DE GAMIFICACIÓN

## Resumen
Este documento explica cómo el módulo de Gamificación puede obtener datos de progreso de lecciones para implementar desafíos semanales y mensuales.

## Clase Principal: `IntegracionGamificacion`

### Ubicación
```java
import GestorEjercicios.integracion.IntegracionGamificacion;
```

### Métodos Disponibles

#### 📊 Obtener Datos de Lecciones

```java
// Lecciones completadas históricamente
int totalLecciones = IntegracionGamificacion.getLeccionesTotales("nombreUsuario");

// Lecciones completadas en últimos 7 días
int leccionesSemana = IntegracionGamificacion.getLeccionesSemanaActual("nombreUsuario");

// Lecciones completadas en últimos 30 días  
int leccionesMes = IntegracionGamificacion.getLeccionesMesActual("nombreUsuario");
```

#### 🎯 Verificar Metas de Desafíos

```java
// Para desafíos semanales
boolean completoDesafioSemanal = IntegracionGamificacion.cumplioMetaSemanal("nombreUsuario", 5);

// Para desafíos mensuales
boolean completoDesafioMensual = IntegracionGamificacion.cumplioMetaMensual("nombreUsuario", 20);
```

#### 👥 Gestión de Usuarios

```java
// Obtener todos los usuarios activos
List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();

// Iterar sobre todos los usuarios
for (String usuario : usuarios) {
    int lecciones = IntegracionGamificacion.getLeccionesSemanaActual(usuario);
    System.out.println(usuario + " completó " + lecciones + " lecciones esta semana");
}
```

#### 💎 Datos Adicionales (XP y Conocimiento)

```java
// XP total del usuario
int xp = IntegracionGamificacion.getXPTotal("nombreUsuario");

// Conocimiento total del usuario
int conocimiento = IntegracionGamificacion.getConocimientoTotal("nombreUsuario");

// Todas las estadísticas en un Map
Map<String, Integer> stats = IntegracionGamificacion.getEstadisticasCompletas("nombreUsuario");
```

## 📋 Ejemplo de Implementación para DesafioSemanal

```java
public class DesafioSemanal extends Desafio {
    private int metaSemanal;
    private String usuario;
    
    public void actualizarActividades(Integer cantidad) {
        // En lugar de pasar la cantidad, obtener datos reales
        int leccionesActuales = IntegracionGamificacion.getLeccionesSemanaActual(this.usuario);
        
        // Actualizar progreso basado en datos reales
        this.actividadesCompletadas = leccionesActuales;
        
        // Verificar si se completó el desafío
        if (leccionesActuales >= this.metaSemanal) {
            this.completarDesafio();
        }
    }
    
    public void verificarProgreso() {
        boolean completado = IntegracionGamificacion.cumplioMetaSemanal(this.usuario, this.metaSemanal);
        if (completado && this.estaActivo) {
            this.completarDesafio();
        }
    }
}
```

## 🔄 Actualización en Tiempo Real

Los datos se leen directamente desde los archivos del sistema de lecciones, por lo que siempre están actualizados:

- `todos_los_usuarios.txt` - Progreso individual
- `totales_compartidos.txt` - XP y conocimiento totales  
- `estadisticas_compartidas.txt` - Historial de lecciones completadas

## 🎯 Casos de Uso Comunes

### 1. Crear Desafío Semanal Automático
```java
// Crear desafío basado en actividad previa del usuario
String usuario = "nombreUsuario";
int leccionesSemanaPasada = IntegracionGamificacion.getLeccionesSemanaActual(usuario);
int metaSugerida = Math.max(3, leccionesSemanaPasada + 1); // Incrementar gradualmente

DesafioSemanal desafio = new DesafioSemanal(metaSugerida, logros);
```

### 2. Ranking Dinámico
```java
List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();
usuarios.sort((u1, u2) -> {
    int lecciones1 = IntegracionGamificacion.getLeccionesSemanaActual(u1);
    int lecciones2 = IntegracionGamificacion.getLeccionesSemanaActual(u2);
    return Integer.compare(lecciones2, lecciones1); // Descendente
});
```

### 3. Notificaciones de Progreso
```java
for (String usuario : IntegracionGamificacion.getUsuariosActivos()) {
    int lecciones = IntegracionGamificacion.getLeccionesSemanaActual(usuario);
    if (lecciones >= 5) {
        // Otorgar logro especial
        otorgarLogro(usuario, "Estudiante Dedicado");
    }
}
```

## ⚠️ Notas Importantes

1. **Sin Dependencias**: Los métodos son estáticos, no necesitas instanciar nada
2. **Datos en Tiempo Real**: Siempre obtienen la información más actualizada
3. **Thread-Safe**: Los métodos leen archivos de forma segura
4. **Manejo de Errores**: Retornan 0 o listas vacías si hay problemas, no lanzan excepciones

## 🔧 Debug y Troubleshooting

```java
// Para debugging, usar el reporte completo
String reporte = IntegracionGamificacion.getReporteUsuario("nombreUsuario");
System.out.println(reporte);

// Solicitar actualización explícita de datos (para debugging)
IntegracionGamificacion.solicitarActualizacionDatos();
```
