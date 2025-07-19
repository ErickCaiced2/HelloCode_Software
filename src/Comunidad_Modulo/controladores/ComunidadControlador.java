package controladores;

import modelo.*;
import servicios.ComunidadService;
import utilidades.InputHelper;
import utilidades.MenuHelper;

/**
 * Controlador para la gestión de comunidades en el módulo de comunidad.
 * Maneja todas las operaciones relacionadas con la administración de comunidades.
 */
public class ComunidadControlador implements IControlador {
    private final ComunidadService comunidadService;
    private final ContextoSistema contexto;
    
    public ComunidadControlador(ComunidadService comunidadService, ContextoSistema contexto) {
        this.comunidadService = comunidadService;
        this.contexto = contexto;
    }
    
    @Override
    public void ejecutar() {
        boolean volver = false;
        while (!volver) {
            MenuHelper.mostrarMenuComunidad();
            int opcion = InputHelper.leerEntero("Seleccione una opción");
            
            switch (opcion) {
                case 1:
                    crearComunidad();
                    break;
                case 2:
                    asignarModerador();
                    break;
                case 3:
                    verInformacionComunidad();
                    break;
                case 4:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
    
    @Override
    public String getNombreModulo() {
        return "Gestión de Comunidad";
    }
    
    private void crearComunidad() {
        System.out.println("\n=== CREAR NUEVA COMUNIDAD ===");
        String nombre = InputHelper.leerTexto("Nombre de la comunidad");
        String descripcion = InputHelper.leerTexto("Descripción");
        
        Comunidad comunidad = new Comunidad(nombre, descripcion);
        contexto.agregarComunidad(comunidad);
        
        if (InputHelper.leerBooleano("¿Desea establecer esta como la comunidad activa?")) {
            contexto.setComunidadActual(comunidad);
        }
        
        System.out.println("Comunidad creada exitosamente: " + comunidad.toString());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void asignarModerador() {
        if (!contexto.tieneComunidadActiva()) {
            System.out.println("No hay comunidad activa.");
            return;
        }
        
        System.out.println("\n=== ASIGNAR MODERADOR ===");
        String nombre = InputHelper.leerTexto("Nombre del moderador");
        
        Moderador moderador = new Moderador(nombre);
        contexto.agregarModerador(moderador);
        contexto.getComunidadActual().setModerador(moderador);
        
        System.out.println("Moderador asignado exitosamente: " + moderador.toString());
        InputHelper.presionarEnterParaContinuar();
    }
    
    private void verInformacionComunidad() {
        if (!contexto.tieneComunidadActiva()) {
            System.out.println("No hay comunidad activa.");
            return;
        }
        
        System.out.println("\n=== INFORMACIÓN DE LA COMUNIDAD ===");
        System.out.println(contexto.getComunidadActual().obtenerEstadisticas());
        InputHelper.presionarEnterParaContinuar();
    }
}
