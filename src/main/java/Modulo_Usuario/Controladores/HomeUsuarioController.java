package Modulo_Usuario.Controladores;

import Gamificacion_Modulo.clases.Main;
import MetodosGlobales.MetodosFrecuentes;
import Modulo_Usuario.Clases.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeUsuarioController {
    
    @FXML private VBox contentContainer;
    @FXML private Label usuarioNombreLabel;
    @FXML private Label xpLabel;
    private Usuario usuario;
    @FXML private Button btnPerfil;
    @FXML private Button btnRanking;
    @FXML private Button btnComunidad;
    @FXML private Button btnSalir;
    @FXML private Button btnLecciones;
    @FXML private Button btnEjercicios;
    @FXML private Button btnProgreso;
    @FXML private Button btnHome;
    @FXML private Button btnPerfil2;
    @FXML private Button btnConfiguracion;

    @FXML
    public void initialize() {
        // Se mostrará el XP cuando se llame a setUsuario()
        // Marcar como activo el botón Home por defecto
        marcarBotonActivo(btnHome);
    }

    @FXML
    private void irARanking(ActionEvent event) {
        try {
            // PASO 1: Inicializar el backend del módulo de gamificación
            Gamificacion_Modulo.clases.Main.inicializarDesdeModuloExterno();
            // Cargar Ranking.fxml
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/Ranking.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a Ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void irAComunidad(ActionEvent event) {
        MetodosFrecuentes.cambiarVentana((Stage) btnComunidad.getScene().getWindow(), "/Modulo_Comunidad/Views/Comunidad.fxml", "Comunidad");
        //mostrarMensaje("Comunidad", "Funcionalidad de comunidad próximamente");
    }

    @FXML
    private void irAHome(ActionEvent event) {
        // Abrir módulo de usuario (perfil)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/perfil.fxml"));
            Parent root = loader.load();
            PerfilController pc = loader.getController();
            pc.setUsuario(usuario);
            Scene scene = new Scene(root, 360, 640);
            Stage stage = new Stage();
            stage.setTitle("Perfil de Usuario");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            // cerrar la actual
            Stage thisStage = (Stage) btnHome.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error abriendo perfil: " + e.getMessage());
        }
    }

    @FXML
    private void irALecciones(ActionEvent event) {
        marcarBotonActivo(btnLecciones);
        mostrarMensaje("Lecciones", "Funcionalidad de lecciones próximamente");
    }

    @FXML
    private void irAEjercicios(ActionEvent event) {
        marcarBotonActivo(btnEjercicios);
        mostrarMensaje("Ejercicios", "Funcionalidad de ejercicios próximamente");
    }

    @FXML
    private void irAProgreso(ActionEvent event) {
        marcarBotonActivo(btnProgreso);
        mostrarMensaje("Progreso", "Funcionalidad de progreso próximamente");
    }

    @FXML
    private void irAPerfil(ActionEvent event) {
        try {
            // PASO 1: Inicializar el backend del módulo de gamificación
            Gamificacion_Modulo.clases.Main.inicializarDesdeModuloExterno();

            // PASO 2: Cargar la interfaz gráfica
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gamificacion_Modulo/fxml/PerfilUsuario.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 360, 720);

            Stage stage = new Stage();
            stage.setTitle("Hello Code Software - Gamificación");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Cerrar la pantalla actual
            Stage thisStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir el módulo de gamificación: " + e.getMessage());
        }
    }

    @FXML
    private void irAConfiguracion(ActionEvent event) {
        marcarBotonActivo(btnConfiguracion);
        mostrarMensaje("Configuración", "Funcionalidad de configuración próximamente");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 360, 640);

            Stage stage = new Stage();
            stage.setTitle("Hello Code Software - Iniciar Sesión");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Cerrar la pantalla actual
            Stage thisStage = (Stage) btnSalir.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cerrar sesión: " + e.getMessage());
        }
    }

    // Efectos hover para botones del navbar
    @FXML
    private void onMouseEntered(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle(btn.getStyle().replace("#424874", "#6B7A99") + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }
    
    @FXML
    private void onMouseExited(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle(btn.getStyle().replace("#6B7A99", "#424874") + "; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }
    
    @FXML
    private void onMouseEnteredSalir(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle(btn.getStyle().replace("#e74c3c", "#c0392b") + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }
    
    @FXML
    private void onMouseExitedSalir(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle(btn.getStyle().replace("#c0392b", "#e74c3c") + "; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    private void marcarBotonActivo(Button botonActivo) {
        // Restablecer todos los botones del navbar (solo 4 módulos)
        Button[] botones = {btnHome, btnPerfil2, btnRanking, btnComunidad};
        
        for (Button boton : botones) {
            if (boton != null) {
                boton.setStyle("-fx-background-color: white; -fx-text-fill: #424874; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 18; -fx-border-radius: 18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0.3, 0, 2); -fx-cursor: hand; -fx-padding: 8;");
            }
        }
        
        // Marcar el botón activo
        if (botonActivo != null) {
            botonActivo.setStyle("-fx-background-color: #424874; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 18; -fx-border-radius: 18; -fx-effect: dropshadow(gaussian, rgba(66,72,116,0.4), 8, 0.4, 0, 4); -fx-cursor: hand; -fx-padding: 8;");
        }
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    /**
     * Inicializa la vista con los datos del usuario.
     */
    public void setUsuario(Usuario usuario) {
        // Guardar referencia y mostrar XP real del usuario
        this.usuario = usuario;
        xpLabel.setText(usuario.getXp() + " XP");
    }
}
