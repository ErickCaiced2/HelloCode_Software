package GestorEjercicios.Controllers;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.model.GestorLeccionesBasadoEnArchivos;
import GestorEjercicios.services.UserProgressService;
import GestorEjercicios.services.LoggingService;
import GestorEjercicios.GestorEjerciciosEntry;
import Modulo_Ejercicios.exercises.Lenguaje;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador principal de la pantalla principal del Gestor de Ejercicios
 * Maneja la navegación entre lecciones y la configuración del sistema
 */
public class PantallaPrincipalController {
    
    @FXML
    private Button btnCrearLeccion; // Botón "FINAL" - Quinta lección
    
    @FXML
    private Button btnEstadisticas; // Botón "5" - Cuarta lección
    
    @FXML
    private Button btnConfiguracion; // Botón "4" - Tercera lección
    
    @FXML
    private Button btnLeccion2; // Botón "2" - Segunda lección
    
    @FXML
    private Button btnLeccion3; // Botón "3" - Tercera lección
    
    @FXML
    private Button btnSalir; // Botón "1" - Primera lección (NO salir)
    
    @FXML
    private Button btnSalirReal; // Botón real de salir en la barra inferior
    
    // Variables para el progreso de lecciones (CON persistencia JSON)
    private static String usuarioActual;
    private static LenguajeProgramacion lenguajeConfigurado;
    private static NivelDificultad nivelConfigurado;
    private static TipoLeccion tipoConfigurado;
    private static int leccionActual = 1;
    private static boolean primeraLeccionCompletada = false;
    
    @FXML
    public void initialize() {
        System.out.println("🎮 === PANTALLA PRINCIPAL ACTUALIZADA CARGADA ===");
        System.out.println("🎯 Sistema TXT + Ejercicios Reales ACTIVO");
        System.out.println("🔧 Version: " + java.time.LocalDateTime.now());
        
        // 🔍 Obtener usuario actual del sistema
        obtenerUsuarioActual();
        
        // 💾 Cargar progreso guardado
        cargarProgresoGuardado();
        
        configurarBotones();
        actualizarEstadoBotones();
        
        // 📋 NUEVO: Mostrar vista previa de lecciones disponibles
        mostrarVistaPreviewLecciones();
        
        System.out.println("🎮 PantallaPrincipal inicializada para usuario: " + usuarioActual);
    }
    
    /**
     * 👀 NUEVO: Muestra una vista previa de las lecciones disponibles al cargar la pantalla
     */
    private void mostrarVistaPreviewLecciones() {
        System.out.println("\n👀 === VISTA PREVIA LECCIONES DISPONIBLES ===");
        
        // Mostrar estadísticas generales del sistema TXT
        try {
            java.util.Map<String, Integer> stats = GestorEjerciciosEntry.obtenerEstadisticasLeccionesTXT();
            System.out.println("📊 Estadísticas del sistema:");
            for (java.util.Map.Entry<String, Integer> entry : stats.entrySet()) {
                System.out.println("   " + entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error obteniendo estadísticas: " + e.getMessage());
        }
        
        System.out.println("\n💡 === INSTRUCCIONES ===");
        System.out.println("   🔘 Botón '1': Configurar lenguaje y nivel");
        System.out.println("   🔘 Botones '2-5': Ejecutar lecciones según configuración");
        System.out.println("   🔘 Botón 'FINAL': Ver lista completa de lecciones disponibles");
        
        if (lenguajeConfigurado != null && nivelConfigurado != null) {
            System.out.println("\n✅ === CONFIGURACIÓN ACTUAL ===");
            System.out.println("   🔧 Lenguaje: " + lenguajeConfigurado.name());
            System.out.println("   📊 Nivel: " + nivelConfigurado.name());
            System.out.println("   📚 Progreso: Lección " + leccionActual);
            
            // Mostrar cuántas lecciones están disponibles para esta configuración
            try {
                java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> leccionesDisponibles = 
                    GestorEjerciciosEntry.obtenerLeccionesTXT(lenguajeConfigurado.name());
                
                long leccionesDelNivel = leccionesDisponibles.stream()
                    .filter(l -> l.nivel.equalsIgnoreCase(nivelConfigurado.name()))
                    .count();
                
                System.out.println("   🎮 Lecciones disponibles: " + leccionesDelNivel + " para tu nivel");
                
                if (leccionesDelNivel > 0) {
                    System.out.println("\n🎯 Presiona botón 'FINAL' para ver la lista completa");
                }
                
            } catch (Exception e) {
                System.out.println("   ⚠️ Error verificando lecciones disponibles");
            }
        } else {
            System.out.println("\n🔧 === CONFIGURACIÓN NECESARIA ===");
            System.out.println("   ⚠️ Presiona botón '1' para configurar tu lenguaje y nivel");
            System.out.println("   📚 Una vez configurado, podrás ver y ejecutar lecciones");
        }
        
        System.out.println("👀 === FIN VISTA PREVIA ===\n");
    }
    
    /**
     * 👤 Obtiene el usuario actual del sistema
     */
    private void obtenerUsuarioActual() {
        try {
            var usuario = GestorEjerciciosEntry.obtenerUsuarioActual();
            if (usuario != null) {
                usuarioActual = usuario.getUsername();
                System.out.println("✅ Usuario detectado: " + usuarioActual);
            } else {
                usuarioActual = "UsuarioDemo"; // Fallback
                System.out.println("⚠️ No se detectó usuario, usando: " + usuarioActual);
            }
        } catch (Exception e) {
            usuarioActual = "UsuarioDemo";
            System.out.println("❌ Error obteniendo usuario: " + e.getMessage());
        }
    }
    
    /**
     * 📖 Carga el progreso guardado usando el servicio refactorizado
     */
    private void cargarProgresoGuardado() {
        try {
            UserProgressService.ProgresoInfo progreso = UserProgressService.cargarProgresoUsuario(usuarioActual);
            
            // Restaurar configuración guardada
            lenguajeConfigurado = progreso.lenguajeConfigurado;
            nivelConfigurado = progreso.nivelConfigurado;
            tipoConfigurado = progreso.tipoConfigurado;
            leccionActual = progreso.leccionActual;
            primeraLeccionCompletada = progreso.primeraLeccionCompletada;
            
            if (UserProgressService.esConfiguracionValida(progreso)) {
                LoggingService.logProgresoUsuario(usuarioActual, "PROGRESO_RESTAURADO", 
                                                "Lección " + leccionActual + " - " + lenguajeConfigurado);
            } else {
                LoggingService.info("Sin progreso previo, comenzando desde cero");
            }
        } catch (Exception e) {
            LoggingService.error("Error cargando progreso", e);
        }
    }
    
    /**
     * Configura las acciones de todos los botones de la interfaz
     */
    private void configurarBotones() {
        // Botón "1" - Primera lección (siempre configuración)
        btnSalir.setOnAction(_ -> iniciarPrimeraLeccion());
        
        // Botón "2" - Segunda lección
        btnLeccion2.setOnAction(_ -> iniciarLeccion(2));
        
        // Botón "3" - Tercera lección
        btnLeccion3.setOnAction(_ -> iniciarLeccion(3));
        
        // Botón "4" - Cuarta lección
        btnConfiguracion.setOnAction(_ -> iniciarLeccion(4));
        
        // Botón "5" - Quinta lección
        btnEstadisticas.setOnAction(_ -> iniciarLeccion(5));
        
        // Botón "FINAL" - Mostrar lista de lecciones disponibles
        btnCrearLeccion.setOnAction(_ -> mostrarListaLecciones());
        
        // Botón real de salir en la barra inferior
        btnSalirReal.setOnAction(_ -> salirAplicacion());
    }
    
    /**
     * 📋 NUEVO: Muestra todas las lecciones disponibles en consola
     * Este método permite ver qué lecciones están disponibles sin iniciar ninguna
     */
    private void mostrarListaLecciones() {
        System.out.println("\n📋 === MOSTRAR LISTA COMPLETA DE LECCIONES ===");
        
        // Verificar si hay configuración
        if (lenguajeConfigurado == null || nivelConfigurado == null) {
            System.out.println("⚠️ Configuración no encontrada. Configurando primero...");
            navegarACrearLeccion();
            return;
        }
        
        // Obtener estadísticas generales
        java.util.Map<String, Integer> estadisticas = GestorEjerciciosEntry.obtenerEstadisticasLeccionesTXT();
        System.out.println("📊 === ESTADÍSTICAS GENERALES ===");
        for (java.util.Map.Entry<String, Integer> entry : estadisticas.entrySet()) {
            System.out.println("   " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
        
        // Mostrar lecciones para el lenguaje configurado
        System.out.println("🔧 === CONFIGURACIÓN ACTUAL ===");
        System.out.println("   Lenguaje: " + lenguajeConfigurado.name());
        System.out.println("   Nivel: " + nivelConfigurado.name());
        System.out.println("   Lección actual: " + leccionActual);
        System.out.println();
        
        // Obtener todas las lecciones del lenguaje
        java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> todasLecciones = 
            GestorLeccionesBasadoEnArchivos.obtenerLecciones(lenguajeConfigurado.name());
        
        if (!todasLecciones.isEmpty()) {
            System.out.println("📚 === TODAS LAS LECCIONES DE " + lenguajeConfigurado.name() + " ===");
            
            // Agrupar por nivel
            java.util.Map<String, java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion>> leccionesPorNivel = 
                todasLecciones.stream()
                    .collect(java.util.stream.Collectors.groupingBy(l -> l.nivel));
            
            for (java.util.Map.Entry<String, java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion>> entry : leccionesPorNivel.entrySet()) {
                String nivel = entry.getKey();
                java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> lecciones = entry.getValue();
                
                System.out.println("📊 === NIVEL " + nivel + " (" + lecciones.size() + " lecciones) ===");
                for (int i = 0; i < lecciones.size(); i++) {
                    GestorLeccionesBasadoEnArchivos.LeccionDefinicion leccion = lecciones.get(i);
                    String marcador = nivel.equalsIgnoreCase(nivelConfigurado.name()) ? "👉" : "  ";
                    System.out.println(String.format("%s [%d] 📖 %s", 
                        marcador, i + 1, leccion.titulo));
                    System.out.println(String.format("      📝 %s", leccion.descripcion));
                    System.out.println(String.format("      ⏱️ %d min | 🎮 %d ejercicios", 
                        leccion.duracionMinutos, leccion.cantidadEjercicios));
                    if (nivel.equalsIgnoreCase(nivelConfigurado.name())) {
                        System.out.println("      ✅ Disponible para tu nivel actual");
                    }
                    System.out.println();
                }
            }
            
            // Probar el sistema TXT
            System.out.println("🧪 === PRUEBA DEL SISTEMA TXT ===");
            GestorEjerciciosEntry.probarSistemaTXT();
            
            // Mostrar lección que se ejecutaría si presionas los botones
            java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> leccionesNivelActual = 
                leccionesPorNivel.getOrDefault(nivelConfigurado.name(), new java.util.ArrayList<>());
            
            if (!leccionesNivelActual.isEmpty()) {
                int indiceLeccion = Math.min(leccionActual - 1, leccionesNivelActual.size() - 1);
                indiceLeccion = Math.max(0, indiceLeccion);
                
                GestorLeccionesBasadoEnArchivos.LeccionDefinicion proximaLeccion = leccionesNivelActual.get(indiceLeccion);
                System.out.println("🎯 === PRÓXIMA LECCIÓN ===");
                System.out.println("   Al presionar botones 2-5 ejecutarás:");
                System.out.println("   📖 " + proximaLeccion.titulo);
                System.out.println("   📝 " + proximaLeccion.descripcion);
                System.out.println("   🎮 " + proximaLeccion.cantidadEjercicios + " ejercicios");
            }
            
        } else {
            System.out.println("❌ No hay lecciones disponibles para " + lenguajeConfigurado.name());
        }
        
        System.out.println("📋 === FIN LISTA DE LECCIONES ===\n");
    }
    
    /**
     * Inicia la primera lección (siempre va a configuración)
     */
    private void iniciarPrimeraLeccion() {
        navegarACrearLeccion();
    }
    
    /**
     * Inicia una lección específica verificando el progreso
     */
    private void iniciarLeccion(int numeroLeccion) {
        if (!primeraLeccionCompletada) {
            System.out.println("Debes completar la primera lección antes de continuar");
            return;
        }
        
        if (lenguajeConfigurado == null || nivelConfigurado == null || tipoConfigurado == null) {
            System.out.println("Configuración no encontrada. Redirigiendo a configuración...");
            navegarACrearLeccion();
            return;
        }
        
        leccionActual = numeroLeccion;
        crearYMostrarLeccion();
    }
    
    /**
     * Crea y muestra una lección con la configuración actual
     * 📋 NUEVO: Muestra lista de lecciones disponibles para que el usuario seleccione
     */
    private void crearYMostrarLeccion() {
        try {
            // 📋 Obtener todas las lecciones disponibles para mostrar al usuario
            java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> leccionesDisponibles = 
                GestorLeccionesBasadoEnArchivos.obtenerLecciones(lenguajeConfigurado.name());
            
            // Filtrar por nivel si es necesario
            java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> leccionesFiltradas = 
                leccionesDisponibles.stream()
                    .filter(l -> l.nivel.equalsIgnoreCase(nivelConfigurado.name()))
                    .collect(java.util.stream.Collectors.toList());
            
            if (!leccionesFiltradas.isEmpty()) {
                System.out.println("📋 === LECCIONES DISPONIBLES ===");
                System.out.println("🔧 Lenguaje: " + lenguajeConfigurado.name());
                System.out.println("📊 Nivel: " + nivelConfigurado.name());
                System.out.println("📚 Total disponibles: " + leccionesFiltradas.size());
                System.out.println();
                
                // Mostrar lista de lecciones disponibles
                for (int i = 0; i < leccionesFiltradas.size(); i++) {
                    GestorLeccionesBasadoEnArchivos.LeccionDefinicion leccion = leccionesFiltradas.get(i);
                    System.out.println(String.format("  [%d] 📖 %s", 
                        i + 1, leccion.titulo));
                    System.out.println(String.format("      📝 %s", leccion.descripcion));
                    System.out.println(String.format("      ⏱️ %d min | 🎮 %d ejercicios", 
                        leccion.duracionMinutos, leccion.cantidadEjercicios));
                    System.out.println();
                }
                
                // Por ahora, usar la lección que corresponde al número actual de lección
                // En el futuro se puede implementar una selección por GUI
                int indiceLeccion = Math.min(leccionActual - 1, leccionesFiltradas.size() - 1);
                indiceLeccion = Math.max(0, indiceLeccion); // Asegurar que no sea negativo
                
                System.out.println("🎯 Seleccionando lección " + (indiceLeccion + 1) + " de " + leccionesFiltradas.size());
                
                // Obtener la lección específica
                GestorLeccionesBasadoEnArchivos.LeccionCompleta leccionCompleta = 
                    GestorLeccionesBasadoEnArchivos.obtenerLeccionPorIndice(
                        lenguajeConfigurado.name(), 
                        nivelConfigurado.name(),
                        indiceLeccion
                    );
                
                if (leccionCompleta != null) {
                    System.out.println("✅ Lección seleccionada: " + leccionCompleta.titulo);
                    
                    // Crear lección usando el controlador de lección existente
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/CrearLeccion.fxml"));
                    Parent root = loader.load();
                    
                    LeccionController controller = loader.getController();
                    
                    // 🎯 NUEVO: Configurar con lección específica seleccionada
                    controller.configurarLeccionConEjercicios(
                        leccionCompleta.ejercicios,
                        mapearLenguaje(lenguajeConfigurado),
                        mapearNivel(nivelConfigurado),
                        leccionCompleta.titulo,
                        leccionCompleta.descripcion
                    );
                    
                    Stage stage = new Stage();
                    stage.setTitle("Lección " + leccionActual + " - " + leccionCompleta.titulo);
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    centrarVentana(stage);
                    stage.show();
                    
                    cerrarVentanaActual();
                    
                    System.out.println("🚀 Lección iniciada: " + leccionCompleta.titulo + 
                                     " (" + leccionCompleta.ejercicios.size() + " ejercicios)");
                    
                } else {
                    System.out.println("❌ Error al cargar lección específica");
                    crearLeccionOriginal();
                }
                
            } else {
                System.out.println("⚠️ No hay lecciones disponibles para " + lenguajeConfigurado + " - " + nivelConfigurado);
                // Fallback: usar el método original
                crearLeccionOriginal();
            }
            
        } catch (IOException e) {
            System.err.println("❌ Error al crear lección: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🔄 Método original de crear lección (fallback)
     */
    private void crearLeccionOriginal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/CrearLeccion.fxml"));
            Parent root = loader.load();
            
            LeccionController controller = loader.getController();
            
            // Configurar la lección con los valores guardados
            controller.configurarLeccionConValores(
                mapearLenguaje(lenguajeConfigurado),
                mapearNivel(nivelConfigurado)
            );
            
            Stage stage = new Stage();
            stage.setTitle("Lección " + leccionActual + " - " + tipoConfigurado);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            centrarVentana(stage);
            stage.show();
            
            cerrarVentanaActual();
            
        } catch (IOException e) {
            System.err.println("❌ Error al crear lección original: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Navega a la pantalla de configuración de lección
     */
    private void navegarACrearLeccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/CrearLeccion.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Configurar Lección");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            centrarVentana(stage);
            stage.show();
            
            cerrarVentanaActual();
            
            System.out.println("Navegando a configuración de lección");
            
        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla de crear lección: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Mapea el enum de lenguaje del GestorEjercicios al del Modulo_Ejercicios
     */
    private Lenguaje mapearLenguaje(LenguajeProgramacion lp) {
        switch (lp) {
            case JAVA: return Lenguaje.JAVA;
            case PYTHON: return Lenguaje.PYTHON;
            case CPP: return Lenguaje.C;
            default: return Lenguaje.JAVA;
        }
    }
    
    /**
     * Mapea el enum de nivel de dificultad del GestorEjercicios al del Modulo_Ejercicios
     */
    private Modulo_Ejercicios.exercises.NivelDificultad mapearNivel(NivelDificultad nd) {
        switch (nd) {
            case BASICO: return Modulo_Ejercicios.exercises.NivelDificultad.BASICO;
            case INTERMEDIO: return Modulo_Ejercicios.exercises.NivelDificultad.INTERMEDIO;
            case AVANZADO: return Modulo_Ejercicios.exercises.NivelDificultad.AVANZADO;
            default: return Modulo_Ejercicios.exercises.NivelDificultad.BASICO;
        }
    }
    
    /**
     * Marca la lección actual como completada usando el servicio
     * 🏆 REFACTORIZADO: Usa servicio en lugar de acceso directo
     */
    public void completarLeccion() {
        completarLeccion("Lección " + leccionActual, 100, 300); // Valores por defecto
    }
    
    /**
     * 🎉 Marca lección completada con detalles específicos
     */
    public void completarLeccion(String titulo, int puntuacion, int tiempoSegundos) {
        // 🏆 Registrar usando servicio
        java.util.List<String> ejerciciosResueltos = new java.util.ArrayList<>();
        ejerciciosResueltos.add("Ejercicio completado " + java.time.LocalDateTime.now());
        
        UserProgressService.registrarLeccionCompletada(
            usuarioActual != null ? usuarioActual : "UsuarioDemo",
            leccionActual, titulo,
            lenguajeConfigurado != null ? lenguajeConfigurado.name() : "JAVA",
            nivelConfigurado != null ? nivelConfigurado.name() : "BASICO",
            puntuacion, tiempoSegundos, ejerciciosResueltos
        );
        
        leccionActual++;
        
        // Actualizar configuración
        UserProgressService.guardarConfiguracion(
            usuarioActual != null ? usuarioActual : "UsuarioDemo",
            lenguajeConfigurado, nivelConfigurado, tipoConfigurado,
            leccionActual, primeraLeccionCompletada
        );
        
        LoggingService.logLeccion("COMPLETADA", titulo, 
                                "Puntuación: " + puntuacion + " | Siguiente: " + leccionActual);
    }
    
    /**
     * 💾 MÉTODO SIMPLE: Guarda progreso usando archivos TXT simples
     */
    public static void guardarProgresoLeccionEstatico(String titulo, int puntuacion, int tiempoSegundos, int cantidadEjercicios) {
        try {
            String usuario = usuarioActual != null ? usuarioActual : "erick";
            
            // Valores por defecto seguros
            String lenguaje = lenguajeConfigurado != null ? lenguajeConfigurado.name() : "JAVA";
            String nivel = nivelConfigurado != null ? nivelConfigurado.name() : "BASICO";
            
            System.out.println("💾 === GUARDANDO PROGRESO SIMPLE ===");
            System.out.println("👤 Usuario: " + usuario);
            System.out.println("📚 Lección: " + titulo);
            System.out.println("🎯 Puntuación: " + puntuacion);
            System.out.println("⏱️ Tiempo: " + tiempoSegundos + "s");
            System.out.println("🎮 Ejercicios: " + cantidadEjercicios);
            System.out.println("🔧 Lenguaje: " + lenguaje);
            System.out.println("📊 Nivel: " + nivel);
            
            // Usar nuestro sistema simple de archivos TXT
            GestorEjercicios.model.ProgresoSimple.registrarLeccionCompletada(
                usuario, leccionActual, titulo, lenguaje, nivel,
                puntuacion, tiempoSegundos, cantidadEjercicios
            );
            
            // Actualizar progreso básico
            if (!primeraLeccionCompletada) {
                primeraLeccionCompletada = true;
                System.out.println("🎉 ¡Primera lección completada!");
            }
            
            leccionActual++;
            GestorEjercicios.model.ProgresoSimple.guardarProgresoBasico(usuario, lenguaje, nivel, leccionActual);
            
            // Mostrar progreso actualizado
            GestorEjercicios.model.ProgresoSimple.mostrarProgreso(usuario);
            
            System.out.println("✅ Progreso guardado en archivos TXT simples");
            
        } catch (Exception e) {
            System.err.println("❌ Error guardando progreso: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Reinicia el progreso del usuario usando el servicio
     * 🗑️ REFACTORIZADO: Usa servicio centralizado
     */
    public void reiniciarProgreso() {
        leccionActual = 1;
        primeraLeccionCompletada = false;
        lenguajeConfigurado = null;
        nivelConfigurado = null;
        tipoConfigurado = null;
        
        UserProgressService.reiniciarProgreso();
        LoggingService.info("Progreso reiniciado completamente");
    }
    
    /**
     * 📊 Obtiene estadísticas del usuario usando el servicio
     */
    public java.util.Map<String, Integer> obtenerEstadisticas() {
        return UserProgressService.obtenerEstadisticas(
            usuarioActual != null ? usuarioActual : "UsuarioDemo"
        );
    }
    
    /**
     * 📋 Obtiene información básica de lecciones completadas
     */
    public java.util.Map<String, Object> obtenerInfoLeccionesCompletadas() {
        try {
            java.util.Map<String, Integer> stats = obtenerEstadisticas();
            java.util.Map<String, Object> info = new java.util.HashMap<>();
            
            info.put("leccionesCompletadas", stats.getOrDefault("leccionesCompletadas", 0));
            info.put("puntuacionTotal", stats.getOrDefault("puntuacionTotal", 0));
            info.put("tiempoTotal", stats.getOrDefault("tiempoTotal", 0));
            info.put("ejerciciosResueltos", stats.getOrDefault("ejerciciosResueltos", 0));
            info.put("leccionActual", leccionActual);
            info.put("configuracionCompleta", UserProgressService.esConfiguracionValida(
                UserProgressService.cargarProgresoUsuario(usuarioActual)
            ));
            
            return info;
        } catch (Exception e) {
            LoggingService.error("Error obteniendo información de lecciones", e);
            return new java.util.HashMap<>();
        }
    }
    
    /**
     * Actualiza el estado visual de los botones según el progreso
     */
    private void actualizarEstadoBotones() {
        // Marcar lecciones completadas según el progreso
        if (leccionActual > 1) {
            btnSalir.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 2) {
            btnLeccion2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 3) {
            btnLeccion3.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 4) {
            btnConfiguracion.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 5) {
            btnEstadisticas.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
            btnCrearLeccion.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        }
        
        // Mostrar información de configuración si está disponible
        if (lenguajeConfigurado != null && nivelConfigurado != null && tipoConfigurado != null) {
            System.out.println("Configuración actual: " + lenguajeConfigurado + " - " + nivelConfigurado + " - " + tipoConfigurado);
        }
    }
    
    /**
     * Cierra la aplicación
     */
    private void salirAplicacion() {
        System.exit(0);
    }
    
    /**
     * Centra la ventana en la pantalla
     */
    private void centrarVentana(Stage stage) {
        stage.centerOnScreen();
    }
    
    /**
     * Cierra la ventana actual
     */
    private void cerrarVentanaActual() {
        Stage currentStage = (Stage) btnCrearLeccion.getScene().getWindow();
        currentStage.close();
    }
} 