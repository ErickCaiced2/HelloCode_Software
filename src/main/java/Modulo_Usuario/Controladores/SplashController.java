package Modulo_Usuario.Controladores;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashController {
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private StackPane logoContainer;
    @FXML private StackPane rootPane;

    @FXML
    public void initialize() {
        // Configurar la barra de progreso
        progressBar.setProgress(0);
        
        // Iniciar animación del logo
        animarLogo();
        
        // Crear timeline para simular carga
        Timeline timeline = new Timeline();
        
        // Actualizar progreso y estado
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.ZERO, e -> {
                statusLabel.setText("Inicializando...");
                progressBar.setProgress(0.1);
            }),
            new KeyFrame(Duration.seconds(1), e -> {
                statusLabel.setText("Cargando módulos...");
                progressBar.setProgress(0.3);
            }),
            new KeyFrame(Duration.seconds(2), e -> {
                statusLabel.setText("Preparando interfaz...");
                progressBar.setProgress(0.6);
            }),
            new KeyFrame(Duration.seconds(3), e -> {
                statusLabel.setText("Completado");
                progressBar.setProgress(1.0);
            })
        );
        
        timeline.setOnFinished(e -> {
            // Transición suave al login
            AnchorPane root = (AnchorPane) progressBar.getScene().getRoot();
            FadeTransition fadeOut = new FadeTransition(Duration.millis(800), rootPane);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            
            fadeOut.setOnFinished(event -> {
                try {
                    // Cargar la pantalla de login
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/login.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 360, 720);
                    
                    Stage stage = new Stage();
                    stage.setTitle("Login - Sistema de Gestión");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    
                    // Cerrar splash
                    Stage splashStage = (Stage) progressBar.getScene().getWindow();
                    splashStage.close();
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            
            fadeOut.play();
        });
        
        timeline.play();
    }
    
    private void animarLogo() {
        // Animación de escala pulsante suave (como Shazam)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), logoContainer);
        scaleTransition.setByX(0.1);
        scaleTransition.setByY(0.1);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        
        // Animación de brillo sutil
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), logoContainer);
        fadeTransition.setFromValue(0.9);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        
        // Iniciar animaciones
        scaleTransition.play();
        fadeTransition.play();
    }
} 