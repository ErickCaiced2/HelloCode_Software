package GestorEjercicios.filtros;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.model.Ejercicio;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorLenguaje implements FiltroEjercicio {
    private LenguajeProgramacion lenguaje;

    public FiltroPorLenguaje(LenguajeProgramacion lenguaje) {
        this.lenguaje = lenguaje;
    }

    @Override
    public List<Ejercicio> aplicarFiltro(List<Ejercicio> ejercicios) {
        return ejercicios.stream()
                .filter(e -> e.getLenguaje() == lenguaje)
                .collect(Collectors.toList());
    }
}