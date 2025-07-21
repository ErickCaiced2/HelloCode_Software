package Comunidad_Modulo.enums;

public enum TipoSolucion {
    TEXTO("Texto"),
    IMAGEN("Imagen"),
    CODIGO("Código");
    
    private final String descripcion;
    
    TipoSolucion(String descripcion) {
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
