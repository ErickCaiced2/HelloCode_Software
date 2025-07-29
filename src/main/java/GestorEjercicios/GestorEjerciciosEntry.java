package GestorEjercicios;

import GestorEjercicios.integracion.IGestorEjercicios;
import GestorEjercicios.model.Leccion;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.model.GestorLeccionesBasadoEnArchivos;
import Modulo_Usuario.Clases.Usuario;
import MetodosGlobales.SesionManager;

import java.util.List;

/**
 * 🎯 Punto de entrada para el módulo GestorEjercicios
 * 
 * Esta clase proporciona una API simplificada para interactuar con el GestorEjercicios
 * desde otros módulos. Incluye integración automática con el sistema de usuarios.
 * 
 * 🔑 CARACTERÍSTICAS PRINCIPALES:
 * - Obtención automática del usuario actual (SesionManager, Comunidad, Gamificación)
 * - Métodos simplificados para ejecutar lecciones
 * - Sistema de diagnóstico y pruebas
 * - Integración con sistema TXT de lecciones
 * 
 * 📋 MÉTODOS PRINCIPALES:
 * - obtenerUsuarioActual(): Obtiene el usuario logueado automáticamente
 * - ejecutarLeccionParaUsuarioActual(): Ejecuta lección sin especificar usuario
 * - verificarUsuarioActual(): Verifica que hay un usuario logueado
 * - diagnosticoRapido(): Muestra el estado del sistema
 * 
 * @author Tu equipo de desarrollo
 * @version 2.0 - Integración con SesionManager
 */
public class GestorEjerciciosEntry {
    
    private static final IGestorEjercicios gestor = GestorEjerciciosPrincipal.obtenerInstancia();
    
    /**
     * Obtiene la instancia del gestor de ejercicios
     */
    public static IGestorEjercicios obtenerGestor() {
        return gestor;
    }
    
    /**
     * Crea una nueva lección normal
     */
    public static Leccion crearLeccionNormal(String nombre, List<?> ejercicios) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.NORMAL, 15, 5);
    }
    
    /**
     * Crea una nueva lección de prueba
     */
    public static Leccion crearLeccionPrueba(String nombre, List<?> ejercicios) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.PRUEBA, 30, 0);
    }
    
    /**
     * Crea una nueva lección personalizada
     */
    public static Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento) {
        return gestor.crearLeccion(nombre, ejercicios, tipo, experiencia, conocimiento);
    }

    /**
     * Crea una nueva lección con dificultad y lenguaje específicos
     */
    public static Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento, 
                                      NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        return gestor.crearLeccion(nombre, ejercicios, tipo, experiencia, conocimiento, dificultad, lenguaje);
    }

    /**
     * Crea una nueva lección normal con dificultad y lenguaje específicos
     */
    public static Leccion crearLeccionNormal(String nombre, List<?> ejercicios, NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.NORMAL, 15, 5, dificultad, lenguaje);
    }

    /**
     * Crea una nueva lección de prueba con dificultad y lenguaje específicos
     */
    public static Leccion crearLeccionPrueba(String nombre, List<?> ejercicios, NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.PRUEBA, 30, 0, dificultad, lenguaje);
    }
    
    /**
     * Obtiene una lección por su ID
     */
    public static Leccion obtenerLeccion(int id) {
        return gestor.obtenerLeccion(id);
    }
    
    /**
     * Obtiene todas las lecciones disponibles
     */
    public static List<Leccion> obtenerTodasLasLecciones() {
        return gestor.obtenerTodasLasLecciones();
    }
    
    /**
     * Marca una lección como completada para un usuario
     */
    public static void marcarLeccionCompletada(Leccion leccion, Usuario usuario, int aciertos) {
        gestor.marcarLeccionCompletada(leccion, usuario, aciertos);
    }
    
    /**
     * Obtiene el progreso de un usuario en una lección específica
     */
    public static double obtenerProgresoUsuario(Leccion leccion, Usuario usuario) {
        return gestor.obtenerProgresoUsuario(leccion, usuario);
    }
    
    /**
     * Obtiene las estadísticas de un usuario
     */
    public static IGestorEjercicios.EstadisticasUsuario obtenerEstadisticasUsuario(Usuario usuario) {
        return gestor.obtenerEstadisticasUsuario(usuario);
    }
    
    /**
     * Ejecuta una lección para un usuario
     */
    public static GestorEjercicios.model.ResultadoEvaluacion ejecutarLeccion(Leccion leccion, Usuario usuario) {
        return gestor.ejecutarLeccion(leccion, usuario);
    }
    
    /**
     * 🔥 NUEVO: Ejecuta una lección para el usuario actual automáticamente
     * Usa SesionManager para obtener el usuario logueado
     */
    public static GestorEjercicios.model.ResultadoEvaluacion ejecutarLeccionParaUsuarioActual(Leccion leccion) {
        Usuario usuarioActual = obtenerUsuarioActual();
        if (usuarioActual != null) {
            System.out.println("🎮 Ejecutando lección para: " + usuarioActual.getUsername());
            return gestor.ejecutarLeccion(leccion, usuarioActual);
        } else {
            System.err.println("❌ No hay usuario autenticado para ejecutar la lección");
            return null;
        }
    }
    
    /**
     * 🔥 NUEVO: Obtiene estadísticas del usuario actual automáticamente
     */
    public static IGestorEjercicios.EstadisticasUsuario obtenerEstadisticasUsuarioActual() {
        Usuario usuarioActual = obtenerUsuarioActual();
        if (usuarioActual != null) {
            return gestor.obtenerEstadisticasUsuario(usuarioActual);
        } else {
            System.err.println("❌ No hay usuario autenticado para obtener estadísticas");
            return null;
        }
    }
    
    /**
     * 🔥 NUEVO: Marca una lección como completada para el usuario actual
     */
    public static void marcarLeccionCompletadaParaUsuarioActual(Leccion leccion, int aciertos) {
        Usuario usuarioActual = obtenerUsuarioActual();
        if (usuarioActual != null) {
            System.out.println("✅ Marcando lección completada para: " + usuarioActual.getUsername());
            gestor.marcarLeccionCompletada(leccion, usuarioActual, aciertos);
        } else {
            System.err.println("❌ No hay usuario autenticado para marcar lección");
        }
    }
    
    /**
     * 🔥 NUEVO: Obtiene el progreso del usuario actual en una lección
     */
    public static double obtenerProgresoUsuarioActual(Leccion leccion) {
        Usuario usuarioActual = obtenerUsuarioActual();
        if (usuarioActual != null) {
            return gestor.obtenerProgresoUsuario(leccion, usuarioActual);
        } else {
            System.err.println("❌ No hay usuario autenticado para obtener progreso");
            return 0.0;
        }
    }
    
    /**
     * Inicializa el módulo GestorEjercicios
     * Debe ser llamado al inicio de la aplicación
     */
    public static void inicializar() {
        System.out.println("=== Módulo GestorEjercicios inicializado ===");
        System.out.println("Gestor de ejercicios listo para recibir lecciones");
        System.out.println("Tipos de lecciones soportados: NORMAL, PRUEBA");
        System.out.println("Tipos de ejercicios soportados: Selección múltiple, Completar código");
        System.out.println("=============================================");
    }
    
    /**
     * Obtiene información del estado del módulo
     */
    public static String obtenerEstadoModulo() {
        List<Leccion> lecciones = gestor.obtenerTodasLasLecciones();
        int leccionesNormales = (int) lecciones.stream()
            .filter(l -> l.getTipo() == TipoLeccion.NORMAL).count();
        int leccionesPrueba = (int) lecciones.stream()
            .filter(l -> l.getTipo() == TipoLeccion.PRUEBA).count();
        
        return String.format("GestorEjercicios - Total lecciones: %d (Normales: %d, Pruebas: %d)", 
                           lecciones.size(), leccionesNormales, leccionesPrueba);
    }
    
    /**
     * 🔑 Obtiene el usuario actual de la sesión
     * PRIORIDAD: 1) SesionManager, 2) ContextoSistema (Comunidad), 3) Gamificación
     * @return Usuario actual de la sesión, null si no hay usuario logueado
     */
    public static Usuario obtenerUsuarioActual() {
        System.out.println("🔍 === INICIANDO BÚSQUEDA DE USUARIO ACTUAL ===");
        
        // 🥇 PRIORIDAD 1: SesionManager (recomendado)
        try {
            System.out.println("🔍 Paso 1: Verificando SesionManager...");
            SesionManager sesion = SesionManager.getInstancia();
            
            if (sesion.hayUsuarioAutenticado()) {
                Usuario usuario = sesion.getUsuarioAutenticado();
                System.out.println("✅ Usuario encontrado en SesionManager: " + usuario.getUsername());
                System.out.println("   📧 Email: " + (usuario.getEmail() != null ? usuario.getEmail() : "No especificado"));
                System.out.println("   🏷️ Nombre: " + (usuario.getNombre() != null ? usuario.getNombre() : "No especificado"));
                return usuario;
            } else {
                System.out.println("⚠️ SesionManager no tiene usuario autenticado");
            }
        } catch (Exception e) {
            System.err.println("❌ Error con SesionManager: " + e.getMessage());
        }
        
        // 🥈 PRIORIDAD 2: ContextoSistema (Módulo Comunidad)
        try {
            System.out.println("🔍 Paso 2: Intentando obtener ContextoSistema...");
            Comunidad_Modulo.controladores.ContextoSistema contexto = 
                Comunidad_Modulo.controladores.ContextoSistema.getInstance();
            System.out.println("✅ ContextoSistema obtenido correctamente");
                
            if (contexto.getModuloUsuarios() != null) {
                System.out.println("🔍 ModuloUsuarios encontrado, obteniendo usuario actual...");
                var usuarioComunidad = contexto.getModuloUsuarios().obtenerUsuarioActual();
                if (usuarioComunidad != null) {
                    System.out.println("✅ Usuario encontrado en Comunidad: " + usuarioComunidad.getUsername());
                    // Convertir UsuarioComunidad a Usuario
                    Usuario usuario = new Usuario(
                        usuarioComunidad.getUsername(),
                        usuarioComunidad.getPassword(),
                        usuarioComunidad.getNombre(),
                        usuarioComunidad.getEmail()
                    );
                    
                    System.out.println("✅ Usuario convertido desde Comunidad: " + usuario.getUsername());
                    return usuario;
                } else {
                    System.out.println("❌ UsuarioComunidad es null en ModuloUsuarios");
                }
            } else {
                System.out.println("❌ ModuloUsuarios es null en ContextoSistema");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al obtener usuario desde Comunidad: " + e.getMessage());
        }
        
        // 🥉 PRIORIDAD 3: Gamificación (último recurso)
        System.out.println("⚠️ Intentando último recurso: Gamificación...");
        return obtenerUsuarioDesdeGamificacion();
    }
    
    /**
     * 🎮 Método de respaldo para obtener usuario desde Gamificación
     * Usa reflexión para evitar dependencias directas
     */
    private static Usuario obtenerUsuarioDesdeGamificacion() {
        System.out.println("🔍 === FALLBACK: BUSCANDO EN GAMIFICACIÓN ===");
        try {
            System.out.println("🔍 Paso 3: Intentando obtener usuarios de Gamificación via reflexión...");
            
            // Usar reflexión para acceder a Gamificación sin dependencia directa
            Class<?> gamificacionMain = Class.forName("Gamificacion_Modulo.Main");
            java.lang.reflect.Method getUsuarios = gamificacionMain.getMethod("getUsuarios");
            Object usuariosObj = getUsuarios.invoke(null);
            
            if (usuariosObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<Usuario> usuarios = (java.util.List<Usuario>) usuariosObj;
                System.out.println("🔍 Usuarios encontrados en Gamificación: " + usuarios.size());
                
                if (!usuarios.isEmpty()) {
                    Usuario usuario = usuarios.get(0);
                    System.out.println("✅ Usuario obtenido desde Gamificación: " + usuario.getUsername());
                    System.out.println("   📧 Email: " + (usuario.getEmail() != null ? usuario.getEmail() : "No especificado"));
                    System.out.println("   🏷️ Nombre: " + (usuario.getNombre() != null ? usuario.getNombre() : "No especificado"));
                    return usuario;
                } else {
                    System.out.println("❌ Lista de usuarios de Gamificación está vacía");
                }
            } else {
                System.out.println("❌ getUsuarios() no devolvió una Lista");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Módulo Gamificación no encontrado: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("❌ Método getUsuarios() no encontrado en Gamificación: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error al obtener usuario desde Gamificación: " + e.getMessage());
        }
        
        System.err.println("❌ === NO SE PUDO OBTENER USUARIO DE NINGÚN MÓDULO ===");
        System.err.println("🔍 Verifica que:");
        System.err.println("   1. Hayas hecho login en el sistema (SesionManager)");
        System.err.println("   2. El módulo de Comunidad esté inicializado");
        System.err.println("   3. Gamificación tenga usuarios cargados");
        return null;
    }
    
    /**
     * 🧪 MÉTODO DE PRUEBA: Verificar que el sistema de usuarios funciona correctamente
     * Muestra información detallada del usuario actual y verifica la integración
     */
    public static void probarSistemaUsuarios() {
        System.out.println("\n🧪 === PRUEBA DEL SISTEMA DE USUARIOS ===");
        
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            System.out.println("✅ Usuario obtenido correctamente:");
            System.out.println("   👤 Username: " + usuario.getUsername());
            System.out.println("   📧 Email: " + (usuario.getEmail() != null ? usuario.getEmail() : "No especificado"));
            System.out.println("   🏷️ Nombre: " + (usuario.getNombre() != null ? usuario.getNombre() : "No especificado"));
            System.out.println("   🌟 XP: " + usuario.getXp());
            System.out.println("   👑 Rol: " + usuario.getRol());
            
            // Probar que se pueden obtener estadísticas
            try {
                IGestorEjercicios.EstadisticasUsuario stats = obtenerEstadisticasUsuario(usuario);
                System.out.println("📊 Estadísticas del usuario:");
                System.out.println("   🌟 Experiencia: " + stats.getExperienciaTotal());
                System.out.println("   📚 Lecciones completadas: " + stats.getLeccionesCompletadas());
                System.out.println("   🧠 Conocimiento: " + stats.getConocimientoTotal());
            } catch (Exception e) {
                System.err.println("⚠️ Error al obtener estadísticas: " + e.getMessage());
            }
            
        } else {
            System.err.println("❌ No se pudo obtener usuario actual");
            System.err.println("   🔍 Verificar que:");
            System.err.println("   1. SesionManager tenga usuario autenticado");
            System.err.println("   2. El módulo de Comunidad esté inicializado");
            System.err.println("   3. Gamificación tenga usuarios cargados");
        }
        
        System.out.println("🧪 === FIN DE LA PRUEBA ===\n");
    }
    
    /**
     * 🎯 NUEVO: Prueba rápida del usuario actual
     * Versión simplificada para verificar que todo funciona
     */
    public static boolean verificarUsuarioActual() {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            System.out.println("✅ Usuario verificado: " + usuario.getUsername());
            return true;
        } else {
            System.out.println("❌ No se pudo verificar usuario actual");
            return false;
        }
    }
    
    /**
     * 🔍 MÉTODO DE DEPURACIÓN: Mostrar todos los usuarios con progreso
     * Útil para verificar que los datos se guardan correctamente
     */
    public static void mostrarTodosLosUsuariosConProgreso() {
        GestorEjercicios.model.GestorProgresoUsuario.mostrarTodosLosProgresos();
    }
    
    /**
     * 🔍 MÉTODO DE DEPURACIÓN: Verificar si un usuario tiene progreso guardado
     */
    public static boolean usuarioTieneProgreso(String username) {
        return GestorEjercicios.model.GestorProgresoUsuario.tieneProgreso(username);
    }
    
    /**
     * 🚨 MÉTODO DE DIAGNÓSTICO RÁPIDO: Ejecutar antes de hacer una lección
     * Te dice exactamente qué está pasando con el sistema de usuarios
     */
    public static void diagnosticoRapido() {
        System.out.println("\n🚨 === DIAGNÓSTICO RÁPIDO DEL SISTEMA ===");
        
        // 0. Verificar SesionManager PRIMERO
        try {
            SesionManager sesion = SesionManager.getInstancia();
            if (sesion.hayUsuarioAutenticado()) {
                Usuario usuario = sesion.getUsuarioAutenticado();
                System.out.println("✅ SesionManager tiene usuario: " + usuario.getUsername());
            } else {
                System.out.println("❌ SesionManager NO tiene usuario autenticado");
            }
        } catch (Exception e) {
            System.out.println("❌ Error con SesionManager: " + e.getMessage());
        }
        
        // 1. Verificar ContextoSistema
        try {
            var contexto = Comunidad_Modulo.controladores.ContextoSistema.getInstance();
            System.out.println("✅ ContextoSistema accesible");
            
            if (contexto.getModuloUsuarios() != null) {
                System.out.println("✅ ModuloUsuarios presente");
                var usuario = contexto.getModuloUsuarios().obtenerUsuarioActual();
                if (usuario != null) {
                    System.out.println("✅ Usuario actual en Comunidad: " + usuario.getUsername());
                } else {
                    System.out.println("❌ Usuario actual en Comunidad es NULL");
                }
            } else {
                System.out.println("❌ ModuloUsuarios es NULL");
            }
        } catch (Exception e) {
            System.out.println("❌ Error con ContextoSistema: " + e.getMessage());
        }
        
        // 2. Verificar Gamificación con reflexión
        try {
            Class<?> gamificacionMain = Class.forName("Gamificacion_Modulo.Main");
            java.lang.reflect.Method getUsuarios = gamificacionMain.getMethod("getUsuarios");
            Object usuariosObj = getUsuarios.invoke(null);
            
            if (usuariosObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<Usuario> usuarios = (java.util.List<Usuario>) usuariosObj;
                System.out.println("✅ Gamificación accesible - Usuarios: " + usuarios.size());
                if (!usuarios.isEmpty()) {
                    System.out.println("   👤 Primer usuario: " + usuarios.get(0).getUsername());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error con Gamificación: " + e.getMessage());
        }
        
        // 3. Probar obtener usuario actual
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            System.out.println("✅ RESULTADO: Usuario obtenido = " + usuario.getUsername());
        } else {
            System.out.println("❌ RESULTADO: NO se pudo obtener usuario");
        }
        
        System.out.println("🚨 === FIN DIAGNÓSTICO ===\n");
    }
    
    // ==========================================
    // 📚 SISTEMA TXT - FUENTE DE DATOS DE LECCIONES
    // ==========================================
    
    /**
     * 🎲 Obtiene una lección aleatoria desde archivos TXT
     */
    public static GestorLeccionesBasadoEnArchivos.LeccionCompleta obtenerLeccionAleatoria(String lenguaje, String nivel) {
        return GestorLeccionesBasadoEnArchivos.obtenerLeccionAleatoria(lenguaje.toUpperCase(), nivel.toUpperCase());
    }
    
    /**
     * 📊 Obtiene estadísticas de lecciones disponibles desde archivos TXT
     */
    public static java.util.Map<String, Integer> obtenerEstadisticasLeccionesTXT() {
        return GestorLeccionesBasadoEnArchivos.obtenerEstadisticas();
    }
    
    /**
     * 📚 Obtiene todas las lecciones disponibles desde archivos TXT
     */
    public static java.util.List<GestorLeccionesBasadoEnArchivos.LeccionDefinicion> obtenerLeccionesTXT(String lenguaje) {
        return GestorLeccionesBasadoEnArchivos.obtenerLecciones(lenguaje.toUpperCase());
    }
    
    // ==========================================
    // 📊 UTILIDADES INTERNAS
    // ==========================================
    
    /**
     * 🎯 Obtiene una lección completa con ejercicios desde TXT
     */
    public static GestorLeccionesBasadoEnArchivos.LeccionCompleta obtenerLeccionCompletaTXT(String lenguaje, String nivel) {
        System.out.println("🎯 Obteniendo lección desde TXT: " + lenguaje + " - " + nivel);
        return GestorLeccionesBasadoEnArchivos.obtenerLeccionAleatoria(lenguaje.toUpperCase(), nivel.toUpperCase());
    }
    
    /**
     * 🎲 Inicia una lección usando el sistema TXT
     */
    public static boolean iniciarLeccionTXT(String lenguaje, String nivel) {
        try {
            GestorLeccionesBasadoEnArchivos.LeccionCompleta leccion = obtenerLeccionCompletaTXT(lenguaje, nivel);
            if (leccion != null) {
                System.out.println("✅ Lección TXT cargada: " + leccion.titulo);
                System.out.println("   🎮 Ejercicios: " + leccion.ejercicios.size());
                System.out.println("   📝 Descripción: " + leccion.descripcion);
                return true;
            } else {
                System.out.println("❌ No se encontró lección para " + lenguaje + " - " + nivel);
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ Error iniciando lección TXT: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 📊 Muestra estadísticas completas del sistema TXT
     */
    public static void mostrarEstadisticasTXT() {
        System.out.println("\n📊 === ESTADÍSTICAS DEL SISTEMA TXT ===");
        java.util.Map<String, Integer> stats = GestorLeccionesBasadoEnArchivos.obtenerEstadisticas();
        for (java.util.Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.println("   " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("==========================================\n");
    }
    
    /**
     * 🧪 MÉTODO DE PRUEBA: Prueba completa del sistema TXT
     */
    public static void probarSistemaTXT() {
        System.out.println("\n🧪 === PRUEBA COMPLETA DEL SISTEMA TXT ===");
        
        // 1. Mostrar estadísticas
        mostrarEstadisticasTXT();
        
        // 2. Probar obtener lección
        boolean resultado = iniciarLeccionTXT("JAVA", "BASICO");
        System.out.println("🎯 Resultado de la prueba: " + (resultado ? "✅ ÉXITO" : "❌ FALLO"));
        
        // 3. Probar con diferentes lenguajes
        String[] lenguajes = {"JAVA", "PYTHON", "C"};
        String[] niveles = {"BASICO", "INTERMEDIO", "AVANZADO"};
        
        for (String lenguaje : lenguajes) {
            for (String nivel : niveles) {
                GestorLeccionesBasadoEnArchivos.LeccionCompleta leccion = obtenerLeccionCompletaTXT(lenguaje, nivel);
                if (leccion != null) {
                    System.out.println("✅ " + lenguaje + " " + nivel + ": " + leccion.titulo + " (" + leccion.ejercicios.size() + " ejercicios)");
                } else {
                    System.out.println("❌ " + lenguaje + " " + nivel + ": No disponible");
                }
            }
        }
        
        System.out.println("🧪 === FIN PRUEBA SISTEMA TXT ===\n");
    }
} 