package Modulo_Usuario.Clases;

public class UsuarioTemp extends Usuario {
    private String idUsuario;
    private NivelJava nivelJava;

    // Constructor por defecto
    public UsuarioTemp() {
        super();
        this.nivelJava = NivelJava.PRINCIPIANTE;
    }

    // Constructor con parámetros básicos
    public UsuarioTemp(String username, String password, String nombre, String email) {
        super(username, password, nombre, email);
        this.idUsuario = username; // Usamos username como idUsuario por defecto
        this.nivelJava = NivelJava.PRINCIPIANTE;
    }

    // Constructor completo
    public UsuarioTemp(String username, String password, String nombre, String email,
                       String idUsuario, NivelJava nivelJava) {
        super(username, password, nombre, email);
        this.idUsuario = idUsuario;
        this.nivelJava = nivelJava;
    }

    // Constructor simplificado para amigos temporales
    public UsuarioTemp(String idUsuario, String nombre, NivelJava nivelJava) {
        super(idUsuario, nombre, null, null); // password y email vacíos para amigos temporales
        this.idUsuario = idUsuario;
        this.nivelJava = nivelJava;
    }

    // Getters y Setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public NivelJava getNivelJava() {
        return nivelJava;
    }

    public void setNivelJava(NivelJava nivelJava) {
        this.nivelJava = nivelJava;
    }

    @Override
    public String toString() {
        return "UsuarioTemp{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", nivelJava=" + nivelJava +
                '}';
    }

    // Método para crear usuario temporal desde línea de archivo
    public static UsuarioTemp fromString(String linea) {
        String[] datos = linea.split(";");
        if (datos.length >= 3) {
            return new UsuarioTemp(datos[0], datos[2], NivelJava.valueOf(datos[3]));
        }
        return null;
    }
}