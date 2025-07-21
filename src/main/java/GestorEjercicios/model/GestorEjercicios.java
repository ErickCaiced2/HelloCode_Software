package GestorEjercicios.model;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.filtros.FiltroEjercicio;
import GestorEjercicios.filtros.FiltroPorDificultad;
import GestorEjercicios.filtros.FiltroPorLenguaje;

import java.util.ArrayList;
import java.util.List;

public class GestorEjercicios {
    private List<Ejercicio> ejercicios;

    public GestorEjercicios() {
        this.ejercicios = new ArrayList<>();
    }

    public boolean agregarEjercicio(Ejercicio ejercicio) {
        return ejercicios.add(ejercicio);
    }

    public List<Ejercicio> obtenerEjercicios() {
        return ejercicios;
    }

    public List<Ejercicio> aplicarFiltros(List<FiltroEjercicio> filtros) {
        List<Ejercicio> resultado = new ArrayList<>(ejercicios);
        for (FiltroEjercicio filtro : filtros) {
            resultado = filtro.aplicarFiltro(resultado);
        }
        return resultado;
    }

    public List<Ejercicio> filtrarPorEtiquetas(LenguajeProgramacion lenguaje, NivelDificultad dificultad) {
        List<FiltroEjercicio> filtros = new ArrayList<>();
        if (lenguaje != null) filtros.add(new FiltroPorLenguaje(lenguaje));
        if (dificultad != null) filtros.add(new FiltroPorDificultad(dificultad));
        return aplicarFiltros(filtros);
    }
}