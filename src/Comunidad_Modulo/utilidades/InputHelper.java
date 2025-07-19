package utilidades;

import enums.*;
import java.util.Scanner;

public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);
    
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim();
    }
    
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    public static boolean leerBooleano(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            } else if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            } else {
                System.out.println("Por favor, responda 's' para sí o 'n' para no.");
            }
        }
    }
    
    public static NivelJava leerNivelJava() {
        System.out.println("Seleccione el nivel de Java:");
        NivelJava[] niveles = NivelJava.values();
        for (int i = 0; i < niveles.length; i++) {
            System.out.println((i + 1) + ". " + niveles[i].getDescripcion());
        }
        
        while (true) {
            try {
                int opcion = leerEntero("Opción") - 1;
                if (opcion >= 0 && opcion < niveles.length) {
                    return niveles[opcion];
                } else {
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                }
            } catch (Exception e) {
                System.out.println("Error al leer la opción.");
            }
        }
    }
    
    public static TipoTema leerTipoTema() {
        System.out.println("Seleccione el tipo de tema:");
        TipoTema[] temas = TipoTema.values();
        for (int i = 0; i < temas.length; i++) {
            System.out.println((i + 1) + ". " + temas[i].getDescripcion());
        }
        
        while (true) {
            try {
                int opcion = leerEntero("Opción") - 1;
                if (opcion >= 0 && opcion < temas.length) {
                    return temas[opcion];
                } else {
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                }
            } catch (Exception e) {
                System.out.println("Error al leer la opción.");
            }
        }
    }
    
    public static TipoSolucion leerTipoSolucion() {
        System.out.println("Seleccione el tipo de solución:");
        TipoSolucion[] tipos = TipoSolucion.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i].getDescripcion());
        }
        
        while (true) {
            try {
                int opcion = leerEntero("Opción") - 1;
                if (opcion >= 0 && opcion < tipos.length) {
                    return tipos[opcion];
                } else {
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                }
            } catch (Exception e) {
                System.out.println("Error al leer la opción.");
            }
        }
    }
    
    public static void presionarEnterParaContinuar() {
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }
    
    public static void limpiarPantalla() {
        // Simulación de limpiar pantalla
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
