package enums;

public enum EstadoHilo {
    ABIERTO("Abierto"),
    RESUELTO("Resuelto"),
    CERRADO("Cerrado");
    
    private final String descripcion;
    
    EstadoHilo(String descripcion) {
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