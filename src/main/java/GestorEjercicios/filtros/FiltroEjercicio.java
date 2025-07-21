package GestorEjercicios.filtros;

import GestorEjercicios.model.Ejercicio;

import java.util.List;

public interface FiltroEjercicio {
    List<Ejercicio> aplicarFiltro(List<Ejercicio> ejercicios);
}