package Gamificacion_Modulo;

import java.util.HashMap;
import java.util.Map;

import java.util.Map;

public class VisualizacionProgreso implements EstrategiaVisualizacion {
    private Map<String, Double> datosProgreso;

    public VisualizacionProgreso(Map<String, Double> datosProgreso) {
        this.datosProgreso = datosProgreso != null ? datosProgreso : new HashMap<>();
    }

    @Override
    public void visualizar(Estadistica estadistica) {
        Map<String, Object> datos = estadistica.getDatos();
        System.out.println("\n=== VISUALIZACIÓN DE ESTADÍSTICA DE PROGRESO ===");
        System.out.println("Tipo: " + datos.get("tipo"));
        System.out.println("Valor: " + datos.get("valor"));
        System.out.println("Estudiante: " + datos.get("estudiante_nombre") + " (ID: " + datos.get("estudiante_id") + ")");
        System.out.println("Fecha: " + datos.get("fechaGeneracion"));
        System.out.println("Desafíos en progreso: " + datosProgreso.size());
    }

    public void mostrarBarrasProgreso() {
        if (datosProgreso.isEmpty()) {
            System.out.println(">>> No hay datos de progreso disponibles.");
            return;
        }
        System.out.println("\n=== BARRAS DE PROGRESO ===");
        for (Map.Entry<String, Double> entry : datosProgreso.entrySet()) {
            String id = entry.getKey();
            Double progreso = entry.getValue();
            int barras = (int) (progreso / 10); // 1 barra por cada 10%
            System.out.println("Desafío ID " + id + ": [" + "=".repeat(barras) + ">" + " ".repeat(10 - barras) + "] " + String.format("%.1f", progreso) + "%");
        }
    }
}