package filtros;

import model.Ejercicio;
import enums.NivelDificultad;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorDificultad implements FiltroEjercicio {
    private NivelDificultad nivel;

    public FiltroPorDificultad(NivelDificultad nivel) {
        this.nivel = nivel;
    }

    @Override
    public List<Ejercicio> aplicarFiltro(List<Ejercicio> ejercicios) {
        return ejercicios.stream()
                .filter(e -> e.getDificultad() == nivel)
                .collect(Collectors.toList());
    }
}