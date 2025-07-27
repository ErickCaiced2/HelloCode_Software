package Gamificacion_Modulo.GUI.controllers;

import java.util.List;

import Gamificacion_Modulo.Desafio;
import Gamificacion_Modulo.DesafioMensual;
import Gamificacion_Modulo.DesafioSemanal;
import Gamificacion_Modulo.Logro;
import Gamificacion_Modulo.Main;
import Gamificacion_Modulo.ProgresoEstudiante;
import Modulo_Usuario.Clases.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PerfilUsuarioController {
    @FXML
    private VBox root;

    @FXML
    private Label userName;

    @FXML 
    private VBox contenido;
    
    @FXML
    private Label userEmail;
    
    @FXML
    private Label expPoints;
    
    @FXML
    private Label desafiosSemanales;
    
    @FXML
    private Label desafiosMensuales;
    
    @FXML
    private Hyperlink verProgresoDesafios;
    
    @FXML
    private Hyperlink verProgresoLogros;
    
    @FXML
    private HBox achievementList;
    
    @FXML
    private ImageView profileImage;

    @FXML
    private HBox navigationBar;
    
    @FXML
    private ComboBox<String> estudianteSelector;
    
    @FXML
    private Button volverComunidadBtn;
    
    @FXML
    private Button adminBtn;
    
    private int estudianteActualIndex = 0;

    // Botón 1 (izquierda) - navegar a Desafíos
    @FXML
    private void navButton1() {
        System.out.println(">>> Navegando a Desafíos desde Perfil de Usuario");
        try {
            // Cargar Desafios.fxml desde la carpeta fxml
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/Desafios.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a Desafios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Botón 2 (centro) - Perfil - se queda en PerfilUsuario
    @FXML
    private void navButton2() {
        System.out.println(">>> Ya estás en la pantalla de Perfil de Usuario");
        // Ya estamos en la pantalla de perfil, actualizar datos
        actualizarDatosPerfil();
    }

    // Botón 3 (derecha) - navegar a Ranking
    @FXML
    private void navButton3() {
        System.out.println(">>> Navegando a Ranking desde Perfil de Usuario");
        try {
            // Cargar Ranking.fxml
            Main.cambiarEscena("/Gamificacion_Modulo/fxml/Ranking.fxml");
        } catch (Exception e) {
            System.err.println("Error al navegar a Ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para cambiar de estudiante
    @FXML
    private void cambiarEstudiante() {
        if (estudianteSelector != null && estudianteSelector.getSelectionModel().getSelectedIndex() >= 0) {
            estudianteActualIndex = estudianteSelector.getSelectionModel().getSelectedIndex();
            System.out.println(">>> Cambiando a estudiante: " + estudianteSelector.getValue());
            actualizarDatosPerfil();
        }
    }
    
    // Método para volver al módulo de usuarios
    @FXML
    private void volverAComunidad() {
        try {
            System.out.println(">>> Regresando al módulo de usuarios");
            
            // Cargar la pantalla de Home del módulo de usuarios
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/Modulo_Usuario/views/home.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load(), 360, 720);

            // Obtener el Stage actual
            javafx.stage.Stage currentStage = (javafx.stage.Stage) volverComunidadBtn.getScene().getWindow();
            
            // Cambiar la escena
            currentStage.setTitle("Hello Code Software - Panel Principal");
            currentStage.setScene(scene);
            currentStage.centerOnScreen();
            
        } catch (Exception e) {
            System.err.println("Error al volver al módulo de usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para mostrar progreso de desafíos
    @FXML
    private void verProgresoDesafios() {
        System.out.println(">>> Mostrando progreso de desafíos");
        try {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("📋 PROGRESO DE DESAFÍOS\n");
            mensaje.append("========================\n\n");
            
            if (!Main.getProgresos().isEmpty() && estudianteActualIndex < Main.getProgresos().size()) {
                ProgresoEstudiante progreso = Main.getProgresos().get(estudianteActualIndex);
                
                int semanalesActivos = 0;
                int mensualesActivos = 0;
                
                for (Desafio desafio : progreso.getDesafiosActivos()) {
                    if (desafio instanceof DesafioSemanal) {
                        semanalesActivos++;
                    } else if (desafio instanceof DesafioMensual) {
                        mensualesActivos++;
                    }
                }
                
                mensaje.append("🎯 DESAFÍOS ACTIVOS:\n");
                mensaje.append("   Semanales: ").append(semanalesActivos).append("\n");
                mensaje.append("   Mensuales: ").append(mensualesActivos).append("\n");
                mensaje.append("   Total activos: ").append(progreso.getDesafiosActivos().size()).append("\n\n");
                
                mensaje.append("📊 TOTAL DESAFÍOS COMPLETADOS: ").append(progreso.getDesafiosCompletados()).append("\n\n");
                
                // Mostrar detalles de desafíos activos
                if (!progreso.getDesafiosActivos().isEmpty()) {
                    mensaje.append("📋 DETALLE DE DESAFÍOS ACTIVOS:\n");
                    for (Desafio desafio : progreso.getDesafiosActivos()) {
                        String tipo = desafio instanceof DesafioSemanal ? "Semanal" : "Mensual";
                        String progreso_str = "";
                        
                        if (desafio instanceof DesafioSemanal) {
                            DesafioSemanal ds = (DesafioSemanal) desafio;
                            progreso_str = " - " + ds.getActividadesCompletadas() + "/" + ds.getMetaSemanal() + 
                                         " (" + String.format("%.1f", ds.getProgreso()) + "%)";
                        } else if (desafio instanceof DesafioMensual) {
                            DesafioMensual dm = (DesafioMensual) desafio;
                            progreso_str = " - " + dm.getActividadesCompletadas() + "/" + dm.getObjetivoMensual() + 
                                         " (" + String.format("%.1f", dm.getProgreso()) + "%)";
                        }
                        
                        mensaje.append("   • ").append(desafio.getNombre()).append(" (").append(tipo).append(")").append(progreso_str).append("\n");
                    }
                }
            } else {
                mensaje.append("No hay datos de progreso disponibles.");
            }
            
            mostrarAlerta("Progreso de Desafíos", mensaje.toString());
            
        } catch (Exception e) {
            System.err.println("Error al mostrar progreso de desafíos: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo cargar el progreso de desafíos.");
        }
    }

    // Método para mostrar progreso de logros
    @FXML
    private void verProgresoLogros() {
        System.out.println(">>> Mostrando progreso de logros");
        try {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("🏆 PROGRESO DE LOGROS\n");
            mensaje.append("=====================\n\n");
            
            if (!Main.getProgresos().isEmpty() && estudianteActualIndex < Main.getProgresos().size()) {
                ProgresoEstudiante progreso = Main.getProgresos().get(estudianteActualIndex);
                
                mensaje.append("🎖️ LOGROS OBTENIDOS (").append(progreso.getLogros().size()).append("):\n");
                if (progreso.getLogros().isEmpty()) {
                    mensaje.append("   Aún no has obtenido ningún logro.\n\n");
                } else {
                    for (Logro logro : progreso.getLogros()) {
                        mensaje.append("   ✅ ").append(logro.getNombre())
                               .append(" (+").append(logro.getPuntos()).append(" pts)\n");
                        mensaje.append("      ").append(logro.getDescripcion()).append("\n\n");
                    }
                }
                
                mensaje.append("🏅 LOGROS DISPONIBLES (").append(Main.getLogrosDisponibles().size()).append("):\n");
                for (Logro logro : Main.getLogrosDisponibles()) {
                    boolean obtenido = progreso.getLogros().contains(logro);
                    String estado = obtenido ? "✅" : "❌";
                    mensaje.append("   ").append(estado).append(" ").append(logro.getNombre())
                           .append(" (+").append(logro.getPuntos()).append(" pts)\n");
                    mensaje.append("      ").append(logro.getDescripcion()).append("\n\n");
                }
                
                int puntosLogros = progreso.getLogros().stream().mapToInt(Logro::getPuntos).sum();
                mensaje.append("💰 PUNTOS TOTALES POR LOGROS: ").append(puntosLogros);
            } else {
                mensaje.append("No hay datos de progreso disponibles.");
            }
            
            mostrarAlerta("Progreso de Logros", mensaje.toString());
            
        } catch (Exception e) {
            System.err.println("Error al mostrar progreso de logros: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo cargar el progreso de logros.");
        }
    }

    @FXML
    private void initialize() {
        try {
            System.out.println(">>> Controlador de Perfil de Usuario inicializado");
            
            // Registrar este controlador para recibir notificaciones
            Main.registrarPerfilController(this);
            
            // Cargar estudiantes y datos iniciales
            cargarEstudiantesEnComboBox();
            actualizarDatosPerfil();

        } catch (Exception e) {
            System.err.println("Error al inicializar la interfaz de Perfil: " + e.getMessage());
        }
    }

    public void actualizarDatosPerfil() {
        try {
            List<Usuario> usuarios = Main.getUsuarios();
            List<ProgresoEstudiante> progresos = Main.getProgresos();
            
            System.out.println(">>> Actualizando perfil - Usuarios: " + usuarios.size() + ", Índice actual: " + estudianteActualIndex);
            
            // Actualizar ComboBox si los datos han cambiado
            if (estudianteSelector != null && 
                (estudianteSelector.getItems().isEmpty() || estudianteSelector.getItems().size() != usuarios.size())) {
                cargarEstudiantesEnComboBox();
            }
            
            if (!usuarios.isEmpty() && !progresos.isEmpty() && 
                estudianteActualIndex < usuarios.size() && estudianteActualIndex < progresos.size()) {
                
                Usuario usuario = usuarios.get(estudianteActualIndex);
                ProgresoEstudiante progreso = progresos.get(estudianteActualIndex);
                
                System.out.println(">>> Mostrando datos de: " + usuario.getNombre() + " con " + progreso.getPuntosTotal() + " puntos");
                
                // Actualizar información básica
                if (userName != null) userName.setText(usuario.getNombre());
                if (userEmail != null) userEmail.setText("@" + usuario.getEmail().split("@")[0]);
                if (expPoints != null) expPoints.setText(String.valueOf(progreso.getPuntosTotal()));
                
                // Contar desafíos por tipo
                int semanales = 0, mensuales = 0;
                for (Desafio desafio : progreso.getDesafiosActivos()) {
                    if (desafio instanceof DesafioSemanal) {
                        semanales++;
                    } else if (desafio instanceof DesafioMensual) {
                        mensuales++;
                    }
                }
                
                if (desafiosSemanales != null) desafiosSemanales.setText(String.valueOf(semanales));
                if (desafiosMensuales != null) desafiosMensuales.setText(String.valueOf(mensuales));
                
                // Actualizar logros dinámicamente
                actualizarLogrosVisibles(progreso);
                
            } else {
                System.out.println(">>> No hay datos disponibles para mostrar");
                // Datos por defecto si no hay estudiantes
                if (userName != null) userName.setText("Sin usuario");
                if (userEmail != null) userEmail.setText("@ninguno");
                if (expPoints != null) expPoints.setText("0");
                if (desafiosSemanales != null) desafiosSemanales.setText("0");
                if (desafiosMensuales != null) desafiosMensuales.setText("0");
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar datos del perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para cargar usuarios en el ComboBox
    private void cargarEstudiantesEnComboBox() {
        try {
            List<Usuario> usuarios = Main.getUsuarios();
            System.out.println(">>> Cargando usuarios en ComboBox: " + usuarios.size() + " usuarios encontrados");
            
            if (estudianteSelector != null && !usuarios.isEmpty()) {
                estudianteSelector.getItems().clear();
                for (Usuario usuario : usuarios) {
                    estudianteSelector.getItems().add(usuario.getNombre());
                    System.out.println("   - Agregado: " + usuario.getNombre());
                }
                // Seleccionar el primer usuario por defecto
                estudianteSelector.getSelectionModel().selectFirst();
                estudianteActualIndex = 0;
                System.out.println(">>> ComboBox cargado exitosamente con " + usuarios.size() + " usuarios");
            } else {
                System.out.println(">>> ComboBox no pudo cargarse - selector: " + (estudianteSelector != null) + ", usuarios: " + usuarios.size());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar estudiantes en ComboBox: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void actualizarLogrosVisibles(ProgresoEstudiante progreso) {
        try {
            if (achievementList != null) {
                achievementList.getChildren().clear();
                
                // Mostrar hasta 3 logros obtenidos
                int logrosAMostrar = Math.min(3, progreso.getLogros().size());
                
                if (logrosAMostrar == 0) {
                    // Mostrar mensaje si no hay logros
                    Label noLogros = new Label("Sin logros aún");
                    noLogros.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
                    achievementList.getChildren().add(noLogros);
                } else {
                    for (int i = 0; i < logrosAMostrar; i++) {
                        Logro logro = progreso.getLogros().get(i);
                        VBox logroItem = crearElementoLogro(logro);
                        achievementList.getChildren().add(logroItem);
                    }
                    
                    // Si hay más logros, mostrar indicador
                    if (progreso.getLogros().size() > 3) {
                        Label masLogros = new Label("+" + (progreso.getLogros().size() - 3) + " más");
                        masLogros.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
                        achievementList.getChildren().add(masLogros);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar logros visibles: " + e.getMessage());
        }
    }
    
    private VBox crearElementoLogro(Logro logro) {
        VBox logroItem = new VBox();
        logroItem.setAlignment(javafx.geometry.Pos.CENTER);
        logroItem.getStyleClass().add("achievement-item");
        
        // Imagen del logro (usar una imagen por defecto basada en el nombre)
        ImageView imagen = new ImageView();
        imagen.setFitWidth(60);
        imagen.setFitHeight(60);
        
        String imagenUrl = "";
        if (logro.getNombre().toLowerCase().contains("principiante")) {
            imagenUrl = "/Gamificacion_Modulo/images/logro_principiante.png";
        } else if (logro.getNombre().toLowerCase().contains("dedicado")) {
            imagenUrl = "/Gamificacion_Modulo/images/logro_dedicado.png";
        } else if (logro.getNombre().toLowerCase().contains("acumulador")) {
            imagenUrl = "/Gamificacion_Modulo/images/logro_acumulador.png";
        } else {
            imagenUrl = "/Gamificacion_Modulo/images/logro_principiante.png"; // Por defecto
        }
        
        try {
            Image img = new Image(getClass().getResourceAsStream(imagenUrl));
            imagen.setImage(img);
        } catch (Exception e) {
            System.err.println("No se pudo cargar imagen del logro: " + imagenUrl);
        }
        
        Label nombre = new Label(logro.getNombre());
        nombre.setStyle("-fx-font-size: 10px; -fx-text-alignment: center;");
        
        logroItem.getChildren().addAll(imagen, nombre);
        return logroItem;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Button createNavButton(String imageUrl, String text) {
        Button button = new Button();
        button.getStyleClass().add("nav-button");

        ImageView icon = new ImageView(new Image(getClass().getResource(imageUrl).toExternalForm()));
        icon.setFitWidth(24);
        icon.setFitHeight(24);

        button.setGraphic(icon);
        return button;
    }

    // Métodos públicos para actualizar la información
    public void actualizarDatosUsuario(String nombre, String tag, String experiencia) {
        if (userName != null) userName.setText(nombre);
        if (userEmail != null) userEmail.setText("@" + tag);
        if (expPoints != null) expPoints.setText(experiencia);
    }
    
    // Método para abrir el panel de administración
    @FXML
    private void abrirPanelAdmin() {
        try {
            System.out.println(">>> Abriendo panel de administración...");
            Gamificacion_Modulo.GUI.admin.AdminMainController.mostrarVentanaAdmin();
        } catch (Exception e) {
            System.err.println("Error al abrir panel de administración: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para limpiar recursos al cerrar
    public void cleanup() {
        Main.desregistrarPerfilController();
        System.out.println(">>> Controlador de Perfil limpiado");
    }
}
