package GestionAprendizaje_Modulo.Modelo;

// Clase concreta que representa un recurso de tipo documento PDF.
// Hereda de la clase abstracta RecursoAprendizaje.
public class DocumentoPDF extends RecursoAprendizaje {

    // URL desde donde se puede descargar el documento PDF.
    private final String urlDescarga;

    // Número total de páginas del documento PDF.
    private final int numPaginas;

    // Constructor que recibe el título, la URL de descarga y el número de páginas.
    // Llama al constructor de la superclase con el título.
    public DocumentoPDF(String titulo, String urlDescarga, int numPaginas) {
        super(titulo);
        this.urlDescarga = urlDescarga;
        this.numPaginas = numPaginas;
    }

    // Implementación concreta del método abstracto de RecursoAprendizaje.
    // Devuelve un mensaje con la cantidad de páginas y el enlace de descarga del PDF.
    @Override
    public String obtenerDetalle() {
        return String.format("Descargar PDF (%d páginas) desde: %s", numPaginas, urlDescarga);
    }
}

