package Comunidad_Modulo.enums;

public enum TipoTema {
    SINTAXIS("Sintaxis"),
    ESTRUCTURAS_DATOS("Estructuras de Datos"),
    ALGORITMOS("Algoritmos"),
    POO("Programación Orientada a Objetos"),
    EXCEPCIONES("Manejo de Excepciones"),
    FRAMEWORKS("Frameworks");
    
    private final String descripcion;
    
    TipoTema(String descripcion) {
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
