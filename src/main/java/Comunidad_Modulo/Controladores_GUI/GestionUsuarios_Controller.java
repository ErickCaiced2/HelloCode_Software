package Comunidad_Modulo.Controladores_GUI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import Comunidad_Modulo.controladores.ContextoSistema;
import Comunidad_Modulo.integracion.IModuloUsuarios;
import Modulo_Usuario.Clases.UsuarioComunidad;
import Modulo_Usuario.Clases.NivelJava;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class GestionUsuarios_Controller implements Initializable {
    
    // FXML Components que realmente existen en el archivo FXML
    @FXML private TextField txtNombreUsuario;
    @FXML private ComboBox<NivelJava> comboNivelJava;
    @FXML private Button btnCrearUsuario;
    @FXML private Button btnListarUsuarios;
    @FXML private TextField txtIndiceUsuario;
    @FXML private Button btnConectarUsuario;
    @FXML private Button btnDesconectarUsuario;
    @FXML private TextArea txtListaUsuarios;
    @FXML private TextArea txtInformacion;
    @FXML private Button btnVolver;
    
    private IModuloUsuarios moduloUsuarios;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar e inicializar el módulo de usuarios si no existe
        inicializarModuloUsuarios();
        
        // Obtener referencia al módulo de usuarios
        this.moduloUsuarios = ContextoSistema.getInstancia().getModuloUsuarios();
        
        // Configurar ComboBox de niveles
        configurarComboBox();
        
        // Deshabilitar creación de usuarios (se maneja desde Modulo_Usuario)
        deshabilitarCreacionUsuarios();
        
        // Configurar áreas de texto
        configurarAreasTexto();
        
        // Cargar información inicial
        actualizarInformacionUsuarios();
    }
    
    /**
     * Inicializa el módulo de usuarios real si no está configurado
     */
    private void inicializarModuloUsuarios() {
        ContextoSistema contexto = ContextoSistema.getInstancia();
        
        if (contexto.getModuloUsuarios() == null) {
            try {
                // Crear e inicializar el módulo de usuarios real
                Comunidad_Modulo.integracion.ModuloUsuariosReal moduloReal = 
                    new Comunidad_Modulo.integracion.ModuloUsuariosReal();
                
                // Configurarlo en el contexto
                contexto.setModuloUsuarios(moduloReal);
                
                System.out.println("ModuloUsuariosReal inicializado correctamente");
            } catch (Exception e) {
                System.err.println("Error al inicializar ModuloUsuariosReal: " + e.getMessage());
                e.printStackTrace();
                
                // Como fallback, usar el simulado
                contexto.setModuloUsuarios(new Comunidad_Modulo.integracion.ModuloUsuariosSimulado());
                System.out.println("Usando ModuloUsuariosSimulado como fallback");
            }
        }
    }
    
    private void configurarComboBox() {
        // Cargar niveles Java disponibles
        comboNivelJava.setItems(FXCollections.observableArrayList(NivelJava.values()));
        comboNivelJava.getSelectionModel().selectFirst();
    }
    
    private void configurarAreasTexto() {
        // Configurar área de información como solo lectura
        txtInformacion.setEditable(false);
        txtInformacion.setWrapText(true);
        txtInformacion.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
        
        // Configurar área de lista de usuarios como solo lectura
        txtListaUsuarios.setEditable(false);
        txtListaUsuarios.setWrapText(true);
        txtListaUsuarios.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc;");
    }
    
    private void deshabilitarCreacionUsuarios() {
        // Deshabilitar campos de creación
        txtNombreUsuario.setDisable(true);
        comboNivelJava.setDisable(true);
        btnCrearUsuario.setDisable(true);
        
        // Agregar tooltip explicativo
        Tooltip tooltip = new Tooltip("La creación de usuarios se maneja desde el Módulo de Usuarios");
        btnCrearUsuario.setTooltip(tooltip);
        
        // Configurar placeholder
        txtNombreUsuario.setPromptText("Creación deshabilitada - usar Módulo de Usuarios");
        
        // Mostrar mensaje informativo inicial
        txtInformacion.setText("=== GESTIÓN DE USUARIOS ===\n\n" +
                              "La creación de usuarios se maneja desde el Módulo de Usuarios principal.\n" +
                              "Aquí puede visualizar el usuario actual y gestionar conexiones.");
    }
    
    @FXML
    private void crearUsuario() {
        // Mostrar mensaje informativo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Creación de usuarios deshabilitada");
        alert.setContentText("La creación de usuarios se maneja desde el Módulo de Usuarios principal. " +
                           "Use la interfaz principal del sistema para crear nuevos usuarios.");
        alert.showAndWait();
    }
    
    @FXML
    private void listarUsuarios() {
        actualizarInformacionUsuarios();
        mostrarMensajeExito("Lista de usuarios actualizada");
    }
    
    @FXML
    private void conectarUsuario() {
        try {
            String indiceTexto = txtIndiceUsuario.getText().trim();
            if (indiceTexto.isEmpty()) {
                mostrarMensajeError("Por favor ingrese el número del usuario a conectar");
                return;
            }

            int indice = Integer.parseInt(indiceTexto);
            
            // Obtener la lista de usuarios
            if (moduloUsuarios instanceof Comunidad_Modulo.integracion.ModuloUsuariosReal) {
                Comunidad_Modulo.integracion.ModuloUsuariosReal moduloReal = 
                    (Comunidad_Modulo.integracion.ModuloUsuariosReal) moduloUsuarios;
                
                List<UsuarioComunidad> todosUsuarios = moduloReal.obtenerTodosLosUsuarios();
                
                if (indice < 1 || indice > todosUsuarios.size()) {
                    mostrarMensajeError("Número de usuario inválido. Use un número entre 1 y " + todosUsuarios.size());
                    return;
                }
                
                UsuarioComunidad usuario = todosUsuarios.get(indice - 1);
                ContextoSistema contexto = ContextoSistema.getInstancia();
                
                // Verificar si hay una comunidad activa
                if (contexto.getComunidadActual() == null) {
                    mostrarMensajeError("No hay comunidad activa. Cree una comunidad primero en 'Gestionar Comunidad'");
                    return;
                }
                
                // Conectar usuario a la comunidad
                contexto.getComunidadActual().conectarUsuario(usuario);
                contexto.agregarUsuario(usuario);
                
                mostrarMensajeExito("Usuario '" + usuario.getNombre() + "' conectado a la comunidad '" + 
                                  contexto.getComunidadActual().getNombre() + "'");
                
                // Actualizar información
                actualizarInformacionUsuarios();
                
            } else {
                mostrarMensajeError("Funcionalidad no disponible con módulo simulado");
            }
            
        } catch (NumberFormatException e) {
            mostrarMensajeError("Por favor ingrese un número válido");
        } catch (Exception e) {
            mostrarMensajeError("Error al conectar usuario: " + e.getMessage());
        }
    }

    @FXML 
    private void desconectarUsuario() {
        try {
            String indiceTexto = txtIndiceUsuario.getText().trim();
            if (indiceTexto.isEmpty()) {
                mostrarMensajeError("Por favor ingrese el número del usuario a desconectar");
                return;
            }

            int indice = Integer.parseInt(indiceTexto);
            
            // Obtener la lista de usuarios
            if (moduloUsuarios instanceof Comunidad_Modulo.integracion.ModuloUsuariosReal) {
                Comunidad_Modulo.integracion.ModuloUsuariosReal moduloReal = 
                    (Comunidad_Modulo.integracion.ModuloUsuariosReal) moduloUsuarios;
                
                List<UsuarioComunidad> todosUsuarios = moduloReal.obtenerTodosLosUsuarios();
                
                if (indice < 1 || indice > todosUsuarios.size()) {
                    mostrarMensajeError("Número de usuario inválido. Use un número entre 1 y " + todosUsuarios.size());
                    return;
                }
                
                UsuarioComunidad usuario = todosUsuarios.get(indice - 1);
                ContextoSistema contexto = ContextoSistema.getInstancia();
                
                // Verificar si hay una comunidad activa
                if (contexto.getComunidadActual() == null) {
                    mostrarMensajeError("No hay comunidad activa para desconectar usuarios");
                    return;
                }
                
                // Desconectar usuario de la comunidad
                contexto.getComunidadActual().desconectarUsuario(usuario);
                
                mostrarMensajeExito("Usuario '" + usuario.getNombre() + "' desconectado de la comunidad '" + 
                                  contexto.getComunidadActual().getNombre() + "'");
                
                // Actualizar información
                actualizarInformacionUsuarios();
                
            } else {
                mostrarMensajeError("Funcionalidad no disponible con módulo simulado");
            }
            
        } catch (NumberFormatException e) {
            mostrarMensajeError("Por favor ingrese un número válido");
        } catch (Exception e) {
            mostrarMensajeError("Error al desconectar usuario: " + e.getMessage());
        }
    }    @FXML
    private void volver() {
        try {
            // Importar MetodosFrecuentes para cambiar ventana
            MetodosGlobales.MetodosFrecuentes.cambiarVentana(
                (javafx.stage.Stage) btnVolver.getScene().getWindow(),
                "/Modulo_Comunidad/Views/Comunidad.fxml",
                "Sistema de Comunidad"
            );
        } catch (Exception e) {
            mostrarMensajeError("Error al volver: " + e.getMessage());
        }
    }
    
    private void actualizarInformacionUsuarios() {
        try {
            ContextoSistema contexto = ContextoSistema.getInstancia();
            UsuarioComunidad usuarioActual = moduloUsuarios.obtenerUsuarioActual();
            
            StringBuilder info = new StringBuilder("=== INFORMACIÓN DEL SISTEMA ===\n\n");
            
            // Mostrar estadísticas del sistema
            info.append("📊 ESTADÍSTICAS:\n");
            info.append("• Total usuarios registrados: ").append(contexto.getTotalUsuarios()).append("\n");
            info.append("• Total comunidades: ").append(contexto.getTotalComunidades()).append("\n");
            info.append("• Total moderadores: ").append(contexto.getModeradores().size()).append("\n\n");
            
            // Información de comunidad activa
            if (contexto.getComunidadActual() != null) {
                info.append("🏘️ COMUNIDAD ACTIVA:\n");
                info.append("• Nombre: ").append(contexto.getComunidadActual().getNombre()).append("\n");
                info.append("• Descripción: ").append(contexto.getComunidadActual().getDescripcion()).append("\n");
                info.append("• Usuarios conectados: ").append(contexto.getComunidadActual().getUsuariosConectados().size()).append("\n\n");
            } else {
                info.append("⚠️ COMUNIDAD ACTIVA:\n");
                info.append("No hay comunidad activa.\n");
                info.append("Los usuarios no se pueden conectar sin una comunidad activa.\n\n");
            }
            
            // Información del usuario actual
            if (usuarioActual != null) {
                info.append("👤 USUARIO ACTUAL:\n");
                info.append("   • Nombre: ").append(usuarioActual.getNombre()).append("\n");
                info.append("   • Username: ").append(usuarioActual.getUsername()).append("\n");
                info.append("   • Email: ").append(usuarioActual.getEmail() != null && !usuarioActual.getEmail().isEmpty() ? 
                           usuarioActual.getEmail() : "No especificado").append("\n");
                info.append("   • Nivel Java: ").append(usuarioActual.getNivelJava()).append("\n");
                info.append("   • Reputación: ").append(usuarioActual.getReputacion()).append(" puntos\n");
                info.append("   • ID Usuario: ").append(usuarioActual.getIdUsuario()).append("\n\n");
            }
            
            info.append("💡 Consejo: Use los números de la lista para conectar/desconectar usuarios.");
            
            txtInformacion.setText(info.toString());
            
            // Mostrar lista detallada de usuarios con estado de conexión
            mostrarListaUsuariosConEstado(contexto);
            
        } catch (Exception e) {
            mostrarMensajeError("Error al cargar información del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra la lista de usuarios con su estado de conexión a la comunidad
     */
    private void mostrarListaUsuariosConEstado(ContextoSistema contexto) {
        try {
            StringBuilder listaUsuarios = new StringBuilder("📋 USUARIOS REGISTRADOS\n");
            listaUsuarios.append("========================\n\n");
            
            if (moduloUsuarios instanceof Comunidad_Modulo.integracion.ModuloUsuariosReal) {
                Comunidad_Modulo.integracion.ModuloUsuariosReal moduloReal = 
                    (Comunidad_Modulo.integracion.ModuloUsuariosReal) moduloUsuarios;
                
                List<UsuarioComunidad> todosUsuarios = moduloReal.obtenerTodosLosUsuarios();
                listaUsuarios.append("Total: ").append(todosUsuarios.size()).append(" usuario(s)\n\n");
                
                for (int i = 0; i < todosUsuarios.size(); i++) {
                    UsuarioComunidad usuario = todosUsuarios.get(i);
                    listaUsuarios.append(String.format("%d. Usuario: %s (Nivel: %s, Reputación: %d)\n", 
                                       (i + 1), 
                                       usuario.getNombre() != null ? usuario.getNombre() : usuario.getUsername(),
                                       usuario.getNivelJava(), 
                                       usuario.getReputacion()));
                    
                    listaUsuarios.append("   ID: ").append(usuario.getIdUsuario()).append("\n");
                    listaUsuarios.append("   Amigos: ").append(usuario.getAmigos().size()).append("\n");
                    
                    // Verificar estado de conexión a la comunidad
                    boolean estaConectado = false;
                    if (contexto.getComunidadActual() != null) {
                        estaConectado = contexto.getComunidadActual().getUsuariosConectados()
                                      .stream()
                                      .anyMatch(u -> u.getUsername().equals(usuario.getUsername()));
                    }
                    
                    listaUsuarios.append("   Estado: ");
                    if (estaConectado) {
                        listaUsuarios.append("✅ Conectado a '")
                                   .append(contexto.getComunidadActual().getNombre())
                                   .append("'");
                    } else {
                        listaUsuarios.append("❌ Desconectado");
                    }
                    listaUsuarios.append("\n\n");
                }
            } else {
                listaUsuarios.append("No hay información de usuarios disponible (módulo simulado)\n");
            }
            
            txtListaUsuarios.setText(listaUsuarios.toString());
            
        } catch (Exception e) {
            txtListaUsuarios.setText("Error al cargar lista de usuarios: " + e.getMessage());
        }
    }
    
    private void mostrarMensajeExito(String mensaje) {
        txtInformacion.setStyle("-fx-text-fill: green; -fx-background-color: #f0fff0; -fx-border-color: #90ee90;");
        txtInformacion.setText("✅ " + mensaje + "\n\n" + txtInformacion.getText());
        
        // Restaurar estilo después de 3 segundos
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), e -> {
                txtInformacion.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
            })
        );
        timeline.play();
    }
    
    private void mostrarMensajeError(String mensaje) {
        txtInformacion.setStyle("-fx-text-fill: red; -fx-background-color: #fff0f0; -fx-border-color: #ffcccc;");
        txtInformacion.setText("❌ " + mensaje);
        
        // Restaurar estilo después de 4 segundos
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(4), e -> {
                txtInformacion.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
                actualizarInformacionUsuarios();
            })
        );
        timeline.play();
    }
}
