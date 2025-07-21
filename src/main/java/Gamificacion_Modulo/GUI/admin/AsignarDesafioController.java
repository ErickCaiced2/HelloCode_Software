package Gamificacion_Modulo.GUI.admin;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Gamificacion_Modulo.Desafio;
import Gamificacion_Modulo.DesafioMensual;
import Gamificacion_Modulo.DesafioSemanal;
import Gamificacion_Modulo.Main;
import Gamificacion_Modulo.ProgresoEstudiante;
import Modulo_Usuario.Clases.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AsignarDesafioController implements Initializable {
    
    @FXML private VBox mainVBox;
    @FXML private ComboBox<String> cbUsuarios;
    @FXML private Label lblInfoUsuario;
    @FXML private VBox vboxDesafiosDisponibles;
    @FXML private Label lblDesafioSeleccionado;
    @FXML private VBox vboxDesafiosActivos;
    @FXML private Button btnCancelar;
    @FXML private Button btnAsignar;
    
    private List<Usuario> usuarios;
    private List<Desafio> desafiosDisponibles;
    private List<RadioButton> radioButtonsDesafios;
    private ToggleGroup toggleGroupDesafios;
    private Usuario usuarioSeleccionado;
    private Desafio desafioSeleccionado;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarios = Main.getUsuarios();
        desafiosDisponibles = Main.getDesafiosDisponibles();
        radioButtonsDesafios = new ArrayList<>();
        toggleGroupDesafios = new ToggleGroup();
        
        cargarUsuarios();
        cargarDesafiosDisponibles();
        configurarEventos();
        
        System.out.println(">>> Asignador de desafíos inicializado");
        System.out.println(">>> Usuarios disponibles: " + usuarios.size());
        System.out.println(">>> Desafíos disponibles: " + desafiosDisponibles.size());
    }
    

    
    private void cargarUsuarios() {
        cbUsuarios.getItems().clear();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            cbUsuarios.getItems().add((i+1) + " - " + usuario.getNombre());
        }
    }
    
    private void cargarDesafiosDisponibles() {
        vboxDesafiosDisponibles.getChildren().clear();
        radioButtonsDesafios.clear();
        
        if (desafiosDisponibles.isEmpty()) {
            Label lblVacio = new Label("No hay desafíos disponibles para asignar");
            lblVacio.setStyle("-fx-text-fill: #666666;");
            vboxDesafiosDisponibles.getChildren().add(lblVacio);
            
            Label lblInstruccion = new Label("Crea desafíos usando las otras opciones del panel de administración");
            lblInstruccion.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");
            lblInstruccion.setWrapText(true);
            vboxDesafiosDisponibles.getChildren().add(lblInstruccion);
            return;
        }
        
        for (Desafio desafio : desafiosDisponibles) {
            String tipo = desafio instanceof DesafioSemanal ? "Semanal" : "Mensual";
            String meta = "";
            
            if (desafio instanceof DesafioSemanal) {
                meta = " (Meta: " + ((DesafioSemanal) desafio).getMetaSemanal() + " actividades/semana)";
            } else if (desafio instanceof DesafioMensual) {
                meta = " (Meta: " + ((DesafioMensual) desafio).getObjetivoMensual() + " actividades/mes)";
            }
            
            RadioButton rb = new RadioButton();
            rb.setToggleGroup(toggleGroupDesafios);
            rb.setText("🎯 " + desafio.getNombre() + " (" + tipo + ")" + meta);
            rb.setWrapText(true);
            rb.setUserData(desafio);
            
            // Agregar información de logros
            Label lblLogros = new Label("   Logros asociados: " + desafio.getLogrosDisponibles().size() + 
                                      " | Descripción: " + desafio.getDescripcion());
            lblLogros.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
            lblLogros.setWrapText(true);
            
            radioButtonsDesafios.add(rb);
            vboxDesafiosDisponibles.getChildren().addAll(rb, lblLogros);
            
            // Espaciado entre desafíos
            if (desafiosDisponibles.indexOf(desafio) < desafiosDisponibles.size() - 1) {
                vboxDesafiosDisponibles.getChildren().add(new Label(" ")); // Espaciador
            }
        }
    }
    
    private void configurarEventos() {
        // Evento al seleccionar usuario
        cbUsuarios.setOnAction(e -> {
            String seleccion = cbUsuarios.getValue();
            if (seleccion != null) {
                int indice = Integer.parseInt(seleccion.split(" - ")[0]) - 1;
                usuarioSeleccionado = usuarios.get(indice);
                if (usuarioSeleccionado != null) {
                    actualizarInfoUsuario();
                    cargarDesafiosActivos();
                    validarFormulario();
                }
            }
        });
        
        // Evento al seleccionar desafío
        toggleGroupDesafios.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                desafioSeleccionado = (Desafio) newToggle.getUserData();
                lblDesafioSeleccionado.setText("✅ Desafío seleccionado: " + desafioSeleccionado.getNombre());
                lblDesafioSeleccionado.setStyle("-fx-text-fill: #4CAF50;");
                validarFormulario();
            }
        });
    }
    
    private void actualizarInfoUsuario() {
        if (usuarioSeleccionado != null) {
            ProgresoEstudiante progreso = encontrarProgresoPorUsuario(usuarioSeleccionado);
            if (progreso != null) {
                lblInfoUsuario.setText(String.format(
                    "👤 Usuario: %s | ✉ Email: %s | 💎 Puntos: %d | 🏆 Logros: %d",
                    usuarioSeleccionado.getNombre(),
                    usuarioSeleccionado.getEmail(),
                    progreso.getPuntosTotal(),
                    progreso.getLogros().size()
                ));
                lblInfoUsuario.setStyle("-fx-text-fill: #4CAF50;");
            }
        }
    }
    
    private void cargarDesafiosActivos() {
        vboxDesafiosActivos.getChildren().clear();
        
        if (usuarioSeleccionado != null) {
            ProgresoEstudiante progreso = encontrarProgresoPorUsuario(usuarioSeleccionado);
            if (progreso != null) {
                List<Desafio> activos = progreso.getDesafiosActivos();
                
                if (activos.isEmpty()) {
                    Label lblVacio = new Label("Este usuario no tiene desafíos activos");
                    lblVacio.setStyle("-fx-text-fill: #666666;");
                    vboxDesafiosActivos.getChildren().add(lblVacio);
                } else {
                    Label lblTitulo = new Label("Desafíos activos:");
                    lblTitulo.setStyle("-fx-font-weight: bold;");
                    vboxDesafiosActivos.getChildren().add(lblTitulo);
                    
                    for (Desafio desafio : activos) {
                        String tipo = desafio instanceof DesafioSemanal ? "Semanal" : "Mensual";
                        Label lblDesafio = new Label("• " + desafio.getNombre() + " (" + tipo + ")");
                        vboxDesafiosActivos.getChildren().add(lblDesafio);
                    }
                }
            }
        }
    }
    
    private void validarFormulario() {
        boolean valid = usuarioSeleccionado != null && desafioSeleccionado != null;
        btnAsignar.setDisable(!valid);
    }
    
    @FXML
    private void onAsignarClicked() {
        try {
            if (usuarioSeleccionado == null) {
                mostrarError("Selecciona un usuario");
                return;
            }
            
            if (desafioSeleccionado == null) {
                mostrarError("Selecciona un desafío");
                return;
            }
            
            boolean asignado = asignarDesafioAUsuario(desafioSeleccionado, usuarioSeleccionado);
            
            if (asignado) {
                mostrarExito("¡Éxito!", "El desafío '" + desafioSeleccionado.getNombre() + 
                    "' ha sido asignado a " + usuarioSeleccionado.getNombre() + " exitosamente.");
                
                // Actualizar la interfaz
                cargarDesafiosActivos();
                actualizarInfoUsuario();
                
                // Limpiar selección
                toggleGroupDesafios.selectToggle(null);
                lblDesafioSeleccionado.setText("Selecciona un desafío de la lista");
                lblDesafioSeleccionado.setStyle("-fx-text-fill: #666666;");
                
                System.out.println(">>> Desafío asignado exitosamente a " + usuarioSeleccionado.getNombre());
            } else {
                mostrarError("No se pudo asignar el desafío. Verifica que no esté ya asignado.");
            }
            
        } catch (Exception e) {
            mostrarError("Error al asignar desafío: " + e.getMessage());
            System.err.println("Error en asignación: " + e.getMessage());
        }
    }
    
    private boolean asignarDesafioAUsuario(Desafio desafio, Usuario usuario) {
        ProgresoEstudiante progreso = encontrarProgresoPorUsuario(usuario);
        if (progreso == null) {
            return false;
        }
        
        // Verificar que no tenga ya este desafío
        for (Desafio d : progreso.getDesafiosActivos()) {
            if (d.getNombre().equals(desafio.getNombre())) {
                return false; // Ya tiene este desafío
            }
        }
        
        // Activar y agregar el desafío
        desafio.activar();
        progreso.agregarDesafio(desafio);
        
        System.out.println(">>> Desafío '" + desafio.getNombre() + "' asignado a " + usuario.getNombre());
        return true;
    }
    
    @FXML
    private void onCancelarClicked() {
        System.out.println(">>> Cancelando asignación de desafío");
        cerrarVentana();
    }
    
    private Usuario encontrarUsuarioPorIndice(int indice) {
        if (indice >= 0 && indice < usuarios.size()) {
            return usuarios.get(indice);
        }
        return null;
    }
    
    private ProgresoEstudiante encontrarProgresoPorUsuario(Usuario usuario) {
        return Main.getProgresos().stream()
            .filter(p -> p.getUsuario().getUsername().equals(usuario.getUsername()))
            .findFirst()
            .orElse(null);
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    // Método para refrescar los datos cuando se abra la ventana
    public void refrescarDatos() {
        usuarios = Main.getUsuarios();
        desafiosDisponibles = Main.getDesafiosDisponibles();
        
        cargarUsuarios();
        cargarDesafiosDisponibles();
        
        System.out.println(">>> Datos refrescados - Usuarios: " + usuarios.size() + ", Desafíos: " + desafiosDisponibles.size());
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
} 