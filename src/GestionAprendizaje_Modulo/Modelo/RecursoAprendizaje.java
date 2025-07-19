package GestionAprendizaje_Modulo.Modelo;

import java.util.UUID;
// Usamos una clase abstracta para que sea fácil agregar nuevos tipos de recursos en el futuro.
public abstract class RecursoAprendizaje {
    // Cada recurso tiene un identificador único generado automáticamente.
    private final String id;

    // Título del recurso, por ejemplo: "Video introductorio", "Guía PDF", etc.
    private final String titulo;

    // Constructor que asigna un UUID único y el título proporcionado.
    public RecursoAprendizaje(String titulo) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
    }

    // Método getter para obtener el título del recurso.
    public String getTitulo() {
        return titulo;
    }

    // Metodo abstracto que cada tipo de recurso debe implementar.
    // Por ejemplo, un video puede devolver su URL, un PDF puede devolver su nombre de archivo, etc.
    public abstract String obtenerDetalle();

    // Representación en texto del recurso, útil para mostrarlo por consola.
    // Imprime el tipo de recurso (nombre de la clase) seguido del título.
    @Override
    public String toString() {
        return String.format("%s: %s", this.getClass().getSimpleName(), titulo);
    }
}
