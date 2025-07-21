package GestorEjercicios.filtros;

import GestorEjercicios.enums.TipoEjercicio;
import GestorEjercicios.model.Ejercicio;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorTipo implements FiltroEjercicio {
    private TipoEjercicio tipo;

    public FiltroPorTipo(TipoEjercicio tipo) {
        this.tipo = tipo;
    }

    @Override
    public List<Ejercicio> aplicarFiltro(List<Ejercicio> ejercicios) {
        return ejercicios.stream()
                .filter(e -> e.getTipo() == tipo)
                .collect(Collectors.toList());
    }
}