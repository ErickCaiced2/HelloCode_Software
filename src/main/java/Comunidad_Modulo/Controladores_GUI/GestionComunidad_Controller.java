package Comunidad_Modulo.Controladores_GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

// Importar las clases del modelo y controladores
import Comunidad_Modulo.controladores.ContextoSistema;
import Comunidad_Modulo.modelo.Comunidad;
import Comunidad_Modulo.modelo.Moderador;
import Comunidad_Modulo.integracion.ModuloUsuariosSimulado;
import Comunidad_Modulo.integracion.IModuloUsuarios;
import MetodosGlobales.MetodosFrecuentes;

/**
 * Controlador para la gestión de comunidad en la interfaz JavaFX
 * Implementa los principios SOLID y se integra con los controladores existentes
 */
public class GestionComunidad_Controller implements Initializable {

    // Referencias FXML a los elementos de la interfaz
    @FXML private TextField txtNombreComunidad;
    @FXML private TextArea txtDescripcionComunidad;
    @FXML private Button btnCrearComunidad;

    @FXML private TextField txtNombreModerador;
    @FXML private Button btnAsignarModerador;

    @FXML private TextArea txtInformacion;
    @FXML private Button btnVerInformacion;
    @FXML private Button btnVolver;

    // Controladores de negocio reutilizados del sistema existente
    private ContextoSistema contexto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Inicializando controlador de Gestión de Comunidad...");

        // Inicializar los componentes del sistema
        inicializarSistema();

        // Configurar la interfaz
        configurarInterfaz();

        // Actualizar información inicial
        actualizarInformacion();

        System.out.println("✅ Controlador de Gestión de Comunidad inicializado correctamente");
    }

    /**
     * Inicializar el sistema con los componentes existentes
     */
    private void inicializarSistema() {
        // Obtener la instancia del contexto (singleton)
        this.contexto = ContextoSistema.getInstance();

        // Si no hay módulo de usuarios configurado, usar el simulado
        if (contexto.getModuloUsuarios() == null) {
            IModuloUsuarios moduloUsuarios = new ModuloUsuariosSimulado();
            contexto.setModuloUsuarios(moduloUsuarios);
        }
    }

    /**
     * Configurar elementos de la interfaz
     */
    private void configurarInterfaz() {
        // Configurar el área de información como solo lectura
        txtInformacion.setEditable(false);
        txtInformacion.setWrapText(true);

        // Configurar placeholder texts
        txtNombreComunidad.setPromptText("Ingrese el nombre de la comunidad");
        txtDescripcionComunidad.setPromptText("Describe la comunidad y sus objetivos");
        txtNombreModerador.setPromptText("Nombre del moderador");

        // Estilo para el área de información
        txtInformacion.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
    }

    /**
     * Método FXML para crear una nueva comunidad
     * Respeta Single Responsibility: Solo se encarga de crear comunidades
     */
    @FXML
    private void crearComunidad() {
        try {
            // Validar entrada de datos
            String nombre = txtNombreComunidad.getText().trim();
            String descripcion = txtDescripcionComunidad.getText().trim();

            if (nombre.isEmpty()) {
                mostrarError("Campo Requerido", "El nombre de la comunidad es obligatorio.");
                txtNombreComunidad.requestFocus();
                return;
            }

            if (descripcion.isEmpty()) {
                mostrarError("Campo Requerido", "La descripción de la comunidad es obligatoria.");
                txtDescripcionComunidad.requestFocus();
                return;
            }

            if (nombre.length() < 3) {
                mostrarError("Nombre Muy Corto", "El nombre debe tener al menos 3 caracteres.");
                txtNombreComunidad.requestFocus();
                return;
            }

            // Verificar si ya existe una comunidad con ese nombre
            boolean nombreExiste = contexto.getComunidades().stream()
                    .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));

            if (nombreExiste) {
                mostrarError("Nombre Duplicado", "Ya existe una comunidad con ese nombre.");
                txtNombreComunidad.requestFocus();
                return;
            }

            // Crear la comunidad usando el modelo existente
            Comunidad nuevaComunidad = new Comunidad(nombre, descripcion);
            contexto.agregarComunidad(nuevaComunidad);

            // Mostrar mensaje de confirmación
            mostrarExito("Comunidad Creada",
                    "✅ La comunidad '" + nombre + "' ha sido creada exitosamente.\n\n" +
                            "ID: " + nuevaComunidad.getIdComunidad() + "\n" +
                            "Moderador automático: " + nuevaComunidad.getModerador().getNombre());

            // Preguntar si desea establecer como comunidad activa
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Establecer como Activa");
            confirmacion.setHeaderText("¿Desea establecer esta comunidad como la activa?");
            confirmacion.setContentText("La comunidad activa es la que se usará por defecto en el sistema.");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                contexto.setComunidadActual(nuevaComunidad);
                mostrarInformacion("Comunidad Activa", "La comunidad '" + nombre + "' es ahora la comunidad activa.");
            }

            // Limpiar campos y actualizar información
            limpiarCamposComunidad();
            actualizarInformacion();

        } catch (Exception e) {
            mostrarError("Error al Crear Comunidad",
                    "Ocurrió un error inesperado al crear la comunidad:\n" + e.getMessage());
            System.err.println("Error en crearComunidad: " + e.getMessage());
        }
    }

    /**
     * Método FXML para asignar un moderador
     * Respeta Open/Closed Principle: Extensible sin modificar código existente
     */
    @FXML
    private void asignarModerador() {
        try {
            // Verificar que hay una comunidad activa
            if (!contexto.tieneComunidadActiva()) {
                mostrarError("Sin Comunidad Activa",
                        "Debe tener una comunidad activa para asignar un moderador.\n" +
                                "Cree una comunidad primero o seleccione una existente.");
                return;
            }

            // Validar entrada de datos
            String nombreModerador = txtNombreModerador.getText().trim();

            if (nombreModerador.isEmpty()) {
                mostrarError("Campo Requerido", "El nombre del moderador es obligatorio.");
                txtNombreModerador.requestFocus();
                return;
            }

            if (nombreModerador.length() < 2) {
                mostrarError("Nombre Muy Corto", "El nombre del moderador debe tener al menos 2 caracteres.");
                txtNombreModerador.requestFocus();
                return;
            }

            // Crear y asignar el moderador
            Moderador nuevoModerador = new Moderador(nombreModerador);
            contexto.agregarModerador(nuevoModerador);

            Comunidad comunidadActual = contexto.getComunidadActual();
            comunidadActual.setModerador(nuevoModerador);

            // Mostrar confirmación
            mostrarExito("Moderador Asignado",
                    "✅ El moderador '" + nombreModerador + "' ha sido asignado exitosamente.\n\n" +
                            "Comunidad: " + comunidadActual.getNombre() + "\n" +
                            "El moderador anterior ha sido reemplazado.");

            // Limpiar campo y actualizar información
            txtNombreModerador.clear();
            actualizarInformacion();

        } catch (Exception e) {
            mostrarError("Error al Asignar Moderador",
                    "Ocurrió un error al asignar el moderador:\n" + e.getMessage());
            System.err.println("Error en asignarModerador: " + e.getMessage());
        }
    }

    /**
     * Método FXML para ver información de la comunidad
     * Respeta Interface Segregation Principle: Interfaz específica para visualización
     */
    @FXML
    private void verInformacionComunidad() {
        try {
            actualizarInformacion();
            mostrarInformacion("Información Actualizada",
                    "La información de la comunidad ha sido actualizada en la pantalla.");
        } catch (Exception e) {
            mostrarError("Error al Obtener Información",
                    "No se pudo obtener la información de la comunidad:\n" + e.getMessage());
        }
    }

    /**
     * Método FXML para volver a la pantalla anterior
     */
    @FXML
    private void volver() {
        try {
            // Cambiar a la ventana de entrada principal
            MetodosFrecuentes.cambiarVentana(
                    (Stage) btnVolver.getScene().getWindow(),
                    "/Modulo_Comunidad/Views/Comunidad.fxml",
                    "Sistema de Comunidad"
            );
        } catch (Exception e) {
            mostrarError("Error de Navegación",
                    "No se pudo volver a la pantalla anterior:\n" + e.getMessage());
        }
    }

    /**
     * Actualizar la información mostrada en el área de texto
     * Respeta Single Responsibility: Solo se encarga de actualizar la visualización
     */
    private void actualizarInformacion() {
        StringBuilder info = new StringBuilder();
        info.append("=== ESTADO DEL SISTEMA DE COMUNIDAD ===\n\n");

        // Estadísticas generales
        info.append("📊 ESTADÍSTICAS GENERALES:\n");
        info.append("• Total de comunidades: ").append(contexto.getTotalComunidades()).append("\n");
        info.append("• Total de usuarios: ").append(contexto.getTotalUsuarios()).append("\n");
        info.append("• Total de moderadores: ").append(contexto.getModeradores().size()).append("\n\n");

        // Información de la comunidad activa
        if (contexto.tieneComunidadActiva()) {
            Comunidad comunidadActual = contexto.getComunidadActual();
            info.append("🏛️ COMUNIDAD ACTIVA:\n");
            info.append(comunidadActual.obtenerEstadisticas());
            info.append("\n\n");

            if (comunidadActual.getModerador() != null) {
                info.append("👤 MODERADOR ACTUAL:\n");
                info.append("• Nombre: ").append(comunidadActual.getModerador().getNombre()).append("\n");
                info.append("• Estado: ✅ Activo\n\n");
            }

        } else {
            info.append("⚠️ COMUNIDAD ACTIVA:\n");
            info.append("No hay comunidad activa seleccionada.\n");
            info.append("Cree una nueva comunidad para comenzar.\n\n");
        }

        // Lista de todas las comunidades
        if (!contexto.getComunidades().isEmpty()) {
            info.append("📋 TODAS LAS COMUNIDADES:\n");
            for (int i = 0; i < contexto.getComunidades().size(); i++) {
                Comunidad com = contexto.getComunidades().get(i);
                String marca = com == contexto.getComunidadActual() ? "🔸" : "🔹";
                info.append(String.format("%s %d. %s (%d usuarios)\n",
                        marca, i + 1, com.getNombre(), com.getUsuariosConectados().size()));
            }
        } else {
            info.append("📋 COMUNIDADES:\n");
            info.append("No hay comunidades creadas en el sistema.\n");
        }

        info.append("\n🌟 Sistema funcionando correctamente");

        txtInformacion.setText(info.toString());
    }

    /**
     * Limpiar los campos de creación de comunidad
     */
    private void limpiarCamposComunidad() {
        txtNombreComunidad.clear();
        txtDescripcionComunidad.clear();
    }

    // ========== MÉTODOS DE UTILIDAD PARA ALERTAS ==========

    private void mostrarExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText("✅ Operación Exitosa");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("❌ Error en el Sistema");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
