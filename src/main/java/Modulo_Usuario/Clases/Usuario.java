package Modulo_Usuario.Clases;

public class Usuario extends UsuarioBase {
    private String nombre;
    private String email;
    private int xp;
    private Roles rol;


    // Constructor por defecto
    public Usuario() {
        super();
        this.xp = 0;
        this.rol = Roles.USUARIO;
    }

    // Constructor con parámetros
    public Usuario(String username, String password) {
        super(username, password);
        this.xp = 0;
        this.rol = Roles.USUARIO;
    }

    // Constructor completo
    public Usuario(String username, String password, String nombre, String email) {
        super(username, password);
        this.nombre = nombre;
        this.email = email;
        this.xp = 0;
        this.rol = Roles.USUARIO;
    }

    // Constructor con XP
    public Usuario(String username, String password, String nombre, String email, int xp) {
        super(username, password);
        this.nombre = nombre;
        this.email = email;
        this.xp = xp;
        this.rol = Roles.USUARIO;
    }

    public Usuario(String username, String password, String nombre, String email, int xp, Roles rol) {
        super(username, password);
        this.nombre = nombre;
        this.email = email;
        this.xp = xp;
        this.rol = rol;
    }


    // ... otros getters y setters existentes ...


    public int getXp() {
        return xp;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public void setXp(int xp) {
        if (xp > 10000) {
            this.xp = 10000;
        } else {
            this.xp = xp;
        }
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
        // Usar getters para username y password heredados de UsuarioBase
        return getUsername() + ";" + getPassword() + ";" + nombre + ";" + email + ";" + xp + ";" + rol;
    }

    // Método para crear usuario desde línea de archivo
    public static Usuario fromString(String linea) {
        String[] datos = linea.split(";");
        if (datos.length >= 2) {
            Usuario usuario = new Usuario(datos[0], datos[1]);
            if (datos.length >= 3) usuario.setNombre(datos[2]);
            if (datos.length >= 4) usuario.setEmail(datos[3]);
            if (datos.length >= 5) {
                try {
                    usuario.setXp(Integer.parseInt(datos[4]));
                } catch (NumberFormatException e) {
                    usuario.setXp(0);
                }
            }
            if (datos.length >= 6) {
                try {
                    usuario.setRol(Roles.valueOf(datos[5]));
                } catch (Exception e) {
                    usuario.setRol(Roles.USUARIO);
                }
            }
            return usuario;
        }
        return null;
    }
} 