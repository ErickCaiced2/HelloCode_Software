package Comunidad_Modulo.servicios;

import Modulo_Usuario.Clases.UsuarioComunidad;
import Comunidad_Modulo.modelo.*;
import java.util.List;

public class ComunidadService {
    
    public void procesarUsuarioEnGrupo(UsuarioComunidad usuario, GrupoDiscusion grupo) {
        if (grupo.esApropiado(usuario)) {
            grupo.unirseGrupo(usuario);
            System.out.println("Usuario " + usuario.getNombre() + " se unió al grupo: " + grupo.getTitulo());
        } else {
            System.out.println("El usuario no cumple con los requisitos para unirse al grupo");
        }
    }
    
    public void procesarUsuarioEnGrupoCompartir(UsuarioComunidad usuario, GrupoCompartir grupo) {
        if (grupo.esApropiado(usuario)) {
            grupo.unirseGrupo(usuario);
            System.out.println("Usuario " + usuario.getNombre() + " se unió al grupo: " + grupo.getTitulo());
        } else {
            System.out.println("El usuario no cumple con los requisitos para unirse al grupo");
        }
    }
    
    public List<GrupoDiscusion> obtenerGruposRecomendados(UsuarioComunidad usuario, ForoGeneral foro) {
        return foro.buscarGruposDiscusionRecomendados(usuario);
    }
    
    public List<GrupoCompartir> obtenerGruposCompartirRecomendados(UsuarioComunidad usuario, ForoGeneral foro) {
        return foro.buscarGruposCompartirRecomendados(usuario);
    }
    
    public void procesarRespuestaEnHilo(HiloDiscusion hilo, String contenido, UsuarioComunidad autor) {
        try {
            hilo.responder(contenido, autor);
            System.out.println("Respuesta agregada exitosamente");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void procesarSolucionEnGrupo(GrupoCompartir grupo, Solucion solucion) {
        try {
            grupo.compartirSolucion(solucion);
            System.out.println("Solución compartida exitosamente");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
