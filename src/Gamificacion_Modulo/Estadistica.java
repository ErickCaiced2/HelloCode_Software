
package Gamificacion_Modulo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Estadistica {
    private static Long contadorId = 1L;
    private Long id;
    private String tipo;
    private Double valor;
    private LocalDateTime fechaGeneracion;
    private Estudiante estudiante;

    public Estadistica(String tipo, Double valor, Estudiante estudiante) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de estadística no puede ser nulo o vacío.");
        }
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        if (valor == null || valor < 0) {
            throw new IllegalArgumentException("El valor debe ser no negativo.");
        }
        this.id = contadorId++;
        this.tipo = tipo;
        this.valor = valor;
        this.estudiante = estudiante;
        this.fechaGeneracion = LocalDateTime.now();
    }

    public static Estadistica generarEstadistica(String tipo, Double valor, Estudiante estudiante) {
        return new Estadistica(tipo, valor, estudiante);
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
        datos.put("estudiante_id", estudiante.getId());
        datos.put("estudiante_nombre", estudiante.getNombre());
        return datos;
    }
}
