package GestionAprendizaje_Modulo.Gestor;

import java.util.List;
import java.util.Objects;

import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;


public class ValidadorDeContenido {

    public boolean validarDatos(ModuloEducativo modulo) {
        return verificarFormato(modulo) && validarLecciones(modulo.getLecciones());
    }

    public boolean verificarFormato(ModuloEducativo modulo) {
        List<RecursoAprendizaje> recursos = modulo.getRecursos();
        if (recursos == null || recursos.isEmpty()) return false;


        return recursos.stream()
                .filter(Objects::nonNull)
                .allMatch(r -> {
                    String detalle = r.obtenerDetalle();
                    return detalle != null && !detalle.isBlank();
                });
    }

    public boolean validarLecciones(List<Leccion> lecciones) {
        if (lecciones == null || lecciones.isEmpty()) return false;

        return lecciones.stream()
                .allMatch(this::validarLeccion);
    }

    public boolean validarLeccion(Leccion leccion) {
        return leccion != null &&
                leccion.getTitulo() != null && !leccion.getTitulo().isBlank() &&
                leccion.getDescripcion() != null && !leccion.getDescripcion().isBlank();
    }
}
