package utilidades;

import modelo.*;
import java.util.List;

public class MenuHelper {
    
    public static void mostrarMenuPrincipal() {
        System.out.println("\n===== SISTEMA DE COMUNIDAD =====");
        System.out.println("1. Gestionar Usuarios");
        System.out.println("2. Gestionar Comunidad");
        System.out.println("3. Gestionar Foro");
        System.out.println("4. Gestionar Chats Privados");
        System.out.println("5. Salir");
        System.out.println("================================");
    }
    
    public static void mostrarMenuUsuarios() {
        System.out.println("\n=== GESTIÓN DE USUARIOS ===");
        System.out.println("1. Crear Usuario");
        System.out.println("2. Listar Usuarios");
        System.out.println("3. Conectar Usuario");
        System.out.println("4. Desconectar Usuario");
        System.out.println("5. Volver al Menú Principal");
        System.out.println("============================");
    }
    
    public static void mostrarMenuComunidad() {
        System.out.println("\n=== GESTIÓN DE COMUNIDAD ===");
        System.out.println("1. Crear Comunidad");
        System.out.println("2. Asignar Moderador");
        System.out.println("3. Ver Información de Comunidad");
        System.out.println("4. Volver al Menú Principal");
        System.out.println("=============================");
    }
    
    public static void mostrarMenuForo() {
        System.out.println("\n=== GESTIÓN DE FORO ===");
        System.out.println("1. Crear Grupo de Discusión");
        System.out.println("2. Crear Grupo de Compartir");
        System.out.println("3. Listar Grupos de Discusión");
        System.out.println("4. Listar Grupos de Compartir");
        System.out.println("5. Unirse a Grupo");
        System.out.println("6. Crear Hilo de Discusión");
        System.out.println("7. Responder Hilo");
        System.out.println("8. Compartir Solución");
        System.out.println("9. Volver al Menú Principal");
        System.out.println("=======================");
    }
    
    public static void mostrarMenuChats() {
        System.out.println("\n=== GESTIÓN DE CHATS PRIVADOS ===");
        System.out.println("1. Crear Chat Privado");
        System.out.println("2. Enviar Mensaje");
        System.out.println("3. Ver Historial de Chat");
        System.out.println("4. Listar Chats");
        System.out.println("5. Volver al Menú Principal");
        System.out.println("==================================");
    }
    
    public static void mostrarUsuarios(List<UsuarioTemp> usuarios) {
        System.out.println("\n=== LISTA DE USUARIOS ===");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            for (int i = 0; i < usuarios.size(); i++) {
                System.out.println((i + 1) + ". " + usuarios.get(i).toString());
            }
        }
    }
    
    public static void mostrarGruposDiscusion(List<GrupoDiscusion> grupos) {
        System.out.println("\n=== GRUPOS DE DISCUSIÓN ===");
        if (grupos.isEmpty()) {
            System.out.println("No hay grupos de discusión.");
        } else {
            for (int i = 0; i < grupos.size(); i++) {
                System.out.println((i + 1) + ". " + grupos.get(i).toString());
            }
        }
    }
    
    public static void mostrarGruposCompartir(List<GrupoCompartir> grupos) {
        System.out.println("\n=== GRUPOS DE COMPARTIR ===");
        if (grupos.isEmpty()) {
            System.out.println("No hay grupos de compartir.");
        } else {
            for (int i = 0; i < grupos.size(); i++) {
                System.out.println((i + 1) + ". " + grupos.get(i).toString());
            }
        }
    }
    
    public static void mostrarHilos(List<HiloDiscusion> hilos) {
        System.out.println("\n=== HILOS DE DISCUSIÓN ===");
        if (hilos.isEmpty()) {
            System.out.println("No hay hilos en este grupo.");
        } else {
            for (int i = 0; i < hilos.size(); i++) {
                System.out.println((i + 1) + ". " + hilos.get(i).toString());
            }
        }
    }
    
    public static void mostrarChats(List<ChatPrivado> chats) {
        System.out.println("\n=== CHATS PRIVADOS ===");
        if (chats.isEmpty()) {
            System.out.println("No hay chats privados.");
        } else {
            for (int i = 0; i < chats.size(); i++) {
                System.out.println((i + 1) + ". " + chats.get(i).toString());
            }
        }
    }
    
    public static void mostrarMensajes(List<Mensaje> mensajes) {
        System.out.println("\n=== HISTORIAL DE MENSAJES ===");
        if (mensajes.isEmpty()) {
            System.out.println("No hay mensajes en este chat.");
        } else {
            for (Mensaje mensaje : mensajes) {
                System.out.println(mensaje.toString());
            }
        }
    }
    
    public static void mostrarSoluciones(List<Solucion> soluciones) {
        System.out.println("\n=== SOLUCIONES COMPARTIDAS ===");
        if (soluciones.isEmpty()) {
            System.out.println("No hay soluciones compartidas.");
        } else {
            for (int i = 0; i < soluciones.size(); i++) {
                System.out.println((i + 1) + ". " + soluciones.get(i).toString());
            }
        }
    }
}
