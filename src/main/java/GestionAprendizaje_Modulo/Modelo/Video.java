package GestionAprendizaje_Modulo.Modelo;

// Clase concreta que representa un recurso de tipo video.
// Hereda de la clase abstracta RecursoAprendizaje.
public class Video extends RecursoAprendizaje {

    // URL donde se encuentra alojado el video (puede ser un enlace a YouTube, Vimeo, etc.)
    private final String url;

    // Duración del video en segundos.
    private final int duracionSegundos;

    // Constructor que recibe el título, la URL y la duración.
    // Llama al constructor de la superclase con el título.
    public Video(String titulo, String url, int duracionSegundos) {
        super(titulo);
        this.url = url;
        this.duracionSegundos = duracionSegundos;
    }

    // Implementación concreta del método abstracto de RecursoAprendizaje.
    // Devuelve un mensaje detallado con la URL del video y su duración en minutos.
    @Override
    public String obtenerDetalle() {
        return String.format("Ver video en %s (%d minutos)", url, duracionSegundos / 60);
    }
}
