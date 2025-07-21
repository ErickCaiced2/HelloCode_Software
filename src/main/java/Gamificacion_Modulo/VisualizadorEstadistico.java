package Gamificacion_Modulo;

public class VisualizadorEstadistico {
    private EstrategiaVisualizacion estrategia;

    public VisualizadorEstadistico(EstrategiaVisualizacion estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de visualización no puede ser nula.");
        }
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaVisualizacion estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de visualización no puede ser nula.");
        }
        this.estrategia = estrategia;
    }

    public EstrategiaVisualizacion getEstrategia() {
        return estrategia;
    }

    public void visualizar(Estadistica estadistica) {
        if (estadistica == null) {
            System.out.println(">>> No se proporcionó una estadística para visualizar.");
            return;
        }
        estrategia.visualizar(estadistica);
    }
}
