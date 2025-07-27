package Gamificacion_Modulo.GUI.controllers;

import Gamificacion_Modulo.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class DesafiosController {
    
    @FXML
    private AnchorPane mainPane;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private ImageView imgEllipse;
    
    @FXML
    private HBox navigationBar;
    
    @FXML
    private Button navButton1;
    
    @FXML
    private Button navButton2;
    
    @FXML
    private Button navButton3;

    // Botón 1 - Desafíos (current) - se queda en esta pantalla
    @FXML
    private void navButton1() {
        System.out.println(">>> Ya estás en la pantalla de Desafíos");
        // Ya estamos en la pantalla de desafíos, no hacer nada o refrescar
    }

    // Botón 2 - Perfil - navegar a PerfilUsuario.fxml
    @FXML
    private void navButton2() {
        System.out.println(">>> Navegando a Perfil de Usuario desde Desafíos");
        try {
            // Cargar PerfilUsuario.fxml desde la carpeta fxml
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/PerfilUsuario.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a PerfilUsuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Botón 3 - Ranking - navegar a Ranking.fxml
    @FXML
    private void navButton3() {
        System.out.println(">>> Navegando a Ranking desde Desafíos");
        try {
            // Cargar Ranking.fxml
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/Ranking.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a Ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método de inicialización
    @FXML
    private void initialize() {
        System.out.println(">>> Controlador de Desafíos inicializado");
        // Configuración adicional si es necesaria
    }
}
