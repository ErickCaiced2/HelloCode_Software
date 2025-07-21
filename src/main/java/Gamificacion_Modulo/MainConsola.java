package Gamificacion_Modulo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Modulo_Usuario.Clases.Usuario;

public class MainConsola {
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Logro> logrosDisponibles = new ArrayList<>();
    private static final Ranking ranking = new Ranking();
    private static final List<ProgresoEstudiante> progresos = new ArrayList<>();
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GAMIFICACIÓN - MODO CONSOLA ===");
        System.out.println("Nota: Para usar la interfaz gráfica, necesitas configurar JavaFX");
        System.out.println();
        
        scanner = new Scanner(System.in);
        inicializarDatos();
        mostrarMenu();
    }
    
    private static void inicializarDatos() {
        // Crear usuarios
        usuarios.add(new Usuario("ana_garcia", "123", "Ana García", "ana.garcia@email.com"));
        usuarios.add(new Usuario("luis_martinez", "123", "Luis Martínez", "luis.martinez@email.com"));
        usuarios.add(new Usuario("maria_lopez", "123", "María López", "maria.lopez@email.com"));
        usuarios.add(new Usuario("carlos_rodriguez", "123", "Carlos Rodríguez", "carlos.rodriguez@email.com"));
        usuarios.add(new Usuario("elena_sanchez", "123", "Elena Sánchez", "elena.sanchez@email.com"));

        // Crear logros disponibles
        logrosDisponibles.add(new Logro("Primer Paso", "Completa tu primer desafío", "desafios_completados:1", 100));
        logrosDisponibles.add(new Logro("Constante", "Completa 5 desafíos seguidos", "desafios_completados:5", 250));
        logrosDisponibles.add(new Logro("Experto", "Alcanza 1000 puntos", "puntos_totales:1000", 500));
        logrosDisponibles.add(new Logro("Maratonista", "Completa 10 desafíos en una semana", "desafios_completados:10", 300));
        logrosDisponibles.add(new Logro("Explorador", "Prueba todos los tipos de desafíos", "logros_obtenidos:3", 200));

        // Crear progresos para cada usuario
        for (Usuario usuario : usuarios) {
            ProgresoEstudiante progreso = new ProgresoEstudiante(usuario);
            
            // Agregar algunos desafíos
            progreso.agregarDesafio(new DesafioSemanal(3, List.of(logrosDisponibles.get(0))));
            progreso.agregarDesafio(new DesafioMensual(10, List.of(logrosDisponibles.get(2))));
            
            // Simular algunos puntos
            progreso.sumarPuntos((int)(Math.random() * 800) + 200);
            
            progresos.add(progreso);
        }

        // Actualizar ranking para cada estudiante
        for (ProgresoEstudiante progreso : progresos) {
            ranking.actualizarRanking(progreso);
        }
        
        System.out.println(">>> Datos inicializados correctamente");
        System.out.println("- Usuarios: " + usuarios.size());
        System.out.println("- Logros disponibles: " + logrosDisponibles.size());
        System.out.println("- Progresos creados: " + progresos.size());
    }
    
    private static void mostrarMenu() {
        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ver Estudiantes");
            System.out.println("2. Ver Ranking");
            System.out.println("3. Ver Logros");
            System.out.println("4. Ver Desafíos");
            System.out.println("5. Gestionar Progreso");
            System.out.println("6. Salir");
            System.out.print("Selecciona una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    mostrarEstudiantes();
                    break;
                case 2:
                    mostrarRanking();
                    break;
                case 3:
                    mostrarLogros();
                    break;
                case 4:
                    mostrarDesafios();
                    break;
                case 5:
                    gestionarProgreso();
                    break;
                case 6:
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
    }
    
    private static void mostrarEstudiantes() {
        System.out.println("\n=== LISTA DE USUARIOS ===");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            ProgresoEstudiante progreso = progresos.get(i);
            System.out.println((i + 1) + ". " + usuario.getNombre() + 
                             " (" + usuario.getUsername() + ") - " + 
                             progreso.getPuntosTotal() + " puntos");
        }
    }
    
    private static void mostrarRanking() {
        System.out.println("\n=== RANKING DE ESTUDIANTES ===");
        List<ProgresoEstudiante> rankingActual = ranking.obtenerRankingGeneral();
        
        for (int i = 0; i < rankingActual.size(); i++) {
            ProgresoEstudiante progreso = rankingActual.get(i);
            String emoji = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "  ";
            System.out.println(emoji + " " + (i + 1) + ". " + 
                             progreso.getUsuario().getNombre() + 
                             " - " + progreso.getPuntosTotal() + " puntos");
        }
    }
    
    private static void mostrarLogros() {
        System.out.println("\n=== LOGROS DISPONIBLES ===");
        for (int i = 0; i < logrosDisponibles.size(); i++) {
            Logro logro = logrosDisponibles.get(i);
            System.out.println((i + 1) + ". " + logro.getNombre() + 
                             " - " + logro.getDescripcion() + 
                             " (Puntos: " + logro.getPuntos() + ")");
        }
    }
    
    private static void mostrarDesafios() {
        System.out.println("\n=== DESAFÍOS DISPONIBLES ===");
        System.out.println("Mostrando desafíos del primer estudiante:");
        
        if (!progresos.isEmpty()) {
            ProgresoEstudiante progreso = progresos.get(0);
            List<Desafio> desafios = progreso.getDesafiosActivos();
            
            for (int i = 0; i < desafios.size(); i++) {
                Desafio desafio = desafios.get(i);
                String tipo = desafio instanceof DesafioSemanal ? "Semanal" : "Mensual";
                String estado = desafio.estaCompletado() ? "Completado" : "Activo";
                System.out.println((i + 1) + ". " + desafio.getNombre() + 
                                 " (" + tipo + ") - " + estado);
                System.out.println("   " + desafio.getDescripcion());
            }
        }
    }
    
    private static void gestionarProgreso() {
        System.out.println("\n=== GESTIÓN DE PROGRESO ===");
        System.out.println("Selecciona un usuario:");
        
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getNombre());
        }
        
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        if (opcion > 0 && opcion <= usuarios.size()) {
            ProgresoEstudiante progreso = progresos.get(opcion - 1);
            mostrarDetalleProgreso(progreso);
        } else {
            System.out.println("Opción inválida.");
        }
    }
    
    private static void mostrarDetalleProgreso(ProgresoEstudiante progreso) {
        System.out.println("\n=== PROGRESO DE " + progreso.getUsuario().getNombre().toUpperCase() + " ===");
        System.out.println("Puntos totales: " + progreso.getPuntosTotal());
        System.out.println("Logros obtenidos: " + progreso.getLogros().size());
        System.out.println("Desafíos activos: " + progreso.getDesafiosActivos().size());
        
        System.out.println("\nLogros desbloqueados:");
        for (Logro logro : progreso.getLogros()) {
            System.out.println("- " + logro.getNombre() + ": " + logro.getDescripcion());
        }
        
        System.out.println("\nDesafíos:");
        for (Desafio desafio : progreso.getDesafiosActivos()) {
            String estado = desafio.estaCompletado() ? "✅ Completado" : "⏳ En progreso";
            System.out.println("- " + desafio.getNombre() + " " + estado);
        }
        
        System.out.println("\n¿Deseas agregar puntos? (s/n)");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("s")) {
            System.out.print("Cantidad de puntos a agregar: ");
            int puntos = scanner.nextInt();
            scanner.nextLine();
            progreso.sumarPuntos(puntos);
            ranking.actualizarRanking(progreso);
            System.out.println("¡Puntos agregados! Nuevo total: " + progreso.getPuntosTotal());
        }
    }
    
    // Métodos para acceder a los datos desde los controladores (cuando JavaFX funcione)
    public static List<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public static List<Logro> getLogrosDisponibles() {
        return logrosDisponibles;
    }
    
    public static Ranking getRanking() {
        return ranking;
    }
    
    public static List<ProgresoEstudiante> getProgresos() {
        return progresos;
    }
}
