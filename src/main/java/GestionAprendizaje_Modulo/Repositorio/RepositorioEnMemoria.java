package GestionAprendizaje_Modulo.Repositorio;

import GestionAprendizaje_Modulo.Modelo.*;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;

import java.util.*;

    // Usamos el patrón Singleton para asegurar UNA SOLA fuente de datos en toda la GUI.
    public class RepositorioEnMemoria {

        // --- LA IMPLEMENTACIÓN CORRECTA DEL SINGLETON ---
        // 1. Se crea la única instancia de forma privada y estática.
        private static final RepositorioEnMemoria INSTANCIA = new RepositorioEnMemoria();

        // 2. El constructor es privado para evitar que se creen más instancias desde fuera.
        private RepositorioEnMemoria() {
            precargarDatos(); // Llenamos con datos de ejemplo al iniciar
        }

        // 3. Se proporciona un método público y estático para obtener ESA ÚNICA instancia.
        public static RepositorioEnMemoria getInstancia() {
            return INSTANCIA;
        }
        // -----------------------------------------------


        // --- COLECCIONES QUE SIMULAN LA BASE DE DATOS ---
        private final Map<String, Curso> cursos = new HashMap<>();
        private final List<Ruta> rutasIndependientes = new ArrayList<>();
        private final List<ModuloEducativo> modulosIndependientes = new ArrayList<>();


        // --- MÉTODOS DE ACCESO A LOS DATOS (GETTERS) ---
        public Map<String, Curso> getCursos() {
            return cursos;
        }

        public List<Ruta> getRutas() {
            return rutasIndependientes;
        }

        public List<ModuloEducativo> getModulos() {
            return modulosIndependientes;
        }


        /**
         * Esta función crea un escenario de ejemplo completo para que la aplicación
         * no comience vacía. Simula el trabajo que un administrador ya habría hecho.
         */
        private void precargarDatos() {
            System.out.println(">>> Repositorio: Precargando datos de ejemplo...");

            // Lecciones
            Leccion leccionVars = new Leccion(UUID.randomUUID().toString(), "Variables y Tipos de Datos", "Aprende los fundamentos de las variables en Java.", "PUBLICADO");
            Leccion leccionOps = new Leccion(UUID.randomUUID().toString(), "Operadores Aritméticos", "Cómo realizar sumas, restas y más.", "PUBLICADO");
            Leccion leccionPoo = new Leccion(UUID.randomUUID().toString(), "Introducción a la POO", "Conceptos de Clases y Objetos.", "PUBLICADO");

            // Módulos Educativos
            ModuloEducativo moduloBasico = new ModuloEducativo(UUID.randomUUID().toString(), "Java Básico", "1.0", "Conceptos iniciales de Java", "activo");
            moduloBasico.getLecciones().add(leccionVars);
            moduloBasico.getLecciones().add(leccionOps);
            modulosIndependientes.add(moduloBasico);

            // Rutas de Aprendizaje
            Ruta rutaPrincipiante = new Ruta(UUID.randomUUID().toString(), "Ruta del Novato en Java", "Camino guiado para empezar a programar.");
            NodoRuta nodo1 = new NodoRuta(1, leccionVars);
            NodoRuta nodo2 = new NodoRuta(2, leccionOps);
            rutaPrincipiante.agregarNodo(nodo1);
            rutaPrincipiante.agregarNodo(nodo2);
            rutasIndependientes.add(rutaPrincipiante);

            // Curso
            Curso cursoJavaCompleto = new Curso("Curso Completo de Java", "De cero a profesional en Java.");

            // Conectar todo
            cursoJavaCompleto.getModulos().add(moduloBasico);
            cursoJavaCompleto.getRutas().add(rutaPrincipiante);
            cursos.put(cursoJavaCompleto.getId(), cursoJavaCompleto);

            // Enriquecer contenido
            RecursoAprendizaje videoIntro = new Video("Video: ¿Qué es una variable?", "youtube.com/watch?v=123", 450);
            nodo1.agregarMaterialDeApoyo(videoIntro);

            System.out.println(">>> ¡Datos de ejemplo cargados con éxito! <<<");
        }
    }

