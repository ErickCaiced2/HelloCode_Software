package GestionAprendizaje_Modulo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;

public class MainEnriquecedorDeContenido {

    private static final Map<String, Curso> repositorioCursos = new HashMap<>();
   // private static final RepositorioExterno repoExterno = RepositorioExterno.getInstancia();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("==  SISTEMA DE GESTIÓN DE CONTENIDO (ROL TUYO) ==");
        System.out.println("=============================================");

        buclePrincipal:
        while (true) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Crear un nuevo Curso (tu entidad principal)");
            System.out.println("2. Gestionar un Curso existente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": crearCurso(); break;
                case "2": gestionarCurso(); break;
                case "3": System.out.println("Saliendo..."); break buclePrincipal;
                default: System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    private static void crearCurso() {
        System.out.println("\n--- Creando Nuevo Curso ---");
        System.out.print("Nombre del curso: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción del curso: ");
        String descripcion = scanner.nextLine();

        Curso nuevoCurso = new Curso(nombre, descripcion);

        System.out.println("\nAsociando Módulos al Curso...");
      //  asociarEntidades(repoExterno.getTodosLosModulos(), nuevoCurso.getModulos(), "Módulo");

        System.out.println("\nAsociando Rutas de Aprendizaje al Curso...");
       // asociarEntidades(repoExterno.getTodasLasRutas(), nuevoCurso.getRutas(), "Ruta"); // Usa tu clase Ruta

        repositorioCursos.put(nuevoCurso.getId(), nuevoCurso);
        System.out.println("\n¡Curso '" + nombre + "' creado y configurado con éxito!");
    }

    private static void gestionarCurso() {
        Curso curso = seleccionarCurso();
        if (curso == null) return;

        System.out.println("\n--- Gestionando Curso: " + curso.getNombre() + " ---");
        System.out.println("La principal tarea de este módulo es ENRIQUECER los nodos de las rutas con material de apoyo.");

        Ruta ruta = seleccionarRuta(curso); // <-- Corregido para usar tu clase Ruta
        if (ruta == null) return;

        NodoRuta nodo = seleccionarNodo(ruta);
        if (nodo == null) return;

        agregarMaterialANodo(nodo);
    }

    private static void agregarMaterialANodo(NodoRuta nodo) {
        System.out.println("\n--- Agregando Material a Nodo: '" + nodo.getLeccion().getTitulo() + "' ---");
        // Ahora esta línea funciona gracias a la corrección en NodoRuta.java
        System.out.println("Material actual: " + (nodo.getMaterialDeApoyo().isEmpty() ? "Ninguno" : nodo.getMaterialDeApoyo()));
        System.out.println("¿Qué tipo de material desea agregar? (1: Video, 2: Artículo, 3: PDF, 4: Cancelar)");
        String tipo = scanner.nextLine();
        RecursoAprendizaje recurso;

        try {
            switch (tipo) {
                case "1":
                    System.out.print("Título del video: "); String tV = scanner.nextLine();
                    System.out.print("URL del video: "); String uV = scanner.nextLine();
                    System.out.print("Duración en segundos: "); int dV = Integer.parseInt(scanner.nextLine());
                    recurso = new Video(tV, uV, dV);
                    break;
                case "2":
                    System.out.print("Título del artículo: "); String tA = scanner.nextLine();
                    System.out.print("URL del artículo: "); String uA = scanner.nextLine();
                    recurso = new Articulo(tA, uA);
                    break;
                case "3":
                    System.out.print("Título del PDF: "); String tP = scanner.nextLine();
                    System.out.print("URL del PDF: "); String uP = scanner.nextLine();
                    System.out.print("Número de páginas: "); int nP = Integer.parseInt(scanner.nextLine());
                    recurso = new DocumentoPDF(tP, uP, nP);
                    break;
                case "4":
                    System.out.println("Operación cancelada.");
                    return;
                default:
                    System.out.println("Tipo no válido.");
                    return;
            }
            // Y esta línea también funciona ahora
            nodo.agregarMaterialDeApoyo(recurso);
            System.out.println("¡Material agregado con éxito al nodo!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Entrada numérica no válida. Operación cancelada.");
        }
    }

    private static <T> void asociarEntidades(List<T> disponibles, List<T> destino, String nombreEntidad) {
        while (true) {
            System.out.println("Disponibles para asociar:");
            if (disponibles.isEmpty()) {
                System.out.println("No hay " + nombreEntidad + "s disponibles en el sistema externo.");
                break;
            }
            for (int i = 0; i < disponibles.size(); i++) {
                System.out.println((i + 1) + ". " + disponibles.get(i).toString());
            }
            System.out.print("Seleccione un " + nombreEntidad + " por número para agregar (o '0' para terminar): ");
            int indice;
            try {
                indice = Integer.parseInt(scanner.nextLine());
                if (indice == 0) break;
                if (indice < 1 || indice > disponibles.size()) {
                    System.out.println("Selección fuera de rango.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número.");
                continue;
            }

            T entidadSeleccionada = disponibles.get(indice - 1);
            if (!destino.contains(entidadSeleccionada)) {
                destino.add(entidadSeleccionada);
                System.out.println(nombreEntidad + " '" + entidadSeleccionada + "' asociado.");
            } else {
                System.out.println("Este " + nombreEntidad + " ya fue asociado.");
            }
        }
    }

    private static Curso seleccionarCurso() {
        System.out.println("\n--- Cursos Creados por ti ---");
        if (repositorioCursos.isEmpty()) {
            System.out.println("No hay cursos creados. Por favor, cree uno primero.");
            return null;
        }
        List<Curso> cursos = new ArrayList<>(repositorioCursos.values());
        for (int i = 0; i < cursos.size(); i++) {
            System.out.println((i + 1) + ". " + cursos.get(i).getNombre());
        }
        System.out.print("Seleccione un curso por número: ");
        int indice = Integer.parseInt(scanner.nextLine()) - 1;
        return cursos.get(indice);
    }

    // Corregido para usar tu clase Ruta
    private static Ruta seleccionarRuta(Curso curso) {
        System.out.println("\n--- Rutas Asociadas al Curso '" + curso.getNombre() + "' ---");
        if (curso.getRutas().isEmpty()) {
            System.out.println("Este curso no tiene rutas asociadas.");
            return null;
        }
        for (int i = 0; i < curso.getRutas().size(); i++) {
            System.out.println((i + 1) + ". " + curso.getRutas().get(i)); // Usamos tu toString()
        }
        System.out.print("Seleccione una ruta para gestionar: ");
        int indice = Integer.parseInt(scanner.nextLine()) - 1;
        return curso.getRutas().get(indice);
    }

    private static NodoRuta seleccionarNodo(Ruta ruta) {
        System.out.println("\n--- Nodos en la Ruta '" + ruta + "' ---");
        List<NodoRuta> nodos = new ArrayList<>(ruta.getNodos());
        nodos.sort(Comparator.comparingInt(NodoRuta::getOrden));

        for (int i = 0; i < nodos.size(); i++) {
            NodoRuta n = nodos.get(i);
            // Ahora esta línea funciona perfectamente
            System.out.printf("%d. Nodo %d: %s (Material: %d)\n",
                    i + 1, n.getOrden(), n.getLeccion().getTitulo(), n.getMaterialDeApoyo().size());
        }
        System.out.print("Seleccione un nodo para enriquecer: ");
        int indice = Integer.parseInt(scanner.nextLine()) - 1;
        return nodos.get(indice);
    }
}