package GestionAprendizaje_Modulo.Modelo;

import java.util.ArrayList;
import java.util.List;

public class ModuloEducativo {
    private final String id;
    private String titulo;
    private String version;
    private String metadatosCurriculares;
    private String estado;
    private final List<Leccion> lecciones;
    private final List<RecursoAprendizaje> recursos;

    public ModuloEducativo(String id, String titulo, String version, String metadatosCurriculares, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.version = version;
        this.metadatosCurriculares = metadatosCurriculares;
        this.estado = estado;
        this.lecciones = new ArrayList<>();
        this.recursos = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getVersion() { return version; }
    public String getMetadatosCurriculares() { return metadatosCurriculares; }
    public String getEstado() { return estado; }
    public List<Leccion> getLecciones() { return lecciones; }
    public List<RecursoAprendizaje> getRecursos() { return recursos; }

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setVersion(String version) { this.version = version; }
    public void setMetadatosCurriculares(String metadatos) { this.metadatosCurriculares = metadatos; }
    public void setEstado(String estado) { this.estado = estado; }

    // Operaciones
    public void agregarLeccion(Leccion leccion) {
        lecciones.add(leccion);
    }

    public void agregarRecurso(RecursoAprendizaje recurso) {
        recursos.add(recurso);
    }

    public List<Leccion> listarLecciones() {
        return new ArrayList<>(lecciones);
    }

    public List<RecursoAprendizaje> listarRecursos() {
        return new ArrayList<>(recursos);
    }
}
