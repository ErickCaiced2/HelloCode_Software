package GestionAprendizaje_Modulo.Repositorio;

import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;

import java.util.List;

public interface RepositorioModulosEducativos {
    void guardarModuloEducativo(ModuloEducativo modulo);
    void actualizarModuloEducativo(ModuloEducativo modulo);
    ModuloEducativo buscarPorId(String id);

    List<ModuloEducativo> buscarTodos();
}
