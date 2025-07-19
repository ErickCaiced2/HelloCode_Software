package controladores;

import modelo.*;
import servicios.ComunidadService;
import utilidades.InputHelper;
import utilidades.MenuHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de chats privados en el módulo de comunidad.
 * Maneja todas las operaciones relacionadas con la comunicación privada entre usuarios.
 */
public class ChatControlador implements IControlador {
    private final ComunidadService comunidadService;
    private final ContextoSistema contexto;
    
    public ChatControlador(ComunidadService comunidadService, ContextoSistema contexto) {
        this.comunidadService = comunidadService;
        this.contexto = contexto;
    }
    
    @Override
    public void ejecutar() {
        if (!contexto.tieneComunidadActiva()) {
            System.out.println("No hay comunidad activa.");
            return;
        }
        
        boolean volver = false;
        while (!volver) {
            MenuHelper.mostrarMenuChats();
            int opcion = InputHelper.leerEntero("Seleccione una opción");
            
            switch (opcion) {
                case 1:
                    crearChatPrivado();
                    break;
                case 2:
                    enviarMensaje();
                    break;
                case 3:
                    verHistorialChat();
                    break;
                case 4:
                    listarChats();
                    break;
                case 5:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
    
    @Override
    public String getNombreModulo() {
        return "Gestión de Chats Privados";
    }
    
    private void crearChatPrivado() {
        List<UsuarioTemp> usuariosConectados = contexto.getComunidadActual().getUsuariosConectados();
        if (usuariosConectados.size() < 2) {
            System.out.println("Se necesitan al menos 2 usuarios conectados para crear un chat.");
            return;
        }
        
        System.out.println("\n=== CREAR CHAT PRIVADO ===");
        MenuHelper.mostrarUsuarios(usuariosConectados);
        
        List<UsuarioTemp> participantes = new ArrayList<>();
        
        int cantidadParticipantes = InputHelper.leerEntero("¿Cuántos participantes tendrá el chat?");
        
        for (int i = 0; i < cantidadParticipantes; i++) {
            int indice = InputHelper.leerEntero("Seleccione participante " + (i + 1)) - 1;
            if (indice >= 0 && indice < usuariosConectados.size()) {
                UsuarioTemp usuario = usuariosConectados.get(indice);
                if (!participantes.contains(usuario)) {
                    participantes.add(usuario);
                } else {
                    System.out.println("Usuario ya agregado. Seleccione otro.");
                    i--;
                }
            } else {
                System.out.println("Selección inválida.");
                i--;
            }
        }
        
        if (participantes.size() >= 2) {
            ChatPrivado chat = contexto.getComunidadActual().iniciarChatPrivado(participantes);
            System.out.println("Chat creado exitosamente: " + chat.toString());
        } else {
            System.out.println("No se pudo crear el chat. Se necesitan al menos 2 participantes.");
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void enviarMensaje() {
        List<ChatPrivado> chats = contexto.getComunidadActual().getChatsPrivados();
        if (chats.isEmpty()) {
            System.out.println("No hay chats disponibles.");
            return;
        }
        
        System.out.println("\n=== ENVIAR MENSAJE ===");
        MenuHelper.mostrarChats(chats);
        
        int indiceChat = InputHelper.leerEntero("Seleccione el chat") - 1;
        if (indiceChat < 0 || indiceChat >= chats.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        ChatPrivado chat = chats.get(indiceChat);
        List<UsuarioTemp> participantes = chat.getParticipantes();
        
        MenuHelper.mostrarUsuarios(participantes);
        int indiceUsuario = InputHelper.leerEntero("Seleccione el emisor") - 1;
        
        if (indiceUsuario < 0 || indiceUsuario >= participantes.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        UsuarioTemp emisor = participantes.get(indiceUsuario);
        String contenido = InputHelper.leerTexto("Escriba el mensaje");
        
        // Usar el nuevo método con moderación
        Moderador moderador = contexto.getComunidadActual().getModerador();
        boolean mensajeEnviado = chat.enviarMensaje(contenido, emisor, moderador);
        
        if (mensajeEnviado) {
            System.out.println("✅ Mensaje enviado exitosamente.");
        } else {
            System.out.println("🚫 El mensaje no pudo ser enviado.");
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void verHistorialChat() {
        List<ChatPrivado> chats = contexto.getComunidadActual().getChatsPrivados();
        if (chats.isEmpty()) {
            System.out.println("No hay chats disponibles.");
            return;
        }
        
        System.out.println("\n=== VER HISTORIAL DE CHAT ===");
        MenuHelper.mostrarChats(chats);
        
        int indiceChat = InputHelper.leerEntero("Seleccione el chat") - 1;
        if (indiceChat < 0 || indiceChat >= chats.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        ChatPrivado chat = chats.get(indiceChat);
        List<Mensaje> mensajes = chat.obtenerHistorial();
        
        MenuHelper.mostrarMensajes(mensajes);
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void listarChats() {
        List<ChatPrivado> chats = contexto.getComunidadActual().getChatsPrivados();
        MenuHelper.mostrarChats(chats);
        InputHelper.presionarEnterParaContinuar();
    }
}
