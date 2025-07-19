package Gamificacion_Modulo;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class Ranking {
    private List<ProgresoEstudiante> rankingGeneral;
    private Map<String, List<ProgresoEstudiante>> rankingsSemanal;
    private Map<String, List<ProgresoEstudiante>> rankingsMensual;

    public Ranking() {
        this.rankingGeneral = new ArrayList<>();
        this.rankingsSemanal = new HashMap<>();
        this.rankingsMensual = new HashMap<>();
    }

    public void actualizarRanking(ProgresoEstudiante estudiante) {
        // Agregar estudiante si no existe
        if (!rankingGeneral.contains(estudiante)) {
            rankingGeneral.add(estudiante);
        }
        
        // Ordenar por puntos totales (descendente)
        Collections.sort(rankingGeneral, new Comparator<ProgresoEstudiante>() {
            @Override
            public int compare(ProgresoEstudiante e1, ProgresoEstudiante e2) {
                return e2.getPuntosTotal().compareTo(e1.getPuntosTotal());
            }
        });
        
        System.out.println(">> Ranking actualizado. Posicion de " + 
                          estudiante.getEstudiante().getNombre() + ": " + 
                          calcularPosicion(estudiante));
    }

    public List<ProgresoEstudiante> obtenerRankingGeneral() {
        return new ArrayList<>(rankingGeneral);
    }

    public List<ProgresoEstudiante> obtenerRankingSemanal() {
        // Simplificado - retorna el ranking general por ahora
        return obtenerRankingGeneral();
    }

    public List<ProgresoEstudiante> obtenerRankingMensual() {
        // Simplificado - retorna el ranking general por ahora
        return obtenerRankingGeneral();
    }

    public Integer calcularPosicion(ProgresoEstudiante estudiante) {
        int posicion = rankingGeneral.indexOf(estudiante) + 1;
        return posicion > 0 ? posicion : rankingGeneral.size() + 1;
    }

    public Map<String, Object> generarEstadisticasRanking() {
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("total_estudiantes", rankingGeneral.size());
        if (!rankingGeneral.isEmpty()) {
            estadisticas.put("puntos_maximo", rankingGeneral.get(0).getPuntosTotal());
            estadisticas.put("lider", rankingGeneral.get(0).getEstudiante().getNombre());
        }
        return estadisticas;
    }
} 