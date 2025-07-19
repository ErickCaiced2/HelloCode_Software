package controladores;

import modelo.*;
import enums.*;
import servicios.ComunidadService;
import utilidades.InputHelper;
import utilidades.MenuHelper;

import java.util.List;

/**
 * Controlador para la gestión de usuarios en el módulo de comunidad.
 * Maneja todas las operaciones relacionadas con usuarios.
 */
public class UsuarioControlador implements IControlador {
    private final ComunidadService comunidadService;
    private final ContextoSistema contexto;
    
    public UsuarioControlador(ComunidadService comunidadService, ContextoSistema contexto) {
        this.comunidadService = comunidadService;
        this.contexto = contexto;
    }
    
    @Override
    public void ejecutar() {
        boolean volver = false;
        while (!volver) {
            MenuHelper.mostrarMenuUsuarios();
            int opcion = InputHelper.leerEntero("Seleccione una opción");
            
            switch (opcion) {
                case 1:
                    crearUsuario();
                    break;
                case 2:
                    listarUsuarios();
                    break;
                case 3:
                    conectarUsuario();
                    break;
                case 4:
                    desconectarUsuario();
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
        return "Gestión de Usuarios";
    }
    
    private void crearUsuario() {
        System.out.println("\n=== CREAR NUEVO USUARIO ===");
        String nombre = InputHelper.leerTexto("Nombre del usuario");
        NivelJava nivel = InputHelper.leerNivelJava();
        
        UsuarioTemp usuario = new UsuarioTemp(nombre, nivel);
        contexto.agregarUsuario(usuario);
        
        System.out.println("Usuario creado exitosamente: " + usuario.toString());
        
        if (InputHelper.leerBooleano("¿Desea conectar este usuario a la comunidad?")) {
            if (contexto.getComunidadActual() != null) {
                contexto.getComunidadActual().conectarUsuario(usuario);
                System.out.println("Usuario conectado a la comunidad.");
            } else {
                System.out.println("No hay comunidad activa.");
            }
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void listarUsuarios() {
        MenuHelper.mostrarUsuarios(contexto.getUsuarios());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void conectarUsuario() {
        if (contexto.getComunidadActual() == null) {
            System.out.println("No hay comunidad activa.");
            return;
        }
        
        if (contexto.getUsuarios().isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        
        System.out.println("\n=== CONECTAR USUARIO ===");
        MenuHelper.mostrarUsuarios(contexto.getUsuarios());
        
        int indice = InputHelper.leerEntero("Seleccione el usuario a conectar") - 1;
        if (indice >= 0 && indice < contexto.getUsuarios().size()) {
            UsuarioTemp usuario = contexto.getUsuarios().get(indice);
            contexto.getComunidadActual().conectarUsuario(usuario);
            System.out.println("Usuario conectado exitosamente.");
        } else {
            System.out.println("Selección inválida.");
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void desconectarUsuario() {
        if (contexto.getComunidadActual() == null) {
            System.out.println("No hay comunidad activa.");
            return;
        }
        
        List<UsuarioTemp> usuariosConectados = contexto.getComunidadActual().getUsuariosConectados();
        if (usuariosConectados.isEmpty()) {
            System.out.println("No hay usuarios conectados.");
            return;
        }
        
        System.out.println("\n=== DESCONECTAR USUARIO ===");
        MenuHelper.mostrarUsuarios(usuariosConectados);
        
        int indice = InputHelper.leerEntero("Seleccione el usuario a desconectar") - 1;
        if (indice >= 0 && indice < usuariosConectados.size()) {
            UsuarioTemp usuario = usuariosConectados.get(indice);
            contexto.getComunidadActual().desconectarUsuario(usuario);
            System.out.println("Usuario desconectado exitosamente.");
        } else {
            System.out.println("Selección inválida.");
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
}
