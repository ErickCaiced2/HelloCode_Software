package GestorEjercicios.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI_Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar la pantalla principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/PantallaPrincipal.fxml"));
            Parent root = loader.load();
            
            // Configurar la ventana principal
            primaryStage.setTitle("Gestor de Ejercicios - Sistema de Aprendizaje");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            
            // Centrar la ventana en la pantalla
            primaryStage.centerOnScreen();
            
            // Mostrar la ventana
            primaryStage.show();
            
            System.out.println("Aplicación iniciada con Pantalla Principal");
            
        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 