package GestionAprendizaje_Modulo.Modelo;

// Clase concreta que representa un recurso de tipo artículo web.
// Hereda de la clase abstracta RecursoAprendizaje.
public class Articulo extends RecursoAprendizaje {

    // URL del artículo, normalmente un enlace a una página web.
    private final String url;

    // Constructor que recibe el título del artículo y su URL.
    // Llama al constructor de la superclase con el título.
    public Articulo(String titulo, String url) {
        super(titulo);
        this.url = url;
    }

    // Implementación concreta del método abstracto de RecursoAprendizaje.
    // Devuelve una cadena que invita al usuario a leer el artículo mediante su enlace.
    @Override
    public String obtenerDetalle() {
        return "Leer artículo en: " + url;
    }
}
