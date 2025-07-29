package Modulo_Usuario.Controladores;

import Modulo_Usuario.Clases.Roles;
import Modulo_Usuario.Clases.Usuario;
import Modulo_Usuario.Clases.UsuarioAdministrador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField usuarioField;
    @FXML private PasswordField contrasenaField;
    @FXML private PasswordField confirmarContrasenaField;
    @FXML private Label mensajeLabel;
    @FXML private ComboBox<Roles> comboRol;
    @FXML private PasswordField contraseniaAdmin;
    @FXML private Label labelAdmin;

    private List<Usuario> usuarios = new ArrayList<>();
    private static final String USUARIOS_FILE = "src/main/java/Modulo_Usuario/Usuarios/usuarios.txt";

    @FXML
    public void initialize() {
        contraseniaAdmin.setVisible(false);  // Oculta el campo si es INVITADO
        labelAdmin.setVisible(false);

        comboRol.getItems().addAll(Roles.values());
        cargarUsuarios();
        comboRol.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == Roles.USUARIO) {
                contraseniaAdmin.setVisible(false);  // Oculta el campo si es INVITADO
                labelAdmin.setVisible(false);
            } else {
                contraseniaAdmin.setVisible(true);   // Muestra el campo si es ADMIN o ESTUDIANTE
                labelAdmin.setVisible(true);
            }
        });
    }

    private void cargarUsuarios() {
        try {
            File file = new File(USUARIOS_FILE);
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        linea = linea.trim();
                        if (!linea.isEmpty()) {
                            Usuario usuario = Usuario.fromString(linea);
                            if (usuario != null) {
                                usuarios.add(usuario);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensajeLabel.setText("Error al cargar usuarios existentes: " + e.getMessage());
        }

    }

    @FXML
    protected void handleRegister(ActionEvent event) {
        // Obtener datos de los campos
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String username = usuarioField.getText().trim();
        String password = contrasenaField.getText();
        String confirmPassword = confirmarContrasenaField.getText();
        String passwordAdmin = contraseniaAdmin.getText();

        // Validaciones
        if (nombre.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            mostrarMensaje("Por favor complete todos los campos", true);
            return;
        }

        if (!validarEmail(email)) {
            mostrarMensaje("Por favor ingrese un email válido", true);
            return;
        }

        if (username.length() < 3) {
            mostrarMensaje("El usuario debe tener al menos 3 caracteres", true);
            return;
        }

        if (password.length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarMensaje("Las contraseñas no coinciden", true);
            confirmarContrasenaField.clear();
            return;
        }
        if(comboRol.getValue() == null){
            mostrarMensaje("Por favor complete todos los campos", true);
            return;
        }
        if(comboRol.getValue() == Roles.ADMINISTRADOR){
            if(passwordAdmin.isEmpty()){
                mostrarMensaje("Por favor complete todos los campos", true);
                return;
            }
            if(!passwordAdmin.equals(UsuarioAdministrador.getPasswordCreacion())){
                mostrarMensaje("Incorrecto password de creacion", true);
                return;
            }
        }

        // Verificar si el usuario ya existe
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                mostrarMensaje("El usuario ya existe, elija otro nombre de usuario", true);
                usuarioField.clear();
                return;
            }
        }
        Usuario nuevoUsuario;
        if(comboRol.getValue() == Roles.ADMINISTRADOR){
            // Crear nuevo usuario con rol USUARIO y xp inicial 0
            nuevoUsuario = new Usuario(username, password, nombre, email,0,Roles.ADMINISTRADOR);
            verificarGuardarUsuario(nuevoUsuario);
        }
        if(comboRol.getValue() == Roles.USUARIO){
            nuevoUsuario = new Usuario(username, password, nombre, email, 0, Roles.USUARIO);
            verificarGuardarUsuario(nuevoUsuario);
        }


    }

    private void verificarGuardarUsuario(Usuario nuevoUsuario) {
        if (guardarUsuario(nuevoUsuario)) {
            mostrarMensaje("Usuario registrado exitosamente", false);
            limpiarCampos();

            // Esperar un momento y volver al login
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(this::volverAlLogin);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else {
            mostrarMensaje("Error al guardar el usuario", true);
        }
    }

    private boolean validarEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean guardarUsuario(Usuario usuario) {
        try {
            File file = new File(USUARIOS_FILE);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Agregar el nuevo usuario a la lista
            usuarios.add(usuario);

            // Escribir todos los usuarios al archivo
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                for (Usuario u : usuarios) {
                    writer.write(u.toString());
                    writer.newLine();
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        mensajeLabel.setText(mensaje);
        if (esError) {
            mensajeLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        } else {
            mensajeLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
        }
    }

    private void limpiarCampos() {
        nombreField.clear();
        emailField.clear();
        usuarioField.clear();
        contrasenaField.clear();
        confirmarContrasenaField.clear();
    }

    @FXML
    protected void volverAlLogin(ActionEvent event) {
        volverAlLogin();
    }

    private void volverAlLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 360, 640);

            Stage stage = new Stage();
            stage.setTitle("Hello Code Software - Iniciar Sesión");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Stage thisStage = (Stage) nombreField.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeLabel.setText("Error al cargar pantalla de login: " + e.getMessage());
        }
    }
}