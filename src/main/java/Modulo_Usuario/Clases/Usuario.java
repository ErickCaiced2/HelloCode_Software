package Modulo_Usuario.Clases;

public class Usuario {
    private String username;
    private String password;
    private String nombre;
    private String email;

    // Constructor por defecto
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor completo
    public Usuario(String username, String password, String nombre, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return username + ";" + password + ";" + nombre + ";" + email;
    }

    // Método para crear usuario desde línea de archivo
    public static Usuario fromString(String linea) {
        String[] datos = linea.split(";");
        if (datos.length >= 2) {
            Usuario usuario = new Usuario(datos[0], datos[1]);
            if (datos.length >= 3) usuario.setNombre(datos[2]);
            if (datos.length >= 4) usuario.setEmail(datos[3]);
            return usuario;
        }
        return null;
    }
} 