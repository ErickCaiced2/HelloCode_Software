package Modulo_Usuario.Clases;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdministrador extends Usuario {
    private String idAdmin;
    private List<String> permisos;
    private Integer nivelAcceso;
    private Boolean esSuperAdmin;

    // Constructor por defecto
    public UsuarioAdministrador() {
        super();
        this.permisos = new ArrayList<>();
        this.nivelAcceso = 1;
        this.esSuperAdmin = false;
    }

    // Constructor con parámetros básicos
    public UsuarioAdministrador(String username, String password, String nombre, String email) {
        super(username, password, nombre, email);
        this.idAdmin = username; // Usamos username como idAdmin por defecto
        this.permisos = new ArrayList<>();
        this.nivelAcceso = 1;
        this.esSuperAdmin = false;
    }

    // Constructor completo
    public UsuarioAdministrador(String username, String password, String nombre, String email,
                                String idAdmin, Integer nivelAcceso, Boolean esSuperAdmin) {
        super(username, password, nombre, email);
        this.idAdmin = idAdmin;
        this.nivelAcceso = nivelAcceso;
        this.esSuperAdmin = esSuperAdmin;
        this.permisos = new ArrayList<>();
    }

    // Getters y Setters
    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }

    public Integer getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(Integer nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public Boolean getEsSuperAdmin() {
        return esSuperAdmin;
    }

    public void setEsSuperAdmin(Boolean esSuperAdmin) {
        this.esSuperAdmin = esSuperAdmin;
    }

    // Métodos para gestionar permisos
    public void agregarPermiso(String permiso) {
        if (permiso != null && !permisos.contains(permiso)) {
            permisos.add(permiso);
        }
    }

    public void eliminarPermiso(String permiso) {
        permisos.remove(permiso);
    }

    public Boolean tienePermiso(String permiso) {
        return permisos.contains(permiso);
    }

    public void incrementarNivelAcceso() {
        this.nivelAcceso = Math.min(10, this.nivelAcceso + 1);
    }

    public void decrementarNivelAcceso() {
        this.nivelAcceso = Math.max(1, this.nivelAcceso - 1);
    }

    @Override
    public String toString() {
        return "UsuarioAdministrador{" +
                "idAdmin='" + idAdmin + '\'' +
                ", nombre=" + getNombre() + '\'' +
                ", nivelAcceso=" + nivelAcceso +
                ", esSuperAdmin=" + esSuperAdmin +
                ", permisos=" + permisos.size() +
                '}';
    }
}