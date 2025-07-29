package Modulo_Usuario.Controladores;

import Modulo_Usuario.Clases.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CrudController {
    @FXML private ListView<Usuario> listaUsuarios;
    @FXML private TextField nuevoUsuario;
    @FXML private PasswordField nuevaContrasena;
    @FXML private TextField nuevoNombre;
    @FXML private TextField nuevoEmail;
    @FXML private Label mensajeLabel;
    @FXML private Label usuarioActualLabel;
    
    // Nuevos controles para tipos de usuario
    @FXML private ComboBox<String> tipoUsuarioCombo;
    @FXML private VBox camposComunidad;
    @FXML private ComboBox<NivelJava> nivelJavaCombo;
    @FXML private TextField reputacionField;
    @FXML private VBox camposAdmin;
    @FXML private ComboBox<Roles> rolAdminCombo;
    @FXML private CheckBox esSuperAdminCheck;
    
    private final String ARCHIVO_USUARIOS = "src/main/java/Modulo_Usuario/Usuarios/usuarios.txt";
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario usuarioSeleccionado = null;
    private String usuarioActual = "Admin";

    @FXML
    public void initialize() {
        cargarUsuarios();
        configurarListView();
        configurarComboBoxes();
        configurarEventos();
        usuarioActualLabel.setText("Usuario: " + usuarioActual);
    }

    private void configurarComboBoxes() {
        // Configurar tipos de usuario
        ObservableList<String> tiposUsuario = FXCollections.observableArrayList(
           "Usuario Básico", "Usuario Comunidad", "Usuario Administrador", "Usuario Temporal"
        );
        tipoUsuarioCombo.setItems(tiposUsuario);
        tipoUsuarioCombo.getSelectionModel().selectFirst();

        // Configurar niveles de Java
        ObservableList<NivelJava> nivelesJava = FXCollections.observableArrayList(NivelJava.values());
        nivelJavaCombo.setItems(nivelesJava);
        nivelJavaCombo.getSelectionModel().selectFirst();

        // Configurar roles de administrador
        ObservableList<Roles> roles = FXCollections.observableArrayList(Roles.values());
        if (rolAdminCombo != null) {
            rolAdminCombo.setItems(roles);
            rolAdminCombo.getSelectionModel().selectFirst();
        }
    }

    private void configurarEventos() {
        // Mostrar/ocultar campos según el tipo de usuario seleccionado
        tipoUsuarioCombo.setOnAction(e -> mostrarCamposEspecificos());
    }

    private void mostrarCamposEspecificos() {
        String tipoSeleccionado = tipoUsuarioCombo.getValue();
        
        // Ocultar todos los campos específicos
        camposComunidad.setVisible(false);
        camposAdmin.setVisible(false);
        
        // Mostrar campos según el tipo
        switch (tipoSeleccionado) {
            case "Usuario Comunidad":
                camposComunidad.setVisible(true);
                break;
            case "Usuario Administrador":
                camposAdmin.setVisible(true);
                break;
        }
    }

    private void configurarListView() {
        // Configurar la celda personalizada para mostrar información del usuario
        listaUsuarios.setCellFactory(param -> new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (empty || usuario == null) {
                    setText(null);
                } else {
                    String tipo = obtenerTipoUsuario(usuario);
                    String info = String.format("👤 %s - %s (%s) [%s]", 
                        usuario.getUsername(), 
                        usuario.getNombre() != null ? usuario.getNombre() : "Sin nombre",
                        usuario.getEmail() != null ? usuario.getEmail() : "Sin email",
                        tipo);
                    setText(info);
                }
            }
        });
    }

    private String obtenerTipoUsuario(Usuario usuario) {
        if (usuario instanceof UsuarioComunidad) {
            return "Comunidad";
        } else if (usuario instanceof UsuarioAdministrador) {
            return "Admin";
        } else if (usuario instanceof UsuarioTemp) {
            return "Temp";
        } else {
            return "Básico";
        }
    }

    private void cargarUsuarios() {
        usuarios.clear();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/usuarios.txt");
            if (inputStream == null) {
                File file = new File(ARCHIVO_USUARIOS);
                if (file.exists()) {
                    inputStream = new FileInputStream(file);
                } else {
                    mostrarMensaje("Error: No se encontró el archivo usuarios.txt", "error");
                    return;
                }
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
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
            
            actualizarListView();
            mostrarMensaje("Usuarios cargados: " + usuarios.size(), "info");
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error al cargar usuarios: " + e.getMessage(), "error");
        }
    }

    private void actualizarListView() {
        ObservableList<Usuario> observableList = FXCollections.observableArrayList(usuarios);
        listaUsuarios.setItems(observableList);
    }

    private void guardarUsuarios() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_USUARIOS))) {
            for (Usuario usuario : usuarios) {
                bw.write(usuario.toString() + "\n");
            }
            mostrarMensaje("Usuarios guardados correctamente", "success");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al guardar usuarios: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void agregarUsuario() {
        String username = nuevoUsuario.getText().trim();
        String password = nuevaContrasena.getText().trim();
        String nombre = nuevoNombre.getText().trim();
        String email = nuevoEmail.getText().trim();
        String tipoSeleccionado = tipoUsuarioCombo.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Usuario y contraseña son obligatorios", "error");
            return;
        }

        // Verificar si el usuario ya existe
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                mostrarMensaje("El usuario ya existe", "error");
                return;
            }
        }

        Usuario nuevoUsuario = crearUsuarioSegunTipo(username, password, nombre, email, tipoSeleccionado);
        
        if (nuevoUsuario != null) {
            usuarios.add(nuevoUsuario);
            guardarUsuarios();
            actualizarListView();
            limpiarCampos();
            mostrarMensaje("Usuario agregado correctamente", "success");
        }
    }

    private Usuario crearUsuarioSegunTipo(String username, String password, String nombre, String email, String tipo) {
        switch (tipo) {
            case "Usuario Básico":
                Roles rolBasico = Roles.USUARIO;
                return new Usuario(username, password, nombre, email, 0, rolBasico);

            case "Usuario Comunidad":
                NivelJava nivelJava = nivelJavaCombo.getValue();
                Integer reputacion = 0;
                try {
                    reputacion = Integer.parseInt(reputacionField.getText().trim());
                } catch (NumberFormatException e) {
                    reputacion = 0;
                }
                return new UsuarioComunidad(username, password, nombre, email, username, nivelJava, reputacion);

            case "Usuario Administrador":
                Roles rol = rolAdminCombo != null ? rolAdminCombo.getValue() : Roles.ADMINISTRADOR;
                Boolean esSuperAdmin = esSuperAdminCheck.isSelected();
                return new UsuarioAdministrador(username, password, nombre, email, username, rol, esSuperAdmin);

            case "Usuario Temporal":
                NivelJava nivelTemp = nivelJavaCombo.getValue();
                return new UsuarioTemp(username, nombre, nivelTemp);

            default:
                mostrarMensaje("Tipo de usuario no válido", "error");
                return null;
        }
    }

    @FXML
    private void editarUsuario() {
        usuarioSeleccionado = listaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Seleccione un usuario para editar", "error");
            return;
        }

        // Llenar campos con datos del usuario seleccionado
        nuevoUsuario.setText(usuarioSeleccionado.getUsername());
        nuevaContrasena.setText(usuarioSeleccionado.getPassword());
        nuevoNombre.setText(usuarioSeleccionado.getNombre() != null ? usuarioSeleccionado.getNombre() : "");
        nuevoEmail.setText(usuarioSeleccionado.getEmail() != null ? usuarioSeleccionado.getEmail() : "");
        
        // Configurar tipo de usuario
        String tipo = obtenerTipoUsuario(usuarioSeleccionado);
        switch (tipo) {
            case "Comunidad":
                tipoUsuarioCombo.setValue("Usuario Comunidad");
                if (usuarioSeleccionado instanceof UsuarioComunidad) {
                    UsuarioComunidad uc = (UsuarioComunidad) usuarioSeleccionado;
                    nivelJavaCombo.setValue(uc.getNivelJava());
                    reputacionField.setText(uc.getReputacion().toString());
                }
                break;
            case "Admin":
                tipoUsuarioCombo.setValue("Usuario Administrador");
                if (usuarioSeleccionado instanceof UsuarioAdministrador) {
                    UsuarioAdministrador ua = (UsuarioAdministrador) usuarioSeleccionado;
                    if (rolAdminCombo != null) rolAdminCombo.setValue(ua.getRol());
                    esSuperAdminCheck.setSelected(ua.getEsSuperAdmin());
                }
                break;
            case "Temp":
                tipoUsuarioCombo.setValue("Usuario Temporal");
                if (usuarioSeleccionado instanceof UsuarioTemp) {
                    UsuarioTemp ut = (UsuarioTemp) usuarioSeleccionado;
                    nivelJavaCombo.setValue(ut.getNivelJava());
                }
                break;
            default:
                tipoUsuarioCombo.setValue("Usuario Básico");
        }
        
        mostrarCamposEspecificos();
        mostrarMensaje("Modo edición: " + usuarioSeleccionado.getUsername(), "info");
    }

    @FXML
    private void eliminarUsuario() {
        Usuario usuarioSeleccionado = listaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Seleccione un usuario para eliminar", "error");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro?");
        alert.setContentText("¿Desea eliminar el usuario: " + usuarioSeleccionado.getUsername() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                usuarios.remove(usuarioSeleccionado);
                guardarUsuarios();
                actualizarListView();
                limpiarCampos();
                mostrarMensaje("Usuario eliminado correctamente", "success");
            }
        });
    }

    @FXML
    private void actualizarLista() {
        cargarUsuarios();
    }

    @FXML
    private void limpiarCampos() {
        nuevoUsuario.clear();
        nuevaContrasena.clear();
        nuevoNombre.clear();
        nuevoEmail.clear();
        reputacionField.clear();
        esSuperAdminCheck.setSelected(false);
        tipoUsuarioCombo.getSelectionModel().selectFirst();
        usuarioSeleccionado = null;
        listaUsuarios.getSelectionModel().clearSelection();
        mostrarCamposEspecificos();
        mostrarMensaje("Campos limpiados", "info");
    }

    @FXML
    public void regresarAlHome() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 360, 640);

            Stage stage = new Stage();
            stage.setTitle("Hello Code Software - Panel Principal");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Cerrar la pantalla actual
            Stage thisStage = (Stage) listaUsuarios.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error al regresar al home: " + e.getMessage(), "error");
        }
    }

    private void mostrarMensaje(String mensaje, String tipo) {
        mensajeLabel.setText(mensaje);
        switch (tipo) {
            case "error":
                mensajeLabel.setStyle("-fx-text-fill: #e74c3c;");
                break;
            case "success":
                mensajeLabel.setStyle("-fx-text-fill: #27ae60;");
                break;
            case "info":
                mensajeLabel.setStyle("-fx-text-fill: #3498db;");
                break;
        }
    }

    public void setUsuarioActual(String usuario) {
        this.usuarioActual = usuario;
        if (usuarioActualLabel != null) {
            usuarioActualLabel.setText("Usuario: " + usuario);
        }
    }
}
