package Modulo_Usuario.Clases;

public enum NivelJava {
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado"),
    EXPERTO("Experto");

    private final String descripcion;

    NivelJava(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
} 