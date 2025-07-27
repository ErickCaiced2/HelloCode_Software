
package Gamificacion_Modulo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import Modulo_Usuario.Clases.Usuario;

public class Estadistica {
    private static Long contadorId = 1L;
    private Long id;
    private String tipo;
    private Double valor;
    private LocalDateTime fechaGeneracion;
    private Usuario usuario;

    public Estadistica(String tipo, Double valor, Usuario usuario) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de estadística no puede ser nulo o vacío.");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (valor == null || valor < 0) {
            throw new IllegalArgumentException("El valor debe ser no negativo.");
        }
        this.id = contadorId++;
        this.tipo = tipo;
        this.valor = valor;
        this.usuario = usuario;
        this.fechaGeneracion = LocalDateTime.now();
    }

    public static Estadistica generarEstadistica(String tipo, Double valor, Usuario usuario) {
        return new Estadistica(tipo, valor, usuario);
    }

    public void actualizarValor(Double nuevoValor) {
        if (nuevoValor == null || nuevoValor < 0) {
            throw new IllegalArgumentException("El nuevo valor debe ser no negativo.");
        }
        this.valor = nuevoValor;
        this.fechaGeneracion = LocalDateTime.now();
    }

    public Map<String, Object> getDatos() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("id", id);
        datos.put("tipo", tipo);
        datos.put("valor", valor);
        datos.put("fechaGeneracion", fechaGeneracion);
        datos.put("usuario_username", usuario.getUsername());
        datos.put("usuario_nombre", usuario.getNombre());
        return datos;
    }
}
