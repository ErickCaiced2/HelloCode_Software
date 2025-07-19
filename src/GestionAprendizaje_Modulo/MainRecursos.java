package GestionAprendizaje_Modulo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;

public class MainRecursos {

    // Usamos 'static' para que el Scanner y la lista sean accesibles desde los métodos auxiliares.
    private static final Scanner scanner = new Scanner(System.in);

    // Lista donde se almacenan los recursos creados por el profesor durante la ejecución.
    private static final List<RecursoAprendizaje> recursosCreados = new ArrayList<>();

    public static void main(String[] args) {
        // Bienvenida inicial al usuario.
        System.out.println("=================================================");
        System.out.println("== BIENVENIDO AL SISTEMA DE CREACIÓN DE RECURSOS ==");
        System.out.println("=================================================");

        // Etiqueta opcional para salir del bucle desde dentro del switch.
        buclePrincipal:
        while (true) {
            // Mostramos las opciones disponibles al usuario.
            mostrarMenu();
            String opcion = scanner.nextLine();

            // Evaluamos la opción elegida mediante un switch.
            switch (opcion) {
                case "1":
                    crearVideo(); // Crear un recurso tipo video
                    break;
                case "2":
                    crearArticulo(); // Crear un recurso tipo artículo
                    break;
                case "3":
                    crearDocumentoPDF(); // Crear un recurso tipo PDF
                    break;
                case "4":
                    mostrarRecursosCreados(); // Mostrar todos los recursos registrados
                    break;
                case "5":
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break buclePrincipal; // Salida del bucle y del programa
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }

            // Pausa para que el usuario pueda leer la salida antes de mostrar el menú nuevamente.
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
        }

        // Cerramos el Scanner al final del programa.
        scanner.close();
    }

    // Muestra el menú de opciones en consola.
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ DEL PROFESOR ---");
        System.out.println("¿Qué tipo de recurso desea crear?");
        System.out.println("1. Video");
        System.out.println("2. Artículo web");
        System.out.println("3. Documento PDF");
        System.out.println("-------------------------");
        System.out.println("4. Ver todos los recursos creados");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    // Crea un nuevo recurso de tipo Video.
    private static void crearVideo() {
        System.out.println("\n--- Creando un nuevo Video ---");
        try {
            // Pedimos los datos del video al usuario.
            System.out.print("Ingrese el título del video: ");
            String titulo = scanner.nextLine();

            System.out.print("Ingrese la URL del video (ej. https://youtube.com/watch?v=...): ");
            String url = scanner.nextLine();

            System.out.print("Ingrese la duración en segundos: ");
            int duracion = Integer.parseInt(scanner.nextLine());

            // Validamos que no estén vacíos los campos requeridos.
            if (titulo.trim().isEmpty() || url.trim().isEmpty()) {
                System.out.println("Error: El título y la URL no pueden estar vacíos.");
                return;
            }

            // Creamos el video y lo agregamos a la lista.
            Video nuevoVideo = new Video(titulo, url, duracion);
            recursosCreados.add(nuevoVideo);

            System.out.println("\n¡Video creado con éxito!");
            System.out.println("-> " + nuevoVideo.obtenerDetalle());

        } catch (NumberFormatException e) {
            // En caso de error de formato numérico para la duración.
            System.out.println("Error: La duración debe ser un número entero. Creación cancelada.");
        }
    }

    // Crea un nuevo recurso de tipo Artículo web.
    private static void crearArticulo() {
        System.out.println("\n--- Creando un nuevo Artículo ---");

        // Pedimos los datos del artículo al usuario.
        System.out.print("Ingrese el título del artículo: ");
        String titulo = scanner.nextLine();

        System.out.print("Ingrese la URL del artículo: ");
        String url = scanner.nextLine();

        // Validamos que no estén vacíos los campos.
        if (titulo.trim().isEmpty() || url.trim().isEmpty()) {
            System.out.println("Error: El título y la URL no pueden estar vacíos.");
            return;
        }

        // Creamos el artículo y lo agregamos a la lista.
        Articulo nuevoArticulo = new Articulo(titulo, url);
        recursosCreados.add(nuevoArticulo);

        System.out.println("\n¡Artículo creado con éxito!");
        System.out.println("-> " + nuevoArticulo.obtenerDetalle());
    }

    // Crea un nuevo recurso de tipo Documento PDF.
    private static void crearDocumentoPDF() {
        System.out.println("\n--- Creando un nuevo Documento PDF ---");
        try {
            // Solicitamos los datos del documento PDF.
            System.out.print("Ingrese el título del PDF: ");
            String titulo = scanner.nextLine();

            System.out.print("Ingrese la URL de descarga del PDF: ");
            String url = scanner.nextLine();

            System.out.print("Ingrese el número de páginas: ");
            int paginas = Integer.parseInt(scanner.nextLine());

            // Validación de campos vacíos.
            if (titulo.trim().isEmpty() || url.trim().isEmpty()) {
                System.out.println("Error: El título y la URL no pueden estar vacíos.");
                return;
            }

            // Creamos el documento y lo agregamos a la lista.
            DocumentoPDF nuevoPdf = new DocumentoPDF(titulo, url, paginas);
            recursosCreados.add(nuevoPdf);

            System.out.println("\n¡Documento PDF creado con éxito!");
            System.out.println("-> " + nuevoPdf.obtenerDetalle());

        } catch (NumberFormatException e) {
            // Captura de errores si el número de páginas no es válido.
            System.out.println("Error: El número de páginas debe ser un número entero. Creación cancelada.");
        }
    }

    // Muestra todos los recursos creados hasta el momento.
    private static void mostrarRecursosCreados() {
        System.out.println("\n--- LISTA DE RECURSOS CREADOS ---");
        if (recursosCreados.isEmpty()) {
            System.out.println("Todavía no se ha creado ningún recurso.");
        } else {
            // Recorremos la lista de recursos e imprimimos su información.
            for (int i = 0; i < recursosCreados.size(); i++) {
                RecursoAprendizaje recurso = recursosCreados.get(i);
                System.out.printf("%d. [%s] %s%n",
                        i + 1,
                        recurso.getClass().getSimpleName(), // Tipo de recurso
                        recurso.getTitulo()
                );
                System.out.println("   Detalle: " + recurso.obtenerDetalle());
            }
        }
    }
}

