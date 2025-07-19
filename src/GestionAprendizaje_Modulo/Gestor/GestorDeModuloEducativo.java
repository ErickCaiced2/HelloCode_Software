package GestionAprendizaje_Modulo.Gestor;

import java.util.Map;

import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;
import GestionAprendizaje_Modulo.Repositorio.RepositorioModulosEducativos;



public class GestorDeModuloEducativo {

    private final ValidadorDeContenido validador;
    private final RepositorioModulosEducativos repositorio;

    public GestorDeModuloEducativo(RepositorioModulosEducativos repo) {
        this.repositorio = repo;
        this.validador = new ValidadorDeContenido();
    }

    public ModuloEducativo crearModuloEducativo(Map<String, Object> datos) {
        String id = (String) datos.get("id");
        String titulo = (String) datos.get("titulo");
        String version = (String) datos.get("version");
        String metadatos = (String) datos.get("metadatosCurriculares");

        ModuloEducativo mod = new ModuloEducativo(id, titulo, version, metadatos, "BORRADOR");
        repositorio.guardarModuloEducativo(mod);
        return mod;
    }

    public void editarModuloEducativo(String id, Map<String, Object> cambios) {
        ModuloEducativo mod = repositorio.buscarPorId(id);
        if (mod == null) throw new IllegalArgumentException("Módulo no encontrado: " + id);

        if (cambios.containsKey("titulo"))
            mod.setTitulo((String) cambios.get("titulo"));
        if (cambios.containsKey("version"))
            mod.setVersion((String) cambios.get("version"));
        if (cambios.containsKey("metadatosCurriculares"))
            mod.setMetadatosCurriculares((String) cambios.get("metadatosCurriculares"));

        repositorio.actualizarModuloEducativo(mod);
    }

    public void publicarModuloEducativo(String id) {
        ModuloEducativo mod = repositorio.buscarPorId(id);
        if (mod == null) throw new IllegalArgumentException("Módulo no encontrado: " + id);

        boolean datosValidos = validador.validarDatos(mod);
        boolean leccionesValidas = validador.validarLecciones(mod.getLecciones());

        if (datosValidos && leccionesValidas) {
            mod.setEstado("PUBLICADO");
            repositorio.actualizarModuloEducativo(mod);
        } else {
            throw new IllegalStateException("No cumple validación");
        }
    }
}
