package GestionAprendizaje_Modulo.Aplicacion;
/**
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import GestionAprendizaje_Modulo.Gestor.GestorDeModuloEducativo;
import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Repositorio.RepositorioEnMemoria;
import GestionAprendizaje_Modulo.Repositorio.RepositorioModulosEducativos;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;

public class SistemaGestionContenido {

    private static final Scanner scanner = new Scanner(System.in);

    // --- NUESTRA FUENTE ÚNICA DE VERDAD (Simula una base de datos) ---
    // --> Estas colecciones 'static' son la clave. Ambos, Admin y Estudiante, las usarán.
    private static final Map<String, Curso> repositorioCursos = new HashMap<>();
    private static final List<Ruta> repositorioRutas = new ArrayList<>();
    private static final RepositorioModulosEducativos repositorioModulos = new RepositorioEnMemoria();

    public static void main(String[] args) {
        // --> He creado datos de ejemplo para que no tengas que crearlos cada vez que pruebas.
        precargarDatosDeEjemplo();

        while (true) {
            System.out.println("\n=============================================");
            System.out.println("   Bienvenido al Sistema HelloCode");
            System.out.println("=============================================");
            System.out.println("Seleccione su rol:");
            System.out.println("1. Administrador de contenido");
            System.out.println("2. Estudiante");
            System.out.println("3. Salir");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": menuAdministrador(); break;
                case "2": menuEstudiante(); break;
                case "3": System.out.println("Gracias por usar el sistema. ¡Hasta luego!"); return;
                default: System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    // --- MENÚS ---

    private static void menuAdministrador() {
        while (true) {
            // --> Menú refactorizado para un flujo de trabajo más lógico.
            System.out.println("\n--- MENÚ ADMINISTRADOR ---");
            System.out.println("1. Crear Curso");
            System.out.println("2. Crear Módulo Educativo (independiente)");
            System.out.println("3. Crear Ruta de Aprendizaje (independiente)");
            System.out.println("4. Asociar Módulo existente a un Curso");
            System.out.println("5. Asociar Ruta existente a un Curso");
            System.out.println("6. Enriquecer un Nodo de una Ruta con Material de Apoyo"); // <-- TU FUNCIÓN PRINCIPAL
            System.out.println("7. Volver al menú principal");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": crearCurso(); break;
                case "2": crearModulo(); break;
                case "3": crearRuta(); break;
                case "4": asociarModuloACurso(); break;
                case "5": asociarRutaACurso(); break;
                case "6": enriquecerNodoConRecurso(); break;
                case "7": return;
                default: System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    private static void menuEstudiante() {
        System.out.println("\n--- BIENVENIDO, ESTUDIANTE ---");
        // --> El estudiante primero elige un curso.
        Curso cursoSeleccionado = seleccionarCurso();
        if (cursoSeleccionado == null) return;

        // --> Una vez elegido el curso, puede ver sus rutas.
        Ruta rutaSeleccionada = seleccionarRuta(cursoSeleccionado);
        if (rutaSeleccionada == null) return;

        // --> El estudiante ahora "sigue" la ruta, viendo los nodos.
        seguirRuta(rutaSeleccionada);
    }

    // --- LÓGICA DEL ADMINISTRADOR ---

    private static void crearCurso() {
        System.out.print("Nombre del curso: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción del curso: ");
        String descripcion = scanner.nextLine();
        Curso curso = new Curso(nombre, descripcion);
        repositorioCursos.put(curso.getId(), curso);
        System.out.println("¡Curso creado con éxito! ID: " + curso.getId());
    }

    private static void crearModulo() {
        System.out.print("Título del módulo: ");
        String titulo = scanner.nextLine();
        // --> Se crea el módulo y se guarda en su repositorio. Aún no está asociado a ningún curso.
        ModuloEducativo modulo = new ModuloEducativo(UUID.randomUUID().toString(), titulo, "1.0", "","activo");
        repositorioModulos.guardarModuloEducativo(modulo);
        System.out.println("¡Módulo '" + titulo + "' creado y listo para ser asociado!");
    }

    private static void crearRuta() {
        System.out.print("Nombre de la ruta: ");
        String nombre = scanner.nextLine();
        Ruta ruta = new Ruta(UUID.randomUUID().toString(), nombre, "");
        System.out.println("Ahora, agregue lecciones a la ruta.");
        int orden = 1;
        while (true) {
            System.out.print("Título de la lección (o presione Enter para terminar): ");
            String tituloLeccion = scanner.nextLine();
            if (tituloLeccion.isBlank()) break;
            Leccion leccion = new Leccion(UUID.randomUUID().toString(), tituloLeccion, "", "BORRADOR");
            NodoRuta nodo = new NodoRuta(orden++, leccion);
            ruta.agregarNodo(nodo);
            System.out.println("Nodo con lección '" + tituloLeccion + "' agregado.");
        }
        // --> La ruta se guarda en su repositorio, lista para ser asociada a un curso.
        repositorioRutas.add(ruta);
        System.out.println("¡Ruta '" + nombre + "' creada y lista para ser asociada!");
    }

    // --> ¡NUEVO Y CLAVE! Conecta un módulo con un curso.
    private static void asociarModuloACurso() {
        System.out.println("--- Asociar Módulo a Curso ---");
        Curso curso = seleccionarCurso();
        if (curso == null) return;
        ModuloEducativo modulo = seleccionarModulo();
        if (modulo == null) return;

        // --> Aquí ocurre la magia: modificamos el objeto Curso.
        curso.getModulos().add(modulo);
        System.out.println("¡Módulo '" + modulo.getTitulo() + "' asociado al curso '" + curso.getNombre() + "'!");
    }

    // --> ¡NUEVO Y CLAVE! Conecta una ruta con un curso.
    private static void asociarRutaACurso() {
        System.out.println("--- Asociar Ruta a Curso ---");
        Curso curso = seleccionarCurso();
        if (curso == null) return;
        Ruta ruta = seleccionarRutaIndependiente(); // Selecciona de la lista global
        if (ruta == null) return;

        // --> Aquí ocurre la magia: modificamos el objeto Curso.
        curso.getRutas().add(ruta);
        System.out.println("¡Ruta '" + ruta.getNombre() + "' asociada al curso '" + curso.getNombre() + "'!");
    }

    // --> ¡NUEVO Y CLAVE! Este es el corazón de tu módulo.
    private static void enriquecerNodoConRecurso() {
        System.out.println("--- Enriquecer Nodo con Material de Apoyo ---");
        Curso curso = seleccionarCurso();
        if (curso == null) return;
        Ruta ruta = seleccionarRuta(curso); // Selecciona una ruta DENTRO del curso
        if (ruta == null) return;
        NodoRuta nodo = seleccionarNodo(ruta);
        if (nodo == null) return;

        // --> Ya no se crea un recurso al aire, se crea PARA este nodo.
        RecursoAprendizaje recurso = crearRecursoInteractivo();
        if (recurso != null) {
            // --> ¡LA CONEXIÓN FINAL! El recurso se añade al nodo específico.
            nodo.agregarMaterialDeApoyo(recurso);
            System.out.println("¡Recurso '" + recurso.getTitulo() + "' añadido al nodo " + nodo.getOrden() + "!");
        }
    }

    // --> Refactorizado para devolver el recurso creado.
    private static RecursoAprendizaje crearRecursoInteractivo() {
        System.out.println("Seleccione tipo de recurso a crear:");
        System.out.println("1. Video");
        System.out.println("2. Documento PDF");
        System.out.println("3. Artículo");
        System.out.print("Opción: ");
        String tipo = scanner.nextLine();

        System.out.print("Título del recurso: ");
        String titulo = scanner.nextLine();

        try {
            switch (tipo) {
                case "1":
                    System.out.print("URL del video: "); String urlV = scanner.nextLine();
                    System.out.print("Duración en segundos: "); int duracion = Integer.parseInt(scanner.nextLine());
                    return new Video(titulo, urlV, duracion);
                case "2":
                    System.out.print("URL de descarga del PDF: "); String urlP = scanner.nextLine();
                    System.out.print("Número de páginas: "); int paginas = Integer.parseInt(scanner.nextLine());
                    return new DocumentoPDF(titulo, urlP, paginas);
                case "3":
                    System.out.print("URL del artículo: "); String urlA = scanner.nextLine();
                    return new Articulo(titulo, urlA);
                default:
                    System.out.println("Tipo inválido.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Entrada numérica no válida.");
            return null;
        }
    }

    // --- LÓGICA DEL ESTUDIANTE ---

    private static void seguirRuta(Ruta ruta) {
        System.out.println("\n--- Siguiendo la Ruta: " + ruta.getNombre() + " ---");
        while(true) {
            NodoRuta nodoSeleccionado = seleccionarNodo(ruta);
            if (nodoSeleccionado == null) {
                // Si el usuario no elige un nodo (ej. introduce 0 para salir)
                System.out.println("Has salido de la ruta.");
                break;
            }

            // --> El estudiante ahora ve el contenido del nodo.
            System.out.println("\n-------------------------------------------");
            System.out.println("  Viendo Nodo " + nodoSeleccionado.getOrden() + ": " + nodoSeleccionado.getLeccion().getTitulo());
            System.out.println("-------------------------------------------");
            System.out.println("  Descripción de la lección: " + nodoSeleccionado.getLeccion().getDescripcion());
            System.out.println("\n  --- Material de Apoyo Añadido por el Profesor ---");

            // --> Se leen los recursos del nodo que el admin añadió.
            if (nodoSeleccionado.getMaterialDeApoyo().isEmpty()) {
                System.out.println("  (No hay material de apoyo para esta lección)");
            } else {
                for (RecursoAprendizaje recurso : nodoSeleccionado.getMaterialDeApoyo()) {
                    System.out.println("  > " + recurso.getTitulo() + " (" + recurso.getClass().getSimpleName() + ")");
                    System.out.println("    Detalle: " + recurso.obtenerDetalle());
                }
            }
            System.out.println("-------------------------------------------");

            System.out.print("\nPresiona Enter para volver a la lista de nodos...");
            scanner.nextLine();
        }
    }

    // --- MÉTODOS AUXILIARES DE SELECCIÓN (REUTILIZABLES) ---

    private static Curso seleccionarCurso() {
        System.out.println("\n--- Cursos Disponibles ---");
        if (repositorioCursos.isEmpty()) {
            System.out.println("No hay cursos disponibles.");
            return null;
        }
        List<Curso> listaCursos = new ArrayList<>(repositorioCursos.values());
        for (int i = 0; i < listaCursos.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, listaCursos.get(i).getNombre());
        }
        System.out.print("Seleccione un curso por número (o 0 para cancelar): ");
        int indice = Integer.parseInt(scanner.nextLine());
        if (indice == 0) return null;
        return listaCursos.get(indice - 1);
    }

    private static ModuloEducativo seleccionarModulo() {
        System.out.println("\n--- Módulos Disponibles ---");
        List<ModuloEducativo> modulos = repositorioModulos.buscarTodos(); // Asumiendo que este método existe
        if (modulos.isEmpty()) {
            System.out.println("No hay módulos disponibles para asociar.");
            return null;
        }
        for (int i = 0; i < modulos.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, modulos.get(i).getTitulo());
        }
        System.out.print("Seleccione un módulo por número (o 0 para cancelar): ");
        int indice = Integer.parseInt(scanner.nextLine());
        if (indice == 0) return null;
        return modulos.get(indice - 1);
    }

    private static Ruta seleccionarRutaIndependiente() {
        System.out.println("\n--- Rutas Disponibles ---");
        if (repositorioRutas.isEmpty()) {
            System.out.println("No hay rutas disponibles para asociar.");
            return null;
        }
        for (int i = 0; i < repositorioRutas.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, repositorioRutas.get(i).getNombre());
        }
        System.out.print("Seleccione una ruta por número (o 0 para cancelar): ");
        int indice = Integer.parseInt(scanner.nextLine());
        if (indice == 0) return null;
        return repositorioRutas.get(indice - 1);
    }

    private static Ruta seleccionarRuta(Curso curso) {
        System.out.println("\n--- Rutas en el curso '" + curso.getNombre() + "' ---");
        if (curso.getRutas().isEmpty()) {
            System.out.println("Este curso no tiene rutas asociadas.");
            return null;
        }
        for (int i = 0; i < curso.getRutas().size(); i++) {
            System.out.printf("%d. %s\n", i + 1, curso.getRutas().get(i).getNombre());
        }
        System.out.print("Seleccione una ruta por número (o 0 para volver): ");
        int indice = Integer.parseInt(scanner.nextLine());
        if (indice == 0) return null;
        return curso.getRutas().get(indice - 1);
    }

    private static NodoRuta seleccionarNodo(Ruta ruta) {
        System.out.println("\n--- Nodos en la ruta '" + ruta.getNombre() + "' ---");
        List<NodoRuta> nodos = ruta.getNodos();
        if (nodos.isEmpty()) {
            System.out.println("Esta ruta no tiene nodos.");
            return null;
        }
        for (int i = 0; i < nodos.size(); i++) {
            System.out.printf("%d. Nodo %d: %s\n", i + 1, nodos.get(i).getOrden(), nodos.get(i).getLeccion().getTitulo());
        }
        System.out.print("Seleccione un nodo por número (o 0 para volver): ");
        int indice = Integer.parseInt(scanner.nextLine());
        if (indice == 0) return null;
        return nodos.get(indice - 1);
    }

    private static void precargarDatosDeEjemplo() {
        // Crear un curso
        Curso cursoJava = new Curso("Java para Principiantes", "Aprende los fundamentos de Java.");
        repositorioCursos.put(cursoJava.getId(), cursoJava);

        // Crear una ruta con nodos y lecciones
        Ruta rutaBasica = new Ruta(UUID.randomUUID().toString(), "Fundamentos de Java", "El camino inicial.");
        Leccion l1 = new Leccion(UUID.randomUUID().toString(), "Variables y Tipos de Datos", "Aprende qué es una variable.", "ACTIVO");
        Leccion l2 = new Leccion(UUID.randomUUID().toString(), "Operadores", "Realiza operaciones matemáticas y lógicas.", "ACTIVO");
        NodoRuta n1 = new NodoRuta(1, l1);
        NodoRuta n2 = new NodoRuta(2, l2);
        rutaBasica.agregarNodo(n1);
        rutaBasica.agregarNodo(n2);
        repositorioRutas.add(rutaBasica);

        // Conectar la ruta al curso
        cursoJava.getRutas().add(rutaBasica);

        // Añadir un recurso de ejemplo al primer nodo
        RecursoAprendizaje videoIntro = new Video("Video de Introducción a Variables", "youtube.com/intro-vars", 300);
        n1.agregarMaterialDeApoyo(videoIntro);

        System.out.println(">>> ¡Datos de ejemplo cargados! <<<");
    }
}
 **/