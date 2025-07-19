package controladores;

import modelo.*;
import enums.*;
import servicios.ComunidadService;
import utilidades.InputHelper;
import utilidades.MenuHelper;

import java.util.List;

/**
 * Controlador para la gestión del foro en el módulo de comunidad.
 * Maneja todas las operaciones relacionadas con grupos de discusión y compartir.
 */
public class ForoControlador implements IControlador {
    private final ComunidadService comunidadService;
    private final ContextoSistema contexto;
    
    public ForoControlador(ComunidadService comunidadService, ContextoSistema contexto) {
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
            MenuHelper.mostrarMenuForo();
            int opcion = InputHelper.leerEntero("Seleccione una opción");
            
            switch (opcion) {
                case 1:
                    crearGrupoDiscusion();
                    break;
                case 2:
                    crearGrupoCompartir();
                    break;
                case 3:
                    listarGruposDiscusion();
                    break;
                case 4:
                    listarGruposCompartir();
                    break;
                case 5:
                    unirseAGrupo();
                    break;
                case 6:
                    crearHiloDiscusion();
                    break;
                case 7:
                    responderHilo();
                    break;
                case 8:
                    compartirSolucion();
                    break;
                case 9:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
    
    @Override
    public String getNombreModulo() {
        return "Gestión de Foro";
    }
    
    private void crearGrupoDiscusion() {
        System.out.println("\n=== CREAR GRUPO DE DISCUSIÓN ===");
        String titulo = InputHelper.leerTexto("Título del grupo");
        NivelJava nivel = InputHelper.leerNivelJava();
        TipoTema tema = InputHelper.leerTipoTema();
        
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        GrupoDiscusion grupo = foro.crearGrupoDiscusion(titulo, nivel, tema);
        
        System.out.println("Grupo creado exitosamente: " + grupo.toString());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void crearGrupoCompartir() {
        System.out.println("\n=== CREAR GRUPO DE COMPARTIR ===");
        String titulo = InputHelper.leerTexto("Título del grupo");
        NivelJava nivel = InputHelper.leerNivelJava();
        TipoTema tema = InputHelper.leerTipoTema();
        
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        GrupoCompartir grupo = foro.crearGrupoCompartir(titulo, nivel, tema);
        
        System.out.println("Grupo creado exitosamente: " + grupo.toString());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void listarGruposDiscusion() {
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        MenuHelper.mostrarGruposDiscusion(foro.getGruposDiscusion());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void listarGruposCompartir() {
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        MenuHelper.mostrarGruposCompartir(foro.getGruposCompartir());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void unirseAGrupo() {
        List<UsuarioTemp> usuariosConectados = contexto.getComunidadActual().getUsuariosConectados();
        if (usuariosConectados.isEmpty()) {
            System.out.println("No hay usuarios conectados.");
            return;
        }
        
        System.out.println("\n=== UNIRSE A GRUPO ===");
        MenuHelper.mostrarUsuarios(usuariosConectados);
        
        int indiceUsuario = InputHelper.leerEntero("Seleccione el usuario") - 1;
        if (indiceUsuario < 0 || indiceUsuario >= usuariosConectados.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        UsuarioTemp usuario = usuariosConectados.get(indiceUsuario);
        
        System.out.println("Tipo de grupo:");
        System.out.println("1. Grupo de Discusión");
        System.out.println("2. Grupo de Compartir");
        
        int tipoGrupo = InputHelper.leerEntero("Seleccione el tipo");
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        
        if (tipoGrupo == 1) {
            unirseAGrupoDiscusion(usuario, foro);
        } else if (tipoGrupo == 2) {
            unirseAGrupoCompartir(usuario, foro);
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void unirseAGrupoDiscusion(UsuarioTemp usuario, ForoGeneral foro) {
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();
        if (grupos.isEmpty()) {
            System.out.println("No hay grupos de discusión disponibles.");
            return;
        }
        
        MenuHelper.mostrarGruposDiscusion(grupos);
        int indiceGrupo = InputHelper.leerEntero("Seleccione el grupo") - 1;
        
        if (indiceGrupo >= 0 && indiceGrupo < grupos.size()) {
            GrupoDiscusion grupo = grupos.get(indiceGrupo);
            comunidadService.procesarUsuarioEnGrupo(usuario, grupo);
        } else {
            System.out.println("Selección inválida.");
        }
    }
    
    private void unirseAGrupoCompartir(UsuarioTemp usuario, ForoGeneral foro) {
        List<GrupoCompartir> grupos = foro.getGruposCompartir();
        if (grupos.isEmpty()) {
            System.out.println("No hay grupos de compartir disponibles.");
            return;
        }
        
        MenuHelper.mostrarGruposCompartir(grupos);
        int indiceGrupo = InputHelper.leerEntero("Seleccione el grupo") - 1;
        
        if (indiceGrupo >= 0 && indiceGrupo < grupos.size()) {
            GrupoCompartir grupo = grupos.get(indiceGrupo);
            comunidadService.procesarUsuarioEnGrupoCompartir(usuario, grupo);
        } else {
            System.out.println("Selección inválida.");
        }
    }
    
    private void crearHiloDiscusion() {
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();
        
        if (grupos.isEmpty()) {
            System.out.println("No hay grupos de discusión disponibles.");
            return;
        }
        
        System.out.println("\n=== CREAR HILO DE DISCUSIÓN ===");
        MenuHelper.mostrarGruposDiscusion(grupos);
        
        int indiceGrupo = InputHelper.leerEntero("Seleccione el grupo") - 1;
        if (indiceGrupo < 0 || indiceGrupo >= grupos.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        GrupoDiscusion grupo = grupos.get(indiceGrupo);
        List<UsuarioTemp> miembros = grupo.getMiembros();
        
        if (miembros.isEmpty()) {
            System.out.println("No hay miembros en este grupo.");
            return;
        }
        
        MenuHelper.mostrarUsuarios(miembros);
        int indiceUsuario = InputHelper.leerEntero("Seleccione el autor") - 1;
        
        if (indiceUsuario < 0 || indiceUsuario >= miembros.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        UsuarioTemp autor = miembros.get(indiceUsuario);
        String titulo = InputHelper.leerTexto("Título del hilo");
        String problema = InputHelper.leerTexto("Descripción del problema");
        
        grupo.crearHilo(titulo, problema, autor);
        System.out.println("Hilo creado exitosamente.");
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void responderHilo() {
        System.out.println("\n=== RESPONDER HILO ===");
        
        ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();
        
        if (grupos.isEmpty()) {
            System.out.println("No hay grupos de discusión disponibles.");
            InputHelper.presionarEnterParaContinuar();
            return;
        }
        
        // Seleccionar grupo
        System.out.println("Grupos de discusión disponibles:");
        for (int i = 0; i < grupos.size(); i++) {
            GrupoDiscusion grupo = grupos.get(i);
            System.out.println((i + 1) + ". " + grupo.getTitulo() + 
                             " (" + grupo.getHilos().size() + " hilos)");
        }
        
        int indiceGrupo = InputHelper.leerEntero("Seleccione el grupo") - 1;
        if (indiceGrupo < 0 || indiceGrupo >= grupos.size()) {
            System.out.println("Selección inválida.");
            InputHelper.presionarEnterParaContinuar();
            return;
        }
        
        GrupoDiscusion grupo = grupos.get(indiceGrupo);
        List<HiloDiscusion> hilos = grupo.getHilos();
        
        if (hilos.isEmpty()) {
            System.out.println("No hay hilos en este grupo.");
            InputHelper.presionarEnterParaContinuar();
            return;
        }
        
        // Seleccionar hilo
        System.out.println("\nHilos disponibles:");
        for (int i = 0; i < hilos.size(); i++) {
            HiloDiscusion hilo = hilos.get(i);
            System.out.println((i + 1) + ". " + hilo.getTitulo() + 
                             " (" + hilo.getRespuestas().size() + " respuestas) [" + 
                             hilo.getEstado() + "]");
        }
        
        int indiceHilo = InputHelper.leerEntero("Seleccione el hilo") - 1;
        if (indiceHilo < 0 || indiceHilo >= hilos.size()) {
            System.out.println("Selección inválida.");
            InputHelper.presionarEnterParaContinuar();
            return;
        }
        
        HiloDiscusion hilo = hilos.get(indiceHilo);
        
        // Seleccionar usuario que responde
        List<UsuarioTemp> usuariosConectados = contexto.getComunidadActual().getUsuariosConectados();
        if (usuariosConectados.isEmpty()) {
            System.out.println("No hay usuarios conectados.");
            InputHelper.presionarEnterParaContinuar();
            return;
        }
        
        System.out.println("\nUsuarios conectados:");
        MenuHelper.mostrarUsuarios(usuariosConectados);
        
        int indiceUsuario = InputHelper.leerEntero("Seleccione el usuario que responde") - 1;
        if (indiceUsuario < 0 || indiceUsuario >= usuariosConectados.size()) {
            System.out.println("Selección inválida.");
            InputHelper.presionarEnterParaContinuar();
            return;
        }
        
        UsuarioTemp autor = usuariosConectados.get(indiceUsuario);
        
        // Leer la respuesta
        System.out.println("\nProblema: " + hilo.getProblema());
        String respuesta = InputHelper.leerTexto("Escriba su respuesta");
        
        // Enviar respuesta con moderación
        Moderador moderador = contexto.getComunidadActual().getModerador();
        boolean respuestaEnviada = hilo.responder(respuesta, autor, moderador);
        
        if (respuestaEnviada) {
            System.out.println("✅ Respuesta agregada exitosamente.");
        } else {
            System.out.println("🚫 La respuesta no pudo ser enviada.");
        }
        
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void compartirSolucion() {
        // Por ahora, implementación básica
        System.out.println("Función de compartir solución - Implementación completa disponible");
        InputHelper.presionarEnterParaContinuar();
    }
}
