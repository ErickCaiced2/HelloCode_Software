package Gamificacion_Modulo.GUI;

import java.util.List;

import Gamificacion_Modulo.Desafio;
import Gamificacion_Modulo.DesafioMensual;
import Gamificacion_Modulo.DesafioSemanal;
import Gamificacion_Modulo.Main;
import Gamificacion_Modulo.ProgresoEstudiante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DetalleDesafioController {

    @FXML
    private Label lblTitulo;

    @FXML
    private ImageView imgEllipse;

    @FXML
    private Label lblSemanal1, lblSemanal2, lblSemanal3;

    @FXML
    private Label lblProgresoSemanal1, lblProgresoSemanal2, lblProgresoSemanal3;

    @FXML
    private Label lblMensual1, lblMensual2, lblMensual3;

    @FXML
    private Label lblProgresoMensual1, lblProgresoMensual2, lblProgresoMensual3;

    @FXML
    private ImageView iconHome, iconProfile, iconRanking;

    private ProgresoEstudiante progresoActual;

    @FXML
    public void initialize() {
        cargarDatosDesafios();
    }

    private void cargarDatosDesafios() {
        List<ProgresoEstudiante> progresos = Main.getProgresos();

        if (!progresos.isEmpty()) {
            progresoActual = progresos.size() > 1 ? progresos.get(1) : progresos.get(0);

            // Cargar desafíos semanales
            List<Desafio> desafiosActivos = progresoActual.getDesafiosActivos();
            int semanalCount = 0;
            int mensualCount = 0;

            for (Desafio desafio : desafiosActivos) {
                if (desafio instanceof DesafioSemanal && semanalCount < 3) {
                    DesafioSemanal ds = (DesafioSemanal) desafio;
                    String progreso = ds.getActividadesCompletadas() + "/" + ds.getMetaSemanal();
                    switch (semanalCount) {
                        case 0:
                            lblSemanal1.setText(desafio.getNombre());
                            lblProgresoSemanal1.setText(progreso);
                            break;
                        case 1:
                            lblSemanal2.setText(desafio.getNombre());
                            lblProgresoSemanal2.setText(progreso);
                            break;
                        case 2:
                            lblSemanal3.setText(desafio.getNombre());
                            lblProgresoSemanal3.setText(progreso);
                            break;
                    }
                    semanalCount++;
                } else if (desafio instanceof DesafioMensual && mensualCount < 3) {
                    DesafioMensual dm = (DesafioMensual) desafio;
                    String progreso = dm.getActividadesCompletadas() + "/" + dm.getObjetivoMensual();
                    switch (mensualCount) {
                        case 0:
                            lblMensual1.setText(desafio.getNombre());
                            lblProgresoMensual1.setText(progreso);
                            break;
                        case 1:
                            lblMensual2.setText(desafio.getNombre());
                            lblProgresoMensual2.setText(progreso);
                            break;
                        case 2:
                            lblMensual3.setText(desafio.getNombre());
                            lblProgresoMensual3.setText(progreso);
                            break;
                    }
                    mensualCount++;
                }
            }

            System.out.println(">>> Datos de desafíos cargados para: " + progresoActual.getUsuario().getNombre());
        } else {
            // Datos por defecto
            lblSemanal1.setText("Completa 1 actividad");
            lblProgresoSemanal1.setText("0/1");
            lblSemanal2.setText("Completa 2 actividades");
            lblProgresoSemanal2.setText("1/2");
            lblSemanal3.setText("Completa 3 actividades");
            lblProgresoSemanal3.setText("0/3");

            lblMensual1.setText("Completa 1 actividad");
            lblProgresoMensual1.setText("1/2");
            lblMensual2.setText("Completa 2 actividades");
            lblProgresoMensual2.setText("1/2");
            lblMensual3.setText("Completa 3 actividades");
            lblProgresoMensual3.setText("3/3");
        }
    }

    @FXML
    private void onHomeClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gamificacion_Modulo/GUI/MainGamificacion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 360, 720);
            Stage stage = (Stage) iconHome.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sistema de Gamificación - HelloCode");
            stage.show();
            System.out.println(">>> Navegando a Home desde Desafíos");
        } catch (Exception e) {
            System.err.println("Error al navegar a Home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onProfileClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gamificacion_Modulo/GUI/PerfilUsuario.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 393, 852);
            Stage stage = (Stage) iconProfile.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Perfil - Sistema de Gamificación");
            stage.show();
            System.out.println(">>> Navegando a Perfil desde Desafíos");
        } catch (Exception e) {
            System.err.println("Error al navegar a Perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onRankingClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gamificacion_Modulo/GUI/Ranking.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 393, 852);
            Stage stage = (Stage) iconRanking.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ranking - Sistema de Gamificación");
            stage.show();
            System.out.println(">>> Navegando a Ranking desde Desafíos");
        } catch (Exception e) {
            System.err.println("Error al navegar a Ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarDatos() {
        cargarDatosDesafios();
    }
}
