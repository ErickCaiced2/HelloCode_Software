package modelo;

import enums.NivelJava;
import enums.TipoTema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ForoGeneral {
    private String idForo;
    private List<GrupoDiscusion> gruposDiscusion;
    private List<GrupoCompartir> gruposCompartir;
    
    public ForoGeneral() {
        this.idForo = UUID.randomUUID().toString();
        this.gruposDiscusion = new ArrayList<>();
        this.gruposCompartir = new ArrayList<>();
    }
    
    // Getters
    public String getIdForo() {
        return idForo;
    }
    
    public List<GrupoDiscusion> getGruposDiscusion() {
        return new ArrayList<>(gruposDiscusion);
    }
    
    public List<GrupoCompartir> getGruposCompartir() {
        return new ArrayList<>(gruposCompartir);
    }
    
    // Métodos de negocio
    public GrupoDiscusion crearGrupoDiscusion(String titulo, NivelJava nivel, TipoTema tema) {
        GrupoDiscusion grupo = new GrupoDiscusion(titulo, nivel, tema);
        gruposDiscusion.add(grupo);
        return grupo;
    }
    
    public GrupoCompartir crearGrupoCompartir(String titulo, NivelJava nivel, TipoTema tema) {
        GrupoCompartir grupo = new GrupoCompartir(titulo, nivel, tema);
        gruposCompartir.add(grupo);
        return grupo;
    }
    
    public List<GrupoDiscusion> buscarGruposDiscusionPorNivel(NivelJava nivel) {
        return gruposDiscusion.stream()
                             .filter(g -> g.getNivelJava() == nivel)
                             .collect(Collectors.toList());
    }
    
    public List<GrupoCompartir> buscarGruposCompartirPorNivel(NivelJava nivel) {
        return gruposCompartir.stream()
                             .filter(g -> g.getNivelJava() == nivel)
                             .collect(Collectors.toList());
    }
    
    public List<GrupoDiscusion> buscarGruposDiscusionPorTema(TipoTema tema) {
        return gruposDiscusion.stream()
                             .filter(g -> g.getTipoTema() == tema)
                             .collect(Collectors.toList());
    }
    
    public List<GrupoCompartir> buscarGruposCompartirPorTema(TipoTema tema) {
        return gruposCompartir.stream()
                             .filter(g -> g.getTipoTema() == tema)
                             .collect(Collectors.toList());
    }
    
    public List<GrupoDiscusion> buscarGruposDiscusionRecomendados(UsuarioTemp usuario) {
        return gruposDiscusion.stream()
                             .filter(g -> g.esApropiado(usuario))
                             .collect(Collectors.toList());
    }
    
    public List<GrupoCompartir> buscarGruposCompartirRecomendados(UsuarioTemp usuario) {
        return gruposCompartir.stream()
                             .filter(g -> g.esApropiado(usuario))
                             .collect(Collectors.toList());
    }
    
    public void eliminarGrupoDiscusion(String idGrupo) {
        gruposDiscusion.removeIf(g -> g.getIdGrupo().equals(idGrupo));
    }
    
    public void eliminarGrupoCompartir(String idGrupo) {
        gruposCompartir.removeIf(g -> g.getIdGrupo().equals(idGrupo));
    }
    
    @Override
    public String toString() {
        return String.format("Foro General: %d grupos discusión, %d grupos compartir", 
                           gruposDiscusion.size(), gruposCompartir.size());
    }
}
