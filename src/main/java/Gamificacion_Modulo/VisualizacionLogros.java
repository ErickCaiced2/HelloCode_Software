package Gamificacion_Modulo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VisualizacionLogros implements EstrategiaVisualizacion {
    private List<Logro> logrosDisponibles;

    public VisualizacionLogros(List<Logro> logrosDisponibles) {
        this.logrosDisponibles = logrosDisponibles != null ? logrosDisponibles : new ArrayList<>();
    }

    @Override
    public void visualizar(Estadistica estadistica) {
        Map<String, Object> datos = estadistica.getDatos();
        System.out.println("\n=== VISUALIZACIÓN DE ESTADÍSTICA DE LOGROS ===");
        System.out.println("Tipo: " + datos.get("tipo"));
        System.out.println("Valor: " + datos.get("valor"));
        System.out.println("Estudiante: " + datos.get("estudiante_nombre") + " (ID: " + datos.get("estudiante_id") + ")");
        System.out.println("Fecha: " + datos.get("fechaGeneracion"));
        System.out.println("Logros disponibles en el sistema: " + logrosDisponibles.size());
    }

    public void mostrarLogrosDesbloqueados() {
        if (logrosDisponibles.isEmpty()) {
            System.out.println(">>> No hay logros disponibles en el sistema.");
            return;
        }
        System.out.println("\n=== LOGROS DISPONIBLES ===");
        for (Logro logro : logrosDisponibles) {
            System.out.println("* " + logro.getNombre() + " - " + logro.getDescripcion() +
                    " (+" + logro.getPuntos() + " puntos, Criterio: " + logro.getCriteriosDesbloqueo() + ")");
        }
    }
}