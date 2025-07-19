package model;

import java.util.ArrayList;
import java.util.List;
import filtros.FiltroEjercicio;

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
}