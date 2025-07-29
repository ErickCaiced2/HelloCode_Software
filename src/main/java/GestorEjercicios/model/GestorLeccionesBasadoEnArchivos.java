package GestorEjercicios.model;

import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.NivelDificultad;
import Modulo_Ejercicios.exercises.Lenguaje;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 📚 Gestor de lecciones basado en archivos TXT que usa ejercicios reales del Módulo_Ejercicios
 * Reemplaza el sistema anterior de LeccionesPredefinidas con una integración completa
 */
public class GestorLeccionesBasadoEnArchivos {
    
    private static final String RUTA_BASE = "src/main/resources/Modulo_GestorEjercicios/data/";
    private static final Map<String, List<LeccionDefinicion>> leccionesPorLenguaje = new HashMap<>();
    private static boolean inicializado = false;
    
    /**
     * 📋 Definición de una lección cargada desde archivo TXT
     */
    public static class LeccionDefinicion {
        public String titulo;
        public String nivel;
        public String descripcion;
        public int duracionMinutos;
        public int cantidadEjercicios;
        public String filtroNivel;
        public String filtroLenguaje;
        
        public LeccionDefinicion(String titulo, String nivel, String descripcion, 
                               int duracion, int cantidad, String filtroNivel, String filtroLenguaje) {
            this.titulo = titulo;
            this.nivel = nivel;
            this.descripcion = descripcion;
            this.duracionMinutos = duracion;
            this.cantidadEjercicios = cantidad;
            this.filtroNivel = filtroNivel;
            this.filtroLenguaje = filtroLenguaje;
        }
    }
    
    /**
     * 🎯 Información de lección completa con ejercicios reales
     */
    public static class LeccionCompleta {
        public String id;
        public String titulo;
        public String nivel;
        public String descripcion;
        public int duracionMinutos;
        public List<Object> ejercicios; // Mezcla de EjercicioSeleccion y EjercicioCompletarCodigo
        public String lenguaje;
        
        public LeccionCompleta(String id, LeccionDefinicion definicion, List<Object> ejercicios) {
            this.id = id;
            this.titulo = definicion.titulo;
            this.nivel = definicion.nivel;
            this.descripcion = definicion.descripcion;
            this.duracionMinutos = definicion.duracionMinutos;
            this.ejercicios = ejercicios;
            this.lenguaje = definicion.filtroLenguaje;
        }
    }
    
    /**
     * 🚀 Inicializa el sistema cargando lecciones desde archivos TXT
     */
    public static void inicializar() {
        if (inicializado) return;
        
        try {
            cargarLeccionesDesdeArchivo("java", "lecciones_java.txt");
            cargarLeccionesDesdeArchivo("python", "lecciones_python.txt");
            cargarLeccionesDesdeArchivo("cpp", "lecciones_cpp.txt");
            
            inicializado = true;
            System.out.println("✅ GestorLecciones inicializado correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error inicializando GestorLecciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 📄 Carga lecciones desde un archivo TXT específico
     */
    private static void cargarLeccionesDesdeArchivo(String lenguaje, String nombreArchivo) {
        List<LeccionDefinicion> lecciones = new ArrayList<>();
        String rutaCompleta = RUTA_BASE + nombreArchivo;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaCompleta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    LeccionDefinicion leccion = parsearLeccion(linea);
                    if (leccion != null) {
                        lecciones.add(leccion);
                    }
                }
            }
            
            leccionesPorLenguaje.put(lenguaje.toUpperCase(), lecciones);
            System.out.println("📚 Cargadas " + lecciones.size() + " lecciones de " + lenguaje);
            
        } catch (IOException e) {
            System.err.println("❌ Error cargando lecciones de " + lenguaje + ": " + e.getMessage());
        }
    }
    
    /**
     * 🔍 Parsea una línea del archivo TXT en una LeccionDefinicion
     * Formato: TITULO|NIVEL|DESCRIPCION|DURACION|CANTIDAD_EJERCICIOS|FILTRO_NIVEL|FILTRO_LENGUAJE
     */
    private static LeccionDefinicion parsearLeccion(String linea) {
        try {
            String[] partes = linea.split("\\|");
            if (partes.length >= 7) {
                return new LeccionDefinicion(
                    partes[0].trim(),  // titulo
                    partes[1].trim(),  // nivel
                    partes[2].trim(),  // descripcion
                    Integer.parseInt(partes[3].trim()), // duracion
                    Integer.parseInt(partes[4].trim()), // cantidad ejercicios
                    partes[5].trim(),  // filtro nivel
                    partes[6].trim()   // filtro lenguaje
                );
            }
        } catch (Exception e) {
            System.err.println("❌ Error parseando lección: " + linea + " - " + e.getMessage());
        }
        return null;
    }
    
    /**
     * 📋 Obtiene todas las lecciones disponibles para un lenguaje
     */
    public static List<LeccionDefinicion> obtenerLecciones(String lenguaje) {
        inicializar();
        return leccionesPorLenguaje.getOrDefault(lenguaje.toUpperCase(), new ArrayList<>());
    }
    
    /**
     * 🎲 Obtiene una lección aleatoria con ejercicios reales del Módulo_Ejercicios
     */
    public static LeccionCompleta obtenerLeccionAleatoria(String lenguaje, String nivel) {
        inicializar();
        
        List<LeccionDefinicion> lecciones = obtenerLecciones(lenguaje);
        List<LeccionDefinicion> leccionesFiltradas = lecciones.stream()
            .filter(l -> l.nivel.equalsIgnoreCase(nivel))
            .collect(Collectors.toList());
        
        if (leccionesFiltradas.isEmpty()) {
            System.out.println("⚠️ No hay lecciones para " + lenguaje + " - " + nivel);
            return null;
        }
        
        Random random = new Random();
        LeccionDefinicion definicion = leccionesFiltradas.get(random.nextInt(leccionesFiltradas.size()));
        
        // 🎯 Cargar ejercicios reales del Módulo_Ejercicios
        List<Object> ejercicios = cargarEjerciciosReales(definicion);
        
        String id = "leccion_" + lenguaje.toLowerCase() + "_" + System.currentTimeMillis();
        return new LeccionCompleta(id, definicion, ejercicios);
    }
    
    /**
     * 🎯 Obtiene una lección específica por índice
     */
    public static LeccionCompleta obtenerLeccionPorIndice(String lenguaje, String nivel, int indice) {
        inicializar();
        
        List<LeccionDefinicion> lecciones = obtenerLecciones(lenguaje);
        List<LeccionDefinicion> leccionesFiltradas = lecciones.stream()
            .filter(l -> l.nivel.equalsIgnoreCase(nivel))
            .collect(Collectors.toList());
        
        if (indice < 0 || indice >= leccionesFiltradas.size()) {
            return null;
        }
        
        LeccionDefinicion definicion = leccionesFiltradas.get(indice);
        List<Object> ejercicios = cargarEjerciciosReales(definicion);
        
        String id = "leccion_" + lenguaje.toLowerCase() + "_" + indice;
        return new LeccionCompleta(id, definicion, ejercicios);
    }
    
    /**
     * 🔥 Carga ejercicios reales del Módulo_Ejercicios basado en la definición de la lección
     */
    private static List<Object> cargarEjerciciosReales(LeccionDefinicion definicion) {
        List<Object> ejercicios = new ArrayList<>();
        
        try {
            // 📚 Cargar ejercicios de selección múltiple
            List<EjercicioSeleccion> ejerciciosSeleccion = EjercicioRepository.cargarEjerciciosSeleccion();
            
            // 💻 Cargar ejercicios de completar código  
            List<EjercicioCompletarCodigo> ejerciciosCompletar = EjercicioRepository.cargarEjerciciosCompletarCodigo();
            
            // 🎯 Filtrar por nivel y lenguaje
            NivelDificultad nivelFiltro = mapearNivel(definicion.filtroNivel);
            Lenguaje lenguajeFiltro = mapearLenguaje(definicion.filtroLenguaje);
            
            // Filtrar ejercicios de selección
            List<EjercicioSeleccion> seleccionFiltrados = ejerciciosSeleccion.stream()
                .filter(e -> e.getNivel() == nivelFiltro && e.getLenguaje() == lenguajeFiltro)
                .collect(Collectors.toList());
            
            // Filtrar ejercicios de completar código
            List<EjercicioCompletarCodigo> completarFiltrados = ejerciciosCompletar.stream()
                .filter(e -> e.getNivel() == nivelFiltro && e.getLenguaje() == lenguajeFiltro)
                .collect(Collectors.toList());
            
            // 🎲 Seleccionar ejercicios aleatoriamente hasta completar la cantidad requerida
            Random random = new Random();
            int ejerciciosDeseados = definicion.cantidadEjercicios;
            
            // Mezclar tipos de ejercicios
            List<Object> pool = new ArrayList<>();
            pool.addAll(seleccionFiltrados);
            pool.addAll(completarFiltrados);
            
            Collections.shuffle(pool, random);
            
            // Tomar los primeros ejercicios hasta completar la cantidad
            for (int i = 0; i < Math.min(ejerciciosDeseados, pool.size()); i++) {
                ejercicios.add(pool.get(i));
            }
            
            // Si no hay suficientes ejercicios, llenar con los disponibles repetidos
            while (ejercicios.size() < ejerciciosDeseados && !pool.isEmpty()) {
                ejercicios.add(pool.get(random.nextInt(pool.size())));
            }
            
            System.out.println("🎯 Lección '" + definicion.titulo + "' cargada con " + 
                             ejercicios.size() + " ejercicios reales (" + 
                             seleccionFiltrados.size() + " selección, " + 
                             completarFiltrados.size() + " completar)");
            
        } catch (Exception e) {
            System.err.println("❌ Error cargando ejercicios reales: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ejercicios;
    }
    
    /**
     * 🗺️ Mapea string de nivel a enum de Módulo_Ejercicios
     */
    private static NivelDificultad mapearNivel(String nivel) {
        try {
            return NivelDificultad.valueOf(nivel.toUpperCase());
        } catch (Exception e) {
            return NivelDificultad.BASICO; // Fallback
        }
    }
    
    /**
     * 🗺️ Mapea string de lenguaje a enum de Módulo_Ejercicios
     */
    private static Lenguaje mapearLenguaje(String lenguaje) {
        try {
            return Lenguaje.valueOf(lenguaje.toUpperCase());
        } catch (Exception e) {
            return Lenguaje.JAVA; // Fallback
        }
    }
    
    /**
     * 📊 Obtiene estadísticas del sistema de lecciones
     */
    public static Map<String, Integer> obtenerEstadisticas() {
        inicializar();
        
        Map<String, Integer> stats = new HashMap<>();
        int totalLecciones = 0;
        
        for (Map.Entry<String, List<LeccionDefinicion>> entry : leccionesPorLenguaje.entrySet()) {
            String lenguaje = entry.getKey();
            int cantidad = entry.getValue().size();
            stats.put("lecciones_" + lenguaje.toLowerCase(), cantidad);
            totalLecciones += cantidad;
        }
        
        stats.put("total_lecciones", totalLecciones);
        stats.put("lenguajes_soportados", leccionesPorLenguaje.size());
        
        return stats;
    }
    
    /**
     * 🔍 Busca lecciones por palabra clave
     */
    public static List<LeccionDefinicion> buscarLecciones(String lenguaje, String palabraClave) {
        inicializar();
        
        List<LeccionDefinicion> todasLecciones = obtenerLecciones(lenguaje);
        String busqueda = palabraClave.toLowerCase();
        
        return todasLecciones.stream()
            .filter(l -> l.titulo.toLowerCase().contains(busqueda) ||
                        l.descripcion.toLowerCase().contains(busqueda) ||
                        l.nivel.toLowerCase().contains(busqueda))
            .collect(Collectors.toList());
    }
}
