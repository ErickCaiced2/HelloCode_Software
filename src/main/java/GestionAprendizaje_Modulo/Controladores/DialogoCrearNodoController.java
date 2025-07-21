package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Repositorio.RepositorioExterno; // Necesitarás este repo
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class DialogoCrearNodoController {

    // Repositorio externo para jalar las lecciones
    private final RepositorioExterno repoExterno = RepositorioExterno.getInstancia();

    @FXML private ComboBox<Leccion> comboLecciones;
    @FXML private ListView<RecursoAprendizaje> listRecursosAñadidos;
    @FXML private ComboBox<String> comboTipoRecurso;
    @FXML private TextField txtTituloRecurso;
    @FXML private TextField txtUrlRecurso;
    @FXML private TextField txtDatoExtraRecurso;

    private Ruta rutaActual;
    private ObservableList<RecursoAprendizaje> recursosTemporales = FXCollections.observableArrayList();

    public void setRuta(Ruta ruta) {
        this.rutaActual = ruta;
    }

    @FXML
    public void initialize() {
        // Cargar lecciones disponibles
        comboLecciones.setItems(FXCollections.observableArrayList(repoExterno.getTodasLasLecciones()));
        comboLecciones.setConverter(new StringConverter<Leccion>() {
            @Override public String toString(Leccion leccion) { return leccion == null ? "" : leccion.getTitulo(); }
            @Override public Leccion fromString(String string) { return null; }
        });

        // Configurar lista de recursos
        listRecursosAñadidos.setItems(recursosTemporales);

        // Configurar tipos de recursos
        comboTipoRecurso.setItems(FXCollections.observableArrayList("Video", "PDF", "Artículo"));
    }

    @FXML
    void handleAgregarRecurso(ActionEvent event) {
        String tipo = comboTipoRecurso.getValue();
        String titulo = txtTituloRecurso.getText();
        String url = txtUrlRecurso.getText();
        String datoExtra = txtDatoExtraRecurso.getText();

        if (tipo == null || titulo.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Debe seleccionar un tipo y escribir un título.").show();
            return;
        }

        RecursoAprendizaje nuevoRecurso = null;
        try {
            switch (tipo) {
                case "Video": nuevoRecurso = new Video(titulo, url, Integer.parseInt(datoExtra)); break;
                case "PDF": nuevoRecurso = new DocumentoPDF(titulo, url, Integer.parseInt(datoExtra)); break;
                case "Artículo": nuevoRecurso = new Articulo(titulo, url); break;
            }
            if (nuevoRecurso != null) {
                recursosTemporales.add(nuevoRecurso);
                limpiarCamposRecurso();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El dato extra debe ser un número para Videos y PDFs.").show();
        }
    }

    @FXML
    void handleGuardarNodo(ActionEvent event) {
        Leccion leccionSeleccionada = comboLecciones.getSelectionModel().getSelectedItem();
        if (leccionSeleccionada == null) {
            new Alert(Alert.AlertType.ERROR, "Debe seleccionar una lección para el nodo.").show();
            return;
        }

        // Crear el nuevo nodo
        int nuevoOrden = rutaActual.getNodos().size() + 1;
        NodoRuta nuevoNodo = new NodoRuta(nuevoOrden, leccionSeleccionada);

        // Añadirle todos los recursos de la lista temporal
        for (RecursoAprendizaje recurso : recursosTemporales) {
            nuevoNodo.agregarMaterialDeApoyo(recurso);
        }
        if (rutaActual == null) {
            rutaActual = new Ruta("1","RutaNueva","Ruta nueva para el ejemplo");
        }

        // Añadir el nodo a la ruta
        rutaActual.agregarNodo(nuevoNodo);

        // Cerrar la ventana
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void handleCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void limpiarCamposRecurso() {
        comboTipoRecurso.getSelectionModel().clearSelection();
        txtTituloRecurso.clear();
        txtUrlRecurso.clear();
        txtDatoExtraRecurso.clear();
    }
}
