package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Repositorio.RepositorioEnMemoria; // <-- Importa el repositorio
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import MetodosGlobales.MetodosFrecuentes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class AdminRutaVisualController {

    @FXML private Button buttonAgregar;
    @FXML private Text labelNameRuta;
    @FXML private VBox pathContainer;

    // --- LA CORRECCIÓN CLAVE PARA EL ERROR DEL ADMIN ---
    private final RepositorioEnMemoria repositorio = RepositorioEnMemoria.getInstancia();
    private Ruta rutaActual;

    @FXML
    public void initialize() {
        this.rutaActual = obtenerOcrearRutaDePruebaEnRepositorio();
        actualizarVista();
    }

    private Ruta obtenerOcrearRutaDePruebaEnRepositorio() {
        if (!repositorio.getRutas().isEmpty()) {
            return repositorio.getRutas().get(0);
        } else {
            Ruta nuevaRuta = new Ruta(UUID.randomUUID().toString(), "Ruta de Java Inicial", "Creada por Admin");
            Leccion leccionInicial = new Leccion("L-INIT", "¡Bienvenido!", "...", "PUBLICADO");
            nuevaRuta.agregarNodo(new NodoRuta(1, leccionInicial));
            repositorio.getRutas().add(nuevaRuta);
            return nuevaRuta;
        }
    }

    private void actualizarVista() {
        if (rutaActual == null) return;
        labelNameRuta.setText("EDITAR RUTA: " + rutaActual.getNombre());
        pathContainer.getChildren().clear();
        pathContainer.setAlignment(Pos.TOP_CENTER);
        pathContainer.setSpacing(15.0);
        for (NodoRuta nodo : rutaActual.getNodos()) {
            pathContainer.getChildren().add(crearComponenteVisualNodo(nodo));
        }
    }

    @FXML
    void añadirNodos(ActionEvent event) {
        if (rutaActual == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/DialogoCrearNodo.fxml"));
            Parent root = loader.load();
            DialogoCrearNodoController controllerDialogo = loader.getController();
            controllerDialogo.setRuta(this.rutaActual);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir Nuevo Nodo");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            actualizarVista();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox crearComponenteVisualNodo(NodoRuta nodo) {
        HBox nodoBox = new HBox(10);
        nodoBox.setAlignment(Pos.CENTER_LEFT);
        nodoBox.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10; -fx-background-radius: 10;");
        Circle circulo = new Circle(15, Color.STEELBLUE);
        Text textoOrden = new Text(String.valueOf(nodo.getOrden()));
        textoOrden.setFont(Font.font("System", 12));
        textoOrden.setFill(Color.WHITE);
        javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(circulo, textoOrden);
        Text textoLeccion = new Text(nodo.getLeccion().getTitulo());
        textoLeccion.setFont(Font.font("System", 14));
        nodoBox.getChildren().addAll(stack, textoLeccion);
        return nodoBox;
    }
    @FXML
    private Button buttonAtras;
    @FXML
    private void regresarPrincipal(){
        System.out.println("Regreso a contenido");
        MetodosFrecuentes.cambiarVentana((Stage) buttonAtras.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml","ROL");
    }
}