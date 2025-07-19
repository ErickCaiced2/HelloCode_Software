package GestionAprendizaje_Modulo.Repositorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;

public class RepositorioEnMemoria implements RepositorioModulosEducativos {

    private final Map<String, ModuloEducativo> store = new HashMap<>();

    @Override
    public void guardarModuloEducativo(ModuloEducativo modulo) {
        store.put(modulo.getId(), modulo);
    }

    @Override
    public void actualizarModuloEducativo(ModuloEducativo modulo) {
        store.put(modulo.getId(), modulo);
    }

    @Override
    public ModuloEducativo buscarPorId(String id) {
        return store.get(id);
    }

    @Override
    public List<ModuloEducativo> buscarTodos() {
        return List.of();
    }
}
