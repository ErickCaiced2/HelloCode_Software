package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.RepositorioEnMemoria;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import MetodosGlobales.MetodosFrecuentes;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public class StudentDashboardController {

    private final RepositorioEnMemoria repositorio = RepositorioEnMemoria.getInstancia();

    @FXML private ComboBox<Curso> comboCursos;
    @FXML private ComboBox<Ruta> comboRutas;
    @FXML private Label labelNombreRuta;
    @FXML private VBox pathContainerEstudiante; // El contenedor para el camino gráfico

    @FXML
    public void initialize() {
        configurarComboBoxes();
        cargarCursosIniciales();
    }

    private void configurarComboBoxes() {
        comboCursos.setConverter(new StringConverter<>() {
            @Override public String toString(Curso c) { return c == null ? "" : c.getNombre(); }
            @Override public Curso fromString(String s) { return null; }
        });
        comboRutas.setConverter(new StringConverter<>() {
            @Override public String toString(Ruta r) { return r == null ? "" : r.getNombre(); }
            @Override public Ruta fromString(String s) { return null; }
        });
    }

    private void cargarCursosIniciales() {
        comboCursos.setItems(FXCollections.observableArrayList(repositorio.getCursos().values()));
    }

    @FXML
    void onCursoSeleccionado(ActionEvent event) {
        Curso curso = comboCursos.getSelectionModel().getSelectedItem();
        comboRutas.getItems().clear();
        pathContainerEstudiante.getChildren().clear();
        labelNombreRuta.setText("Selecciona una ruta para ver el camino");
        if (curso != null) {
            comboRutas.setItems(FXCollections.observableArrayList(curso.getRutas()));
        }
    }

    @FXML
    void onRutaSeleccionada(ActionEvent event) {
        Ruta ruta = comboRutas.getSelectionModel().getSelectedItem();
        renderizarRutaEstudiante(ruta);
    }

    private void renderizarRutaEstudiante(Ruta ruta) {
        pathContainerEstudiante.getChildren().clear();
        if (ruta == null) {
            labelNombreRuta.setText("Selecciona una ruta para ver el camino");
            return;
        }

        labelNombreRuta.setText("Tu Camino: " + ruta.getNombre());
        for (NodoRuta nodo : ruta.getNodos()) {
            // Creamos el componente visual y le añadimos el evento de clic
            HBox nodoVisual = crearComponenteVisualNodo(nodo);
            nodoVisual.setOnMouseClicked(mouseEvent -> {
                handleNodoClick(nodo);
            });
            pathContainerEstudiante.getChildren().add(nodoVisual);
        }
    }

    // --- LÓGICA DE INTERACCIÓN ---

    private void handleNodoClick(NodoRuta nodo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/NodoDetalle.fxml"));
            Parent root = loader.load();

            // Obtenemos el controlador de la nueva ventana
            NodoDetalleController controller = loader.getController();

            // Le pasamos el nodo en el que se hizo clic
            controller.setNodo(nodo);

            // Marcamos el nodo como completado y refrescamos la vista principal
            nodo.marcarComoCompletado();
            renderizarRutaEstudiante(comboRutas.getSelectionModel().getSelectedItem()); // Redibujar para mostrar el tick

            // Mostramos la ventana de detalles
            Stage stage = new Stage();
            stage.setTitle("Detalle de la Lección: " + nodo.getLeccion().getTitulo());
            stage.initModality(Modality.WINDOW_MODAL); // Modal, pero no bloquea toda la app
            stage.initOwner(pathContainerEstudiante.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODO REUTILIZADO DEL ADMIN PARA DIBUJAR LOS NODOS ---
    private HBox crearComponenteVisualNodo(NodoRuta nodo) {
        HBox nodoBox = new HBox(10);
        nodoBox.setAlignment(Pos.CENTER_LEFT);
        nodoBox.setStyle("-fx-background-color: #E8E8E8; -fx-padding: 10; -fx-background-radius: 10; -fx-cursor: hand;");

        String color = nodo.estaCompletado() ? "#4CAF50" : "#2196F3"; // Verde si está completado, azul si no
        Circle circulo = new Circle(15, Color.web(color));

        Text textoOrden = new Text(nodo.estaCompletado() ? "✔" : String.valueOf(nodo.getOrden()));
        textoOrden.setFont(Font.font("System", 12));
        textoOrden.setFill(Color.WHITE);

        javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(circulo, textoOrden);
        Text textoLeccion = new Text(nodo.getLeccion().getTitulo());
        textoLeccion.setFont(Font.font("System", 14));

        nodoBox.getChildren().addAll(stack, textoLeccion);
        return nodoBox;
    }
    @FXML
    private Button buttonAtrasE;
    @FXML
    private void regresarPrincipalE(){
        System.out.println("Regreso a contenido");
        MetodosFrecuentes.cambiarVentana((Stage) buttonAtrasE.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml","ROL");
    }
}