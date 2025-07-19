package Gamificacion_Modulo;

public class Estudiante {
    private static Long contadorId = 1L;
    private Long id;
    private String nombre;
    private String email;
    private String usuario;

    public Estudiante(String nombre, String email, String usuario) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.email = email;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
} 