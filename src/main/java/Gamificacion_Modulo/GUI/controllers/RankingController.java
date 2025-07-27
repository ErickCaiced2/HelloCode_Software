package Gamificacion_Modulo.GUI.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Gamificacion_Modulo.Main;
import Gamificacion_Modulo.ProgresoEstudiante;
import Modulo_Usuario.Clases.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RankingController implements Initializable {

    @FXML private VBox mainContainer;
    @FXML private Label titleLabel;
    @FXML private ComboBox<String> usuarioSelector;
    @FXML private VBox rankingEntriesContainer;
    @FXML private Button navButton1;
    @FXML private Button navButton2; 
    @FXML private Button navButton3;

    // Usuario actual (será establecido dinámicamente)
    private String currentUserName = null;
    private int usuarioActualIndex = 0;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(">>> Controlador de Ranking inicializado");
        
        // Registrar este controlador para recibir notificaciones
        Main.registrarRankingController(this);
        
        // Configurar hover effects para botones
        configurarHoverEffects();
        
        // Cargar usuarios en ComboBox
        cargarUsuarios();
        
        // Cargar y mostrar ranking inicial
        cargarRanking();
    }
    
    private void configurarHoverEffects() {
        // Hover effects para botones de navegación
        configurarHoverButton(navButton1, "#a6b1e1", "#9fa8da");
        configurarHoverButton(navButton2, "#a6b1e1", "#9fa8da");
        // navButton3 ya está marcado como actual con color diferente
    }
    
    private void configurarHoverButton(Button button, String normalColor, String hoverColor) {
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: " + hoverColor + "; -fx-background-radius: 5; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: " + normalColor + "; -fx-background-radius: 5; -fx-border-radius: 5;"));
    }
    
    public void cargarRanking() {
        try {
            List<Usuario> usuarios = Main.getUsuarios();
            List<ProgresoEstudiante> progresos = Main.getProgresos();
            
            if (usuarios.isEmpty()) {
                mostrarMensajeVacio();
                return;
            }
            
            // Crear lista de usuarios con sus puntuaciones
            List<EstudianteRanking> estudiantesRanking = new ArrayList<>();
            
            for (Usuario usuario : usuarios) {
                ProgresoEstudiante progreso = encontrarProgresoPorUsuario(usuario, progresos);
                int puntos = progreso != null ? progreso.getPuntosTotal() : 0;
                estudiantesRanking.add(new EstudianteRanking(usuario.getNombre(), puntos));
            }
            
            // Ordenar por puntuación descendente
            estudiantesRanking.sort((e1, e2) -> Integer.compare(e2.getPuntos(), e1.getPuntos()));
            
            // Encontrar posición del usuario actual
            int posicionUsuario = encontrarPosicionUsuario(estudiantesRanking);
            
            // Actualizar título con posición
            String mensaje = currentUserName != null ? 
                currentUserName + " ocupas el puesto #" + posicionUsuario :
                "Selecciona un usuario para ver su posición";
            titleLabel.setText(mensaje);
            
            // Obtener top 7 estudiantes para mostrar
            List<EstudianteRanking> topEstudiantes = estudiantesRanking.stream()
                    .limit(7)
                    .collect(Collectors.toList());
            
            // Crear entradas de ranking
            crearEntradasRanking(topEstudiantes);
            
        } catch (Exception e) {
            System.err.println("Error al cargar ranking: " + e.getMessage());
            e.printStackTrace();
            mostrarMensajeError();
        }
    }
    
    private ProgresoEstudiante encontrarProgresoPorUsuario(Usuario usuario, List<ProgresoEstudiante> progresos) {
        return progresos.stream()
                .filter(p -> p.getUsuario().getUsername().equals(usuario.getUsername()))
                .findFirst()
                .orElse(null);
    }
    
    private int encontrarPosicionUsuario(List<EstudianteRanking> estudiantesRanking) {
        if (currentUserName == null) {
            return 1; // Posición por defecto
        }
        
        for (int i = 0; i < estudiantesRanking.size(); i++) {
            if (estudiantesRanking.get(i).getNombre().equals(currentUserName)) {
                return i + 1; // Posición 1-indexada
            }
        }
        return estudiantesRanking.size(); // Si no se encuentra, última posición
    }
    
    private void crearEntradasRanking(List<EstudianteRanking> topEstudiantes) {
        rankingEntriesContainer.getChildren().clear();
        
        for (int i = 0; i < topEstudiantes.size(); i++) {
            EstudianteRanking estudiante = topEstudiantes.get(i);
            
            // Container para cada entrada
            HBox rankingItem = new HBox();
            rankingItem.setAlignment(Pos.CENTER_LEFT);
            rankingItem.setPrefWidth(295);
            
            // Nombre del estudiante
            Label nameLabel = new Label(estudiante.getNombre());
            nameLabel.setFont(Font.font("Inter", FontWeight.BOLD, 18));
            
            // Espaciador
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            // Puntuación
            Label expLabel = new Label(estudiante.getPuntos() + " EXP");
            expLabel.setFont(Font.font("Inter", FontWeight.BOLD, 18));
            
            // Resaltar usuario actual
            if (estudiante.getNombre().equals(currentUserName)) {
                nameLabel.setTextFill(Color.BLUE);
                expLabel.setTextFill(Color.BLUE);
            } else {
                nameLabel.setTextFill(Color.BLACK);
                expLabel.setTextFill(Color.BLACK);
            }
            
            rankingItem.getChildren().addAll(nameLabel, spacer, expLabel);
            rankingEntriesContainer.getChildren().add(rankingItem);
        }
    }
    
    private void mostrarMensajeVacio() {
        rankingEntriesContainer.getChildren().clear();
        Label mensajeVacio = new Label("No hay estudiantes registrados");
        mensajeVacio.setFont(Font.font("Inter", FontWeight.NORMAL, 16));
        mensajeVacio.setTextFill(Color.GRAY);
        rankingEntriesContainer.getChildren().add(mensajeVacio);
        
        titleLabel.setText("Ranking no disponible");
    }
    
    private void mostrarMensajeError() {
        rankingEntriesContainer.getChildren().clear();
        Label mensajeError = new Label("Error al cargar el ranking");
        mensajeError.setFont(Font.font("Inter", FontWeight.NORMAL, 16));
        mensajeError.setTextFill(Color.RED);
        rankingEntriesContainer.getChildren().add(mensajeError);
        
        titleLabel.setText("Error en el ranking");
    }
    
    // Métodos de navegación
    @FXML
    private void navButton1() {
        System.out.println(">>> Navegando a Desafíos desde Ranking");
        try {
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/Desafios.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a Desafios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void navButton2() {
        System.out.println(">>> Navegando a Perfil de Usuario desde Ranking");
        try {
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/PerfilUsuario.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a PerfilUsuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void navButton3() {
        System.out.println(">>> Ya estás en la pantalla de Ranking");
        // Refrescar ranking
        cargarRanking();
    }
    
    private void cargarUsuarios() {
        try {
            if (usuarioSelector != null) {
                usuarioSelector.getItems().clear();
                List<Usuario> usuarios = Main.getUsuarios();
                
                for (Usuario usuario : usuarios) {
                    usuarioSelector.getItems().add(usuario.getNombre());
                }
                
                if (!usuarios.isEmpty()) {
                    usuarioSelector.getSelectionModel().selectFirst();
                    usuarioActualIndex = 0;
                    currentUserName = usuarios.get(0).getNombre();
                }
                
                System.out.println(">>> Usuarios cargados en ComboBox: " + usuarios.size());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    @FXML
    private void cambiarUsuario() {
        try {
            if (usuarioSelector != null && usuarioSelector.getSelectionModel().getSelectedIndex() >= 0) {
                usuarioActualIndex = usuarioSelector.getSelectionModel().getSelectedIndex();
                currentUserName = usuarioSelector.getValue();
                System.out.println(">>> Cambiando a usuario: " + currentUserName);
                
                // Recargar ranking con nuevo usuario
                cargarRanking();
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar usuario: " + e.getMessage());
        }
    }
    
    // Método público para actualizar la información cuando se actualicen los datos
    public void actualizarDatosUsuario() {
        cargarUsuarios();
        cargarRanking();
    }
    
    // Clase interna para manejar datos de ranking
    private static class EstudianteRanking {
        private String nombre;
        private int puntos;
        
        public EstudianteRanking(String nombre, int puntos) {
            this.nombre = nombre;
            this.puntos = puntos;
        }
        
        public String getNombre() { return nombre; }
        public int getPuntos() { return puntos; }
    }

    // Método para limpiar recursos al cerrar
    public void cleanup() {
        Main.desregistrarRankingController();
        System.out.println(">>> Controlador de Ranking limpiado");
    }
} 