package Modulo_Usuario.Clases;


public class UsuarioAdministrador extends Usuario {
    private String idAdmin;
    private Boolean esSuperAdmin;
    private static String passwordCreacion =  "12345";

    // Constructor por defecto
    public UsuarioAdministrador() {
        super();
        this.esSuperAdmin = false;
    }

    // Constructor con parámetros básicos
    public UsuarioAdministrador(String username, String password, String nombre, String email, Roles rol) {
        super(username, password, nombre, email);
        this.idAdmin = username; // Usamos username como idAdmin por defecto
        this.setRol(rol);
        this.esSuperAdmin = false;
    }

    // Constructor completo
    public UsuarioAdministrador(String username, String password, String nombre, String email,
                                String idAdmin, Roles rol, Boolean esSuperAdmin) {
        super(username, password, nombre, email);
        this.idAdmin = idAdmin;
        this.setRol(rol);
        this.esSuperAdmin = esSuperAdmin;
    }

    public static String getPasswordCreacion() {
        return passwordCreacion;
    }

    // Getters y Setters
    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }



    public Boolean getEsSuperAdmin() {
        return esSuperAdmin;
    }

    public void setEsSuperAdmin(Boolean esSuperAdmin) {
        this.esSuperAdmin = esSuperAdmin;
    }

    // Verifica si el admin tiene el rol requerido para un módulo
    public boolean tieneAccesoModulo(Roles rolRequerido) {
        return this.getRol() == rolRequerido || Boolean.TRUE.equals(this.esSuperAdmin);
    }

    // Métodos de permisos eliminados, ahora se usa el rol

    @Override
    public String toString() {
        return "UsuarioAdministrador{" +
                "idAdmin='" + idAdmin + '\'' +
                ", nombre=" + getNombre() + '\'' +
                ", rol=" + getRol() +
                ", esSuperAdmin=" + esSuperAdmin +
                '}';
    }
}