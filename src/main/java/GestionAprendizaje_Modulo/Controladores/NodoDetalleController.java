package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Leccion; // --> Importante añadir esta clase
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import javafx.event.ActionEvent; // --> Importante añadir esta clase
import javafx.fxml.FXML;
import javafx.scene.control.Alert; // --> Importante añadir esta clase
import javafx.scene.control.Button; // --> Importante añadir esta clase
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NodoDetalleController {

    @FXML private Label labelTituloLeccion;
    @FXML private Text textDescripcionLeccion;
    @FXML private VBox vboxRecursos;

    // --> AÑADIDO: Referencia al botón del FXML
    @FXML private Button btnIrALeccion;

    // --> AÑADIDO: Variable para guardar la lección actual
    private Leccion leccionActual;

    /**
     * Método público para "inyectar" el nodo cuyos detalles se mostrarán.
     * @param nodo El objeto NodoRuta que el usuario seleccionó.
     */
    public void setNodo(NodoRuta nodo) {
        if (nodo == null) return;

        // --> AÑADIDO: Guardamos la lección para usarla después en el botón
        this.leccionActual = nodo.getLeccion();

        // 1. Poblar la información de la Lección
        labelTituloLeccion.setText(leccionActual.getTitulo());
        textDescripcionLeccion.setText(leccionActual.getDescripcion());

        // 2. Poblar dinámicamente el material de apoyo
        vboxRecursos.getChildren().clear();
        if (nodo.getMaterialDeApoyo().isEmpty()) {
            vboxRecursos.getChildren().add(new Label("No hay material de apoyo para esta lección."));
        } else {
            for (RecursoAprendizaje recurso : nodo.getMaterialDeApoyo()) {
                VBox cardRecurso = new VBox(3);
                Label tituloRecurso = new Label(recurso.getTitulo());
                tituloRecurso.setStyle("-fx-font-weight: bold;");

                Text detalleRecurso = new Text(recurso.obtenerDetalle());
                detalleRecurso.setWrappingWidth(340);

                cardRecurso.getChildren().addAll(tituloRecurso, detalleRecurso);
                vboxRecursos.getChildren().add(cardRecurso);
            }
        }
    }

    /**
     * --- MÉTODO NUEVO: Se ejecuta cuando el usuario hace clic en el botón "Ir a la Lección" ---
     * @param event El evento del clic.
     */
    @FXML
    void handleIrALeccion(ActionEvent event) {
        if (this.leccionActual != null) {

            // --- SIMULACIÓN ---
            // Por ahora, mostramos una alerta para confirmar que funciona.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Navegación a Módulo Externo");
            alert.setHeaderText("Abriendo la lección: " + leccionActual.getTitulo());
            alert.setContentText("En este punto, la aplicación cargaría la vista de ejercicios correspondiente a esta lección, que está siendo desarrollada por otro equipo.");
            alert.showAndWait();

            // --- CÓDIGO FINAL (PARA CUANDO EL OTRO EQUIPO TERMINE) ---
            /*
             * Cuando tu compañero termine su vista (por ejemplo, "LeccionView.fxml"),
             * reemplazarías la alerta de arriba con un código como este:
             *
             * try {
             *     FXMLLoader loader = new FXMLLoader(getClass().getResource("/ruta/a/la/vista/LeccionView.fxml"));
             *     Parent root = loader.load();
             *
             *     // Si necesitas pasarle la lección al controlador de la otra vista:
             *     LeccionViewController controller = loader.getController();
             *     controller.setLeccion(this.leccionActual);
             *
             *     Stage stage = new Stage();
             *     stage.setTitle("Lección: " + this.leccionActual.getTitulo());
             *     stage.setScene(new Scene(root));
             *     stage.show();
             *
             * } catch (IOException e) {
             *     e.printStackTrace();
             * }
             */

        } else {
            new Alert(Alert.AlertType.ERROR, "No se ha podido cargar la información de la lección.").show();
        }
    }
}