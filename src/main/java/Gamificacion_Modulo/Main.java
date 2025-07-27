package Gamificacion_Modulo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Modulo_Usuario.Clases.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Logro> logrosDisponibles = new ArrayList<>();
    private static final List<Desafio> desafiosDisponibles = new ArrayList<>();
    private static final Ranking ranking = new Ranking();
    private static final List<ProgresoEstudiante> progresos = new ArrayList<>();
    private static Scanner scanner;
    
    // Campo para almacenar el Stage principal para navegación
    private static Stage primaryStage;
    
    // Referencias a controladores activos para notificaciones
    private static Object currentPerfilController = null;
    private static Object currentRankingController = null;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage; // Almacenar referencia del Stage principal
        
        try {
            // Prioridad 1: Cargar PerfilUsuario.fxml (Progreso - interfaz principal)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gamificacion_Modulo/fxml/PerfilUsuario.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 360, 720);
            stage.setTitle("Sistema de Gamificación - HelloCode");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            
            System.out.println(">>> Interfaz gráfica cargada correctamente: PerfilUsuario.fxml (Progreso)");
        } catch (Exception e) {
            System.err.println("Error al cargar PerfilUsuario.fxml, intentando con Desafios.fxml: " + e.getMessage());
            
            try {
                // Prioridad 2: Cargar Desafios.fxml como fallback
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gamificacion_Modulo/fxml/Desafios.fxml"));
                Parent root = loader.load();
                
                Scene scene = new Scene(root, 393, 852);
                stage.setTitle("Sistema de Gamificación - HelloCode");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.show();
                
                System.out.println(">>> Interfaz gráfica cargada correctamente: Desafios.fxml (Fallback)");
            } catch (Exception e2) {
                System.err.println("Error al cargar Desafios.fxml: " + e2.getMessage());
                e2.printStackTrace();
                
                // Fallback final: mostrar ventana simple si falla todo
                mostrarVentanaSimple(stage);
            }
        }
    }
    
    // Método estático para cambiar escenas desde los controladores
    public static void cambiarEscena(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 393, 852);
            
            // Encontrar el Stage activo en lugar de usar primaryStage
            Stage stageActivo = null;
            if (primaryStage != null && primaryStage.isShowing()) {
                stageActivo = primaryStage;
            } else {
                // Buscar cualquier Stage abierto de JavaFX
                for (javafx.stage.Window window : javafx.stage.Stage.getWindows()) {
                    if (window instanceof Stage && window.isShowing()) {
                        stageActivo = (Stage) window;
                        break;
                    }
                }
            }
            
            if (stageActivo != null) {
                stageActivo.setScene(scene);
                stageActivo.centerOnScreen();
                System.out.println(">>> Navegación exitosa a: " + fxmlPath);
            } else {
                System.err.println("No se encontró un Stage activo para cambiar la escena");
            }
            
        } catch (Exception e) {
            System.err.println("Error al cambiar a la escena: " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    // Método para obtener referencia del Stage principal
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    // Métodos para gestión de controladores activos
    public static void registrarPerfilController(Object controller) {
        currentPerfilController = controller;
        System.out.println(">>> Controlador de Perfil registrado para notificaciones");
    }
    
    public static void registrarRankingController(Object controller) {
        currentRankingController = controller;
        System.out.println(">>> Controlador de Ranking registrado para notificaciones");
    }
    
    public static void desregistrarPerfilController() {
        currentPerfilController = null;
        System.out.println(">>> Controlador de Perfil desregistrado");
    }
    
    public static void desregistrarRankingController() {
        currentRankingController = null;
        System.out.println(">>> Controlador de Ranking desregistrado");
    }
    
    // Método para notificar actualizaciones a las interfaces
    public static void notificarActualizacionInterface() {
        System.out.println(">>> Notificando actualización a interfaces activas...");
        
        // Usar Platform.runLater para ejecutar en el hilo de JavaFX
        try {
            if (primaryStage != null) {
                javafx.application.Platform.runLater(() -> {
                    try {
                        if (currentPerfilController != null) {
                            currentPerfilController.getClass().getMethod("actualizarDatosPerfil").invoke(currentPerfilController);
                            System.out.println(">>> Perfil actualizado en hilo FX");
                        }
                        if (currentRankingController != null) {
                            currentRankingController.getClass().getMethod("cargarRanking").invoke(currentRankingController);
                            System.out.println(">>> Ranking actualizado en hilo FX");
                        }
                    } catch (Exception e) {
                        System.err.println("Error al actualizar GUI: " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error al notificar actualización: " + e.getMessage());
        }
    }
    
    private void mostrarVentanaSimple(Stage stage) {
        StackPane layout = new StackPane();
        Button button = new Button("Sistema de Gamificación");
        button.setOnAction(actionEvent -> {
            System.out.println("Interfaz gráfica no disponible. Usa la consola.");
            mostrarMenuConsola();
        });
        
        layout.getChildren().add(button);
        Scene scene = new Scene(layout, 393, 852);
        stage.setScene(scene);
        stage.setTitle("Sistema de Gamificación - Modo Consola");
        stage.show();
    }
    
    private void mostrarMenuConsola() {
        System.out.println("\n=== MODO CONSOLA ACTIVADO ===");
        System.out.println("La interfaz gráfica no está disponible.");
        System.out.println("Cierra esta ventana y ejecuta el programa desde la consola.");
    }
    
    public static void mostrarGUI() {
        System.out.println(">>> Iniciando interfaz gráfica...");
        launch();
    }
    
    // Método para inicializar datos cuando se navega desde otro módulo
    public static void inicializarDesdeModuloExterno() {
        if (usuarios.isEmpty()) {
            System.out.println(">>> Inicializando módulo de gamificación desde navegación externa");
            inicializarDatos();
        } else {
            System.out.println(">>> Módulo de gamificación ya inicializado (" + usuarios.size() + " usuarios)");
            // Recargar usuarios para sincronización automática
            recargarUsuarios();
        }
        
        // Debug: Mostrar usuarios cargados
        System.out.println(">>> USUARIOS DISPONIBLES EN GAMIFICACIÓN:");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            System.out.println("   " + (i+1) + ". " + u.getNombre() + " (" + u.getUsername() + ") - " + u.getEmail());
        }
        
        // Debug: Mostrar progresos creados
        System.out.println(">>> PROGRESOS CREADOS: " + progresos.size());
        for (ProgresoEstudiante p : progresos) {
            System.out.println("   - " + p.getUsuario().getNombre() + ": " + p.getPuntosTotal() + " puntos");
        }
    }
    
    // Métodos para acceder a los datos desde los controladores
    public static List<Usuario> getUsuarios() {
        return usuarios;
    }
    
        public static List<Logro> getLogrosDisponibles() {
        return logrosDisponibles;
    }

    public static List<Desafio> getDesafiosDisponibles() {
        return desafiosDisponibles;
    }

    public static void agregarDesafio(Desafio desafio) {
        desafiosDisponibles.add(desafio);
        System.out.println(">>> Desafío agregado a la lista central: " + desafio.getNombre());
    }

    public static Ranking getRanking() {
        return ranking;
    }
    
    public static List<ProgresoEstudiante> getProgresos() {
        return progresos;
    }
    
    public static void main(String[] args) {
        // Inicializar datos del sistema
        inicializarDatos();
        
        System.out.println("*** BIENVENIDO AL SISTEMA DE GAMIFICACION ***");
        System.out.println("===============================================");
        System.out.println("🚀 Iniciando modo DUAL: Interfaz Gráfica + Consola");
        System.out.println("📱 La interfaz gráfica se abrirá automáticamente");
        System.out.println("💻 La consola estará disponible para interactuar");
        System.out.println("🔄 Los cambios en consola se reflejarán en la interfaz");
        System.out.println("===============================================\n");
        
        // Iniciar la interfaz gráfica en un hilo separado
        Thread guiThread = new Thread(() -> {
            try {
                System.out.println(">>> Iniciando interfaz gráfica...");
                launch();
            } catch (Exception e) {
                System.err.println("Error al iniciar interfaz gráfica: " + e.getMessage());
            }
        });
        
        guiThread.setDaemon(false); // Mantener la aplicación viva
        guiThread.start();
        
        // Esperar un momento para que la GUI se inicialice
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Ejecutar la consola en el hilo principal
        System.out.println(">>> Interfaz de consola lista para usar");
        System.out.println(">>> Utiliza la consola para simular actividades y ver cambios en tiempo real en la GUI\n");
        ejecutarModoConsola();
    }
    
    private static void inicializarDatos() {
        // Cargar usuarios del módulo de usuarios
        cargarUsuariosDesdeArchivo();
        
        // Crear automáticamente progresos para todos los usuarios cargados
        for (Usuario usuario : usuarios) {
            ProgresoEstudiante progreso = new ProgresoEstudiante(usuario);
            progresos.add(progreso);
            System.out.println(">>> Progreso creado para usuario: " + usuario.getNombre());
        }

        // Inicializar logros predeterminados
        inicializarLogros();
        
        // Inicializar desafíos de ejemplo
        inicializarDesafiosEjemplo();
    }
    
    // Método público para recargar usuarios (para sincronización)
    public static void recargarUsuarios() {
        try {
            List<Usuario> usuariosAnteriores = new ArrayList<>(usuarios);
            usuarios.clear();
            cargarUsuariosDesdeArchivo();
            
            // Crear progreso para usuarios nuevos
            for (Usuario usuario : usuarios) {
                boolean existeProgreso = progresos.stream()
                    .anyMatch(p -> p.getUsuario().getUsername().equals(usuario.getUsername()));
                if (!existeProgreso) {
                    ProgresoEstudiante nuevoProgreso = new ProgresoEstudiante(usuario);
                    progresos.add(nuevoProgreso);
                    System.out.println(">>> Progreso creado para nuevo usuario: " + usuario.getNombre());
                }
            }
            
            // Notificar a la GUI sobre la actualización
            notificarActualizacionInterface();
            
            System.out.println(">>> Usuarios recargados exitosamente. Total: " + usuarios.size());
            
        } catch (Exception e) {
            System.err.println(">>> Error al recargar usuarios: " + e.getMessage());
        }
    }

    // Método para cargar usuarios desde el archivo del módulo de usuarios
    private static void cargarUsuariosDesdeArchivo() {
        try {
            InputStream inputStream = Main.class.getResourceAsStream("/Modulo_Usuario/Usuarios/usuarios.txt");
            if (inputStream == null) {
                inputStream = Main.class.getClassLoader().getResourceAsStream("Modulo_Usuario/Usuarios/usuarios.txt");
            }
            
            if (inputStream == null) {
                System.err.println(">>> No se pudo encontrar el archivo de usuarios. Creando usuarios por defecto.");
                crearUsuariosDefecto();
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim();
                    if (!linea.isEmpty()) {
                        Usuario usuario = Usuario.fromString(linea);
                        if (usuario != null) {
                            // Completar información del usuario con datos por defecto si no tiene
                            if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
                                usuario.setNombre("Usuario " + usuario.getUsername());
                            }
                            if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
                                usuario.setEmail(usuario.getUsername() + "@email.com");
                            }
                            usuarios.add(usuario);
                            System.out.println(">>> Usuario cargado: " + usuario.getUsername() + " - " + usuario.getNombre());
                        }
                    }
                }
            }
            
            if (usuarios.isEmpty()) {
                System.out.println(">>> No se encontraron usuarios válidos. Creando usuarios por defecto.");
                crearUsuariosDefecto();
            }
            
        } catch (Exception e) {
            System.err.println(">>> Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
            crearUsuariosDefecto();
        }
    }
    
    // Método para crear usuarios por defecto si no se pueden cargar del archivo
    private static void crearUsuariosDefecto() {
        Usuario admin = new Usuario("admin", "1234", "Administrador", "admin@email.com");
        Usuario usuario1 = new Usuario("usuario1", "abc", "Usuario Demo", "usuario1@email.com");
        usuarios.add(admin);
        usuarios.add(usuario1);
        System.out.println(">>> Usuarios por defecto creados");
    }
    
    private static void ejecutarModoConsola() {
        scanner = new Scanner(System.in);
        System.out.println("\n*** MODO CONSOLA ACTIVADO ***");
        System.out.println("===============================================");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = obtenerOpcion();

            switch (opcion) {
                case 1:
                    mostrarUsuarios();
                    break;
                case 2:
                    crearDesafioSemanal();
                    break;
                case 3:
                    crearDesafioMensual();
                    break;
                case 4:
                    crearLogroPersonalizado();
                    break;
                case 5:
                    simularActividad();
                    break;
                case 6:
                    mostrarProgreso();
                    break;
                case 7:
                    mostrarRanking();
                    break;
                case 8:
                    mostrarDesafiosActivos();
                    break;
                case 9:
                    mostrarLogrosDisponibles();
                    break;
                case 10:
                    visualizarEstadisticas();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Gracias por usar el sistema de gamificacion!");
                    break;
                default:
                    System.out.println(">>> Opcion invalida. Intenta de nuevo.");
            }
        }

        scanner.close();
    }

    private static ProgresoEstudiante buscarProgresoPorUsername(String username) {
        for (ProgresoEstudiante p : progresos) {
            if (p.getUsuario().getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }
    
    // Método temporal para compatibilidad con métodos de consola
    private static ProgresoEstudiante buscarProgresoPorId(long id) {
        // Como ahora usamos usernames, buscar por índice en la lista
        if (id > 0 && id <= progresos.size()) {
            return progresos.get((int)id - 1);
        }
        return null;
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENU PRINCIPAL =====");
        System.out.println("1. Ver usuarios");
        System.out.println("2. Crear Desafio Semanal");
        System.out.println("3. Crear Desafio Mensual");
        System.out.println("4. Crear Logro Personalizado");
        System.out.println("5. Simular Actividad");
        System.out.println("6. Ver Mi Progreso");
        System.out.println("7. Ver Ranking");
        System.out.println("8. Ver Desafios Activos");
        System.out.println("9. Ver Logros Disponibles");
        System.out.println("10. Visualizar Estadísticas");
        System.out.println("0. Salir");
        System.out.print(">> Selecciona una opcion: ");
    }

    private static int obtenerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            return opcion;
        } catch (Exception e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    private static void mostrarUsuarios() {
        System.out.println("\n=== Lista de Usuarios ===");
        for (Usuario usuario : usuarios) {
            System.out.println("Username: " + usuario.getUsername() + " | Nombre: " + usuario.getNombre() + " | Correo: " + usuario.getEmail());
        }
    }

    private static void inicializarLogros() {
        logrosDisponibles.add(new Logro("Principiante", "Completar tu primer desafio", "desafios_completados:1", 100));
        logrosDisponibles.add(new Logro("Dedicado", "Completar 3 desafios", "desafios_completados:3", 250));
        logrosDisponibles.add(new Logro("Acumulador", "Obtener 500 puntos", "puntos_totales:500", 150));
        logrosDisponibles.add(new Logro("Coleccionista", "Obtener 5 logros", "logros_obtenidos:5", 300));
        System.out.println(">>> Logros predeterminados cargados: " + logrosDisponibles.size());
    }
    
    private static void inicializarDesafiosEjemplo() {
        // Solo crear desafíos de ejemplo si no hay ninguno
        if (desafiosDisponibles.isEmpty()) {
            // Crear algunos desafíos de ejemplo con los logros disponibles
            List<Logro> logrosBasicos = new ArrayList<>();
            if (!logrosDisponibles.isEmpty()) {
                logrosBasicos.add(logrosDisponibles.get(0)); // Logro principiante
            }
            
            DesafioSemanal desafioSemanalEjemplo1 = new DesafioSemanal(5, logrosBasicos);
            DesafioSemanal desafioSemanalEjemplo2 = new DesafioSemanal(10, logrosBasicos);
            DesafioMensual desafioMensualEjemplo = new DesafioMensual(25, logrosBasicos);
            
            desafiosDisponibles.add(desafioSemanalEjemplo1);
            desafiosDisponibles.add(desafioSemanalEjemplo2);
            desafiosDisponibles.add(desafioMensualEjemplo);
            
            System.out.println(">>> Desafíos de ejemplo creados: " + desafiosDisponibles.size());
        }
    }

    private static void crearDesafioSemanal() {
        System.out.print("\nSeleccione el ID del estudiante que va a tener desafio: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        System.out.println("\n=== CREAR DESAFIO SEMANAL ===");
        System.out.print(">> Numero de actividades para completar (1-20): ");
        int meta = Math.max(1, Math.min(20, obtenerOpcion()));

        // Seleccionar logros manualmente
        List<Logro> logrosDesafio = seleccionarLogrosManualmente();
        DesafioSemanal desafio = new DesafioSemanal(meta, logrosDesafio);

        desafio.activar();
        progreso.agregarDesafio(desafio);

        System.out.println(">>> Desafio semanal creado para " + progreso.getUsuario().getNombre() + " con meta de " + meta + " actividades");
        System.out.println("Logros asociados: ");
        for (Logro logro : logrosDesafio) {
            System.out.println("   * " + logro.getNombre() + " (+" + logro.getPuntos() + " pts)");
        }
    }

    private static void crearDesafioMensual() {
        System.out.print("\nSeleccione el ID del estudiante que va a tener desafio: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        System.out.println("\n=== CREAR DESAFIO MENSUAL ===");
        System.out.print(">> Numero de actividades para completar (10-100): ");
        int meta = Math.max(10, Math.min(100, obtenerOpcion()));

        List<Logro> logrosDesafio = seleccionarLogrosManualmente();
        DesafioMensual desafio = new DesafioMensual(meta, logrosDesafio);

        desafio.activar();
        progreso.agregarDesafio(desafio);

        System.out.println(">>> Desafio mensual creado para " + progreso.getUsuario().getNombre() + " con meta de " + meta + " actividades");
        System.out.println("Logros asociados: ");
        for (Logro logro : logrosDesafio) {
            System.out.println("   * " + logro.getNombre() + " (+" + logro.getPuntos() + " pts)");
        }
    }

    private static void crearLogroPersonalizado() {
        System.out.print("\nSeleccione el ID del estudiante que va tener logro personalizado: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        System.out.println("\n=== CREAR LOGRO PERSONALIZADO ===");
        System.out.print(">> Nombre del logro: ");
        String nombre = scanner.nextLine();
        System.out.print(">> Descripcion: ");
        String descripcion = scanner.nextLine();

        System.out.println("\nTipos de criterios disponibles:");
        System.out.println("1. Completar cierta cantidad de desafios");
        System.out.println("2. Obtener cierta cantidad de puntos");
        System.out.println("3. Desbloquear cierta cantidad de logros");
        System.out.print(">> Selecciona tipo de criterio (1-3): ");
        int tipoCriterio = obtenerOpcion();

        String criterio = "";
        switch (tipoCriterio) {
            case 1:
                System.out.print(">> Cuantos desafios debe completar? ");
                int desafios = obtenerOpcion();
                criterio = "desafios_completados:" + desafios;
                break;
            case 2:
                System.out.print(">> Cuantos puntos debe obtener? ");
                int puntosRequeridos = obtenerOpcion();
                criterio = "puntos_totales:" + puntosRequeridos;
                break;
            case 3:
                System.out.print(">> Cuantos logros debe desbloquear? ");
                int logros = obtenerOpcion();
                criterio = "logros_obtenidos:" + logros;
                break;
            default:
                System.out.println(">>> Opcion invalida, usando criterio por defecto");
                criterio = "desafios_completados:1";
        }

        System.out.print(">> Puntos de recompensa (50-500): ");
        int puntos = Math.max(50, Math.min(500, obtenerOpcion()));

        Logro nuevoLogro = new Logro(nombre, descripcion, criterio, puntos);
        logrosDisponibles.add(nuevoLogro);

        System.out.println(">>> Logro personalizado creado!");
        System.out.println("* " + nombre + " - " + descripcion);
        System.out.println("  Criterio: " + criterio + " | Recompensa: +" + puntos + " pts");
    }

    private static void simularActividad() {
        System.out.print("\nSeleccione el ID del estudiante que va a realizar una actividad: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        List<Desafio> desafiosActivos = progreso.getDesafiosActivos();
        if (desafiosActivos.isEmpty()) {
            System.out.println(">>> No hay desafios activos para " + progreso.getUsuario().getNombre() + ". Crea uno primero.");
            return;
        }

        System.out.println("\n=== SIMULAR ACTIVIDAD ===");
        System.out.println("Desafios activos para " + progreso.getUsuario().getNombre() + ":");
        for (int i = 0; i < desafiosActivos.size(); i++) {
            Desafio d = desafiosActivos.get(i);
            System.out.println((i + 1) + ". " + d.getNombre() + " - " + d.getDescripcion());
        }

        System.out.print(">> Selecciona desafio (1-" + desafiosActivos.size() + "): ");
        int indice = obtenerOpcion() - 1;

        if (indice >= 0 && indice < desafiosActivos.size()) {
            Desafio desafio = desafiosActivos.get(indice);
            System.out.print(">> Numero de actividades a completar (1-10): ");
            int actividades = Math.max(1, Math.min(10, obtenerOpcion()));

            // Simular actividades
            if (desafio instanceof DesafioSemanal) {
                ((DesafioSemanal) desafio).actualizarActividades(actividades);
            } else if (desafio instanceof DesafioMensual) {
                ((DesafioMensual) desafio).actualizarActividades(actividades);
            }

            progreso.actualizarProgreso(desafio);
            ranking.actualizarRanking(progreso);

            // Notificar a las interfaces gráficas para que se actualicen
            notificarActualizacionInterface();

            if (desafio.estaCompletado() && !desafio.getEstaActivo()) {
                desafiosActivos.remove(desafio);
                System.out.println("*** Desafio completado y removido de la lista activa!");
            }
        } else {
            System.out.println(">>> Seleccion invalida.");
        }
    }

    private static void mostrarProgreso() {
        System.out.print("\nSeleccione el ID del estudiante que quiere visualizar su progreso: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        System.out.println("\n=== MI PROGRESO ===");
        System.out.println("Usuario: " + progreso.getUsuario().getNombre());
        System.out.println("Puntos Totales: " + progreso.getPuntosTotal());
        System.out.println("Desafios Completados: " + progreso.getDesafiosCompletados());
        System.out.println("Logros Desbloqueados: " + progreso.getLogros().size());

        System.out.println("\n=== LOGROS OBTENIDOS ===");
        if (progreso.getLogros().isEmpty()) {
            System.out.println("   (Ningun logro desbloqueado aun)");
        } else {
            for (Logro logro : progreso.getLogros()) {
                System.out.println("   * " + logro.getNombre() + " - " + logro.getDescripcion() + " (+" + logro.getPuntos() + " pts)");
            }
        }

        System.out.println("\n=== PROGRESO EN DESAFIOS ===");
        if (progreso.getListaDesafios().isEmpty()) {
            System.out.println("   (No hay progreso en desafios)");
        } else {
            for (Map.Entry<String, Double> entry : progreso.getListaDesafios().entrySet()) {
                System.out.println("   > Desafio ID " + entry.getKey() + ": " + String.format("%.1f", entry.getValue()) + "%");
            }
        }
    }

    private static void mostrarRanking() {
        System.out.println("\n=== RANKING GENERAL ===");
        List<ProgresoEstudiante> top = ranking.obtenerRankingGeneral();

        if (top.isEmpty()) {
            System.out.println("   (No hay datos de ranking)");
        } else {
            for (int i = 0; i < top.size(); i++) {
                ProgresoEstudiante p = top.get(i);
                String medalla = (i == 0) ? "[1]" : (i == 1) ? "[2]" : (i == 2) ? "[3]" : "[" + (i+1) + "]";
                System.out.println((i + 1) + ". " + medalla + " " +
                        p.getUsuario().getNombre() + " - " +
                        p.getPuntosTotal() + " puntos (" +
                        p.getLogros().size() + " logros)");
            }
        }

        Map<String, Object> stats = ranking.generarEstadisticasRanking();
        System.out.println("\n=== ESTADISTICAS ===");
        System.out.println("Total usuarios: " + stats.get("total_estudiantes"));
        if (stats.containsKey("lider")) {
            System.out.println("Lider actual: " + stats.get("lider") + " (" + stats.get("puntos_maximo") + " puntos)");
        }
    }

    private static void mostrarDesafiosActivos() {
        System.out.println("\n=== DESAFIOS ACTIVOS ===");
        System.out.print("\nSeleccione el ID del estudiante que quiere ver sus desafios activos: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        List<Desafio> desafiosActivos = progreso.getDesafiosActivos();
        if (desafiosActivos.isEmpty()) {
            System.out.println("   (No hay desafios activos para " + progreso.getUsuario().getNombre() + ")");
        } else {
            for (Desafio desafio : desafiosActivos) {
                System.out.println("* " + desafio.getNombre() + " - " + desafio.getDescripcion());
                System.out.println("   Estado: " + (desafio.getEstaActivo() ? "Activo" : "Inactivo"));
                System.out.println("   Criterios: " + desafio.definirCriterios());

                if (desafio instanceof DesafioSemanal) {
                    DesafioSemanal ds = (DesafioSemanal) desafio;
                    System.out.println("   Progreso: " + ds.getActividadesCompletadas() + "/" + ds.getMetaSemanal() +
                            " (" + String.format("%.1f", ds.getProgreso()) + "%)");
                } else if (desafio instanceof DesafioMensual) {
                    DesafioMensual dm = (DesafioMensual) desafio;
                    System.out.println("   Progreso: " + dm.getActividadesCompletadas() + "/" + dm.getObjetivoMensual() +
                            " (" + String.format("%.1f", dm.getProgreso()) + "%)");
                }
                System.out.println();
            }
        }
    }

    private static List<Logro> seleccionarLogrosManualmente() {
        List<Logro> seleccionados = new ArrayList<>();

        if (logrosDisponibles.isEmpty()) {
            System.out.println(">>> No hay logros disponibles");
            return seleccionados;
        }

        System.out.println("\n=== SELECCIONAR LOGROS PARA EL DESAFIO ===");
        System.out.println("Logros disponibles:");
        for (int i = 0; i < logrosDisponibles.size(); i++) {
            Logro logro = logrosDisponibles.get(i);
            System.out.println((i + 1) + ". " + logro.getNombre() + " - " + logro.getDescripcion() + " (+" + logro.getPuntos() + " pts)");
        }

        System.out.print(">> Cuantos logros quieres agregar? (0-" + logrosDisponibles.size() + "): ");
        int cantidad = Math.max(0, Math.min(logrosDisponibles.size(), obtenerOpcion()));

        for (int i = 0; i < cantidad; i++) {
            System.out.print(">> Selecciona logro " + (i + 1) + " (1-" + logrosDisponibles.size() + "): ");
            int indice = obtenerOpcion() - 1;

            if (indice >= 0 && indice < logrosDisponibles.size()) {
                Logro logro = logrosDisponibles.get(indice);
                if (!seleccionados.contains(logro)) {
                    seleccionados.add(logro);
                    System.out.println("   + Agregado: " + logro.getNombre());
                } else {
                    System.out.println("   ! Logro ya seleccionado");
                    i--;
                }
            } else {
                System.out.println("   ! Seleccion invalida");
                i--;
            }
        }

        return seleccionados;
    }

    private static void visualizarEstadisticas() {
        System.out.print("\nSeleccione el ID del estudiante para visualizar estadísticas: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }

        System.out.println("\n=== VISUALIZAR ESTADÍSTICAS ===");
        System.out.println("1. Estadísticas de logros");
        System.out.println("2. Estadísticas de progreso");
        System.out.print(">> Selecciona una opción (1-2): ");
        int opcion = obtenerOpcion();

        VisualizadorEstadistico visualizador;
        switch (opcion) {
            case 1:
                visualizador = new VisualizadorEstadistico(new VisualizacionLogros(logrosDisponibles));
                Estadistica estadisticaLogros = Estadistica.generarEstadistica(
                        "logros_obtenidos", (double) progreso.getLogros().size(), progreso.getUsuario());
                visualizador.visualizar(estadisticaLogros);
                ((VisualizacionLogros) visualizador.getEstrategia()).mostrarLogrosDesbloqueados();
                break;
            case 2:
                visualizador = new VisualizadorEstadistico(new VisualizacionProgreso(progreso.getListaDesafios()));
                Estadistica estadisticaProgreso = Estadistica.generarEstadistica(
                        "desafios_en_progreso", (double) progreso.getListaDesafios().size(), progreso.getUsuario());
                visualizador.visualizar(estadisticaProgreso);
                ((VisualizacionProgreso) visualizador.getEstrategia()).mostrarBarrasProgreso();
                break;
            default:
                System.out.println(">>> Opción inválida.");
        }
    }

    private static void mostrarLogrosDisponibles() {
        System.out.print("\nSeleccione el ID del estudiante que quiere ver sus logros: ");
        long idEstudiante = scanner.nextLong();
        scanner.nextLine();

        ProgresoEstudiante progreso = buscarProgresoPorId(idEstudiante);
        if (progreso == null) {
            System.out.println(">>> Estudiante no encontrado.");
            return;
        }
        System.out.println("\n=== LOGROS DISPONIBLES ===");
        if (logrosDisponibles.isEmpty()) {
            System.out.println("   (No hay logros disponibles)");
        } else {
            for (int i = 0; i < logrosDisponibles.size(); i++) {
                Logro logro = logrosDisponibles.get(i);
                System.out.println((i + 1) + ". " + logro.getNombre());
                System.out.println("   Descripcion: " + logro.getDescripcion());
                System.out.println("   Puntos: +" + logro.getPuntos());
                System.out.println("   Criterio: " + logro.getCriteriosDesbloqueo());
                System.out.println();
            }
        }
    }
}