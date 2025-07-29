package GestorEjercicios.Controllers;

import GestorEjercicios.model.GestorLecciones;
import GestorEjercicios.model.GestorLeccionesBasadoEnArchivos;
import GestorEjercicios.strategy.FabricaEstrategiasLeccion;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.exercises.Lenguaje;
import Modulo_Ejercicios.exercises.NivelDificultad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LeccionController {
    @FXML private Button btnPython;
    @FXML private Button btnJava;
    @FXML private Button btnCpp;
    @FXML private Button btnPhp;
    @FXML private Button btnBasico;
    @FXML private Button btnIntermedio;
    @FXML private Button btnAvanzado;
    @FXML private Button btnContinuar;
    @FXML private ComboBox<String> comboTipoLeccion;

    private Lenguaje lenguajeSeleccionado;
    private NivelDificultad dificultadSeleccionada;

    // Instancia del GestorLecciones
    private GestorLecciones gestorLecciones;

    @FXML
    public void initialize() {
        // Inicializar el gestor de lecciones
        gestorLecciones = new GestorLecciones();

        configurarBotonesLenguaje();
        configurarBotonesDificultad();
        configurarComboBoxTipoLeccion();
        configurarBotonContinuar();
    }

    private void configurarBotonesLenguaje() {
        btnPython.setOnAction(_ -> seleccionarLenguaje(Lenguaje.PYTHON));
        btnJava.setOnAction(_ -> seleccionarLenguaje(Lenguaje.JAVA));
        if (btnCpp != null) btnCpp.setOnAction(_ -> seleccionarLenguaje(Lenguaje.C));
        if (btnPhp != null) btnPhp.setOnAction(_ -> seleccionarLenguaje(Lenguaje.PHP));
    }

    private void configurarBotonContinuar() {
        btnContinuar.setOnAction(_ -> crearYMostrarLeccion());
    }

    private void configurarBotonesDificultad() {
        btnBasico.setOnAction(_ -> seleccionarDificultad(NivelDificultad.BASICO));
        btnIntermedio.setOnAction(_ -> seleccionarDificultad(NivelDificultad.INTERMEDIO));
        btnAvanzado.setOnAction(_ -> seleccionarDificultad(NivelDificultad.AVANZADO));
    }

    private void configurarComboBoxTipoLeccion() {
        comboTipoLeccion.getItems().addAll("Normal", "Diagnóstico", "Prueba");
        comboTipoLeccion.setOnAction(_ -> seleccionarTipoLeccion());
    }

    private void seleccionarTipoLeccion() {
        String tipoSeleccionado = comboTipoLeccion.getValue();
        TipoLeccion tipoLeccion = null;

        switch (tipoSeleccionado) {
            case "Normal":
                tipoLeccion = TipoLeccion.NORMAL;
                break;
            case "Diagnóstico":
                tipoLeccion = TipoLeccion.DIAGNOSTICO;
                break;
            case "Prueba":
                tipoLeccion = TipoLeccion.PRUEBA;
                break;
            default:
                System.out.println("Tipo de lección no válido");
                return;
        }

        // Crear la estrategia correspondiente usando la fábrica
        // La estrategia se aplicará automáticamente en la lección
        FabricaEstrategiasLeccion.crearEstrategia(tipoLeccion);

        // Obtener los ejercicios de la base de datos (List<EjercicioSeleccion>)
        List<EjercicioSeleccion> ejercicios = EjercicioRepository.cargarEjerciciosSeleccion().stream()
                .filter(e -> e.getLenguaje() == lenguajeSeleccionado && e.getNivel() == dificultadSeleccionada)
                .collect(Collectors.toList());

        // Crear la lección con la lista de EjercicioSeleccion y valores de experiencia/conocimiento
        int experiencia = 0, conocimiento = 0;
        switch (tipoLeccion) {
            case NORMAL:
                experiencia = 15;
                conocimiento = 5;
                break;
            case PRUEBA:
                experiencia = 30;
                conocimiento = 0;
                break;
            case DIAGNOSTICO:
                experiencia = 0;
                conocimiento = 0; // Se calcula al finalizar
                break;
        }
        Leccion leccion = new Leccion((int) (Math.random() * 1000), "Lección de " + tipoSeleccionado + " - " + dificultadSeleccionada + " - " + lenguajeSeleccionado, ejercicios, tipoLeccion, experiencia, conocimiento);

        // Agregar la lección al gestor
        gestorLecciones.agregarLeccion(leccion);

        // Imprimir todas las lecciones para verificar que la lección se agregó correctamente
        gestorLecciones.imprimirLecciones();
        // Mostrar la lección
        mostrarLeccion(leccion);
    }

    private void seleccionarLenguaje(Lenguaje lenguaje) {
        this.lenguajeSeleccionado = lenguaje;
        resetearEstilosBotonesLenguaje();
        resaltarBotonSeleccionado(lenguaje);
    }

    private void seleccionarDificultad(NivelDificultad dificultad) {
        this.dificultadSeleccionada = dificultad;
        resetearEstilosBotonesDificultad();
        resaltarBotonDificultadSeleccionado(dificultad);
    }

    private void resetearEstilosBotonesLenguaje() {
        btnPython.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnJava.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        if (btnCpp != null) btnCpp.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        if (btnPhp != null) btnPhp.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
    }

    private void resetearEstilosBotonesDificultad() {
        btnBasico.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnIntermedio.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnAvanzado.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
    }

    private void resaltarBotonSeleccionado(Lenguaje lenguaje) {
        String estiloSeleccionado = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;";
        switch (lenguaje) {
            case PYTHON:
                btnPython.setStyle(estiloSeleccionado);
                break;
            case JAVA:
                btnJava.setStyle(estiloSeleccionado);
                break;
            case C:
                if (btnCpp != null) btnCpp.setStyle(estiloSeleccionado);
                break;
            case PHP:
                if (btnPhp != null) btnPhp.setStyle(estiloSeleccionado);
                break;
        }
    }

    private void resaltarBotonDificultadSeleccionado(NivelDificultad dificultad) {
        String estiloSeleccionado = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;";
        switch (dificultad) {
            case PRINCIPIANTE:
            case BASICO:
                btnBasico.setStyle(estiloSeleccionado);
                break;
            case INTERMEDIO:
                btnIntermedio.setStyle(estiloSeleccionado);
                break;
            case EXPERTO:
            case AVANZADO:
                btnAvanzado.setStyle(estiloSeleccionado);
                break;
        }
    }

    private void mostrarLeccion(Leccion leccion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml"));
            Parent root = loader.load();
            Modulo_Ejercicios.Controladores.EjercicioSeleccionController controller = loader.getController();
            // Usar el método correcto para obtener ejercicios de la lección
            // Necesitamos extraer los ejercicios originales de los adaptadores
            List<EjercicioSeleccion> ejerciciosSeleccion = new ArrayList<>();
            for (GestorEjercicios.adaptadores.AdaptadorEjercicios adaptador : leccion.getEjercicios()) {
                if (adaptador instanceof GestorEjercicios.adaptadores.AdaptadorEjercicioSeleccion) {
                    // Cast seguro y extraer el ejercicio original
                    ejerciciosSeleccion.add(((GestorEjercicios.adaptadores.AdaptadorEjercicioSeleccion) adaptador).obtenerEjercicioOriginal());
                }
            }
            controller.setEjercicios(ejerciciosSeleccion);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ejercicios Selección Múltiple");
            stage.show();
            cerrarVentanaActual();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual() {
        Stage currentStage = (Stage) btnContinuar.getScene().getWindow();
        currentStage.close();
    }

    public void configurarLeccionConValores(Lenguaje lenguaje, NivelDificultad nivel) {
        this.lenguajeSeleccionado = lenguaje;
        this.dificultadSeleccionada = nivel;
        actualizarEstilosBotones();
        crearYMostrarLeccion();
    }
    
    /**
     * 🎯 NUEVO: Configura una lección usando ejercicios desde archivos TXT
     * Este método usa el sistema basado en archivos TXT con ejercicios reales
     */
    public void configurarLeccionDesdeTXT(
            String lenguajeTexto,
            String nivelTexto) {
        
        // Convertir strings a enums
        Lenguaje lenguaje = convertirStringALenguaje(lenguajeTexto);
        NivelDificultad nivel = convertirStringANivel(nivelTexto);
        
        this.lenguajeSeleccionado = lenguaje;
        this.dificultadSeleccionada = nivel;
        
        System.out.println("🎯 Configurando lección desde archivos TXT");
        System.out.println("   📚 Lenguaje: " + lenguaje);
        System.out.println("   📊 Nivel: " + nivel);
        
        // Actualizar estilos visuales
        actualizarEstilosBotones();
        
        // Crear lección con ejercicios desde TXT
        crearLeccionDesdeTXT(lenguajeTexto, nivelTexto);
    }
    
    /**
     * 🎲 Crea una lección usando ejercicios desde archivos TXT
     */
    private void crearLeccionDesdeTXT(String lenguaje, String nivel) {
        try {
            // 🆕 Obtener lección aleatoria desde archivos TXT
            GestorLeccionesBasadoEnArchivos.LeccionCompleta leccionTXT = 
                GestorLeccionesBasadoEnArchivos.obtenerLeccionAleatoria(lenguaje.toUpperCase(), nivel.toUpperCase());
            
            if (leccionTXT == null) {
                System.out.println("⚠️ No se encontró lección en TXT para " + lenguaje + " - " + nivel);
                crearYMostrarLeccion(); // Fallback al método original
                return;
            }
            
            System.out.println("✅ Lección TXT cargada: " + leccionTXT.titulo);
            System.out.println("   🎮 Ejercicios: " + leccionTXT.ejercicios.size());
            System.out.println("   📝 Descripción: " + leccionTXT.descripcion);
            
            // Usar ejercicios reales del sistema TXT
            mostrarEjerciciosDesdeTXT(leccionTXT.ejercicios, leccionTXT.titulo, leccionTXT.descripcion);
            
        } catch (Exception e) {
            System.err.println("❌ Error creando lección desde TXT: " + e.getMessage());
            e.printStackTrace();
            // Fallback al método original
            crearYMostrarLeccion();
        }
    }
    
    /**
     * 🔧 Convierte string de lenguaje a enum
     */
    private Lenguaje convertirStringALenguaje(String lenguajeTexto) {
        switch (lenguajeTexto.toUpperCase()) {
            case "JAVA": return Lenguaje.JAVA;
            case "PYTHON": return Lenguaje.PYTHON;
            case "C": case "CPP": return Lenguaje.C;
            case "PHP": return Lenguaje.PHP;
            default: 
                System.out.println("⚠️ Lenguaje no reconocido: " + lenguajeTexto + ", usando JAVA por defecto");
                return Lenguaje.JAVA;
        }
    }
    
    /**
     * 🔧 Convierte string de nivel a enum
     */
    private NivelDificultad convertirStringANivel(String nivelTexto) {
        switch (nivelTexto.toUpperCase()) {
            case "BASICO": case "PRINCIPIANTE": return NivelDificultad.BASICO;
            case "INTERMEDIO": return NivelDificultad.INTERMEDIO;
            case "AVANZADO": case "EXPERTO": return NivelDificultad.AVANZADO;
            default:
                System.out.println("⚠️ Nivel no reconocido: " + nivelTexto + ", usando BASICO por defecto");
                return NivelDificultad.BASICO;
        }
    }
    
    // Método para continuar y crear la lección
    private void crearYMostrarLeccion() {
        if (lenguajeSeleccionado == null || dificultadSeleccionada == null) {
            System.out.println("Por favor selecciona lenguaje y dificultad.");
            return;
        }

        List<EjercicioSeleccion> ejerciciosSeleccion = EjercicioRepository.cargarEjerciciosSeleccion().stream()
                .filter(e -> e.getLenguaje() == lenguajeSeleccionado && e.getNivel() == dificultadSeleccionada)
                .collect(Collectors.toList());

        if (ejerciciosSeleccion.isEmpty()) {
            System.out.println("No hay ejercicios de selección múltiple para los criterios seleccionados.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml"));
            Parent root = loader.load();
            Modulo_Ejercicios.Controladores.EjercicioSeleccionController controller = loader.getController();
            controller.setEjercicios(ejerciciosSeleccion);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ejercicios Selección Múltiple");
            stage.show();
            cerrarVentanaActual();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void actualizarEstilosBotones() {
        limpiarEstilosBotones();
        if (lenguajeSeleccionado != null) {
            resaltarBotonSeleccionado(lenguajeSeleccionado);
        }
        if (dificultadSeleccionada != null) {
            resaltarBotonDificultadSeleccionado(dificultadSeleccionada);
        }
    }

    private void limpiarEstilosBotones() {
        resetearEstilosBotonesLenguaje();
        resetearEstilosBotonesDificultad();
    }
    
    /**
     * 🎯 NUEVO: Configura una lección con ejercicios específicos desde archivos TXT
     * Este método permite usar ejercicios reales del Modulo_Ejercicios
     */
    public void configurarLeccionConEjercicios(
            List<Object> ejercicios,
            Lenguaje lenguaje,
            NivelDificultad nivel,
            String titulo,
            String descripcion) {
        
        this.lenguajeSeleccionado = lenguaje;
        this.dificultadSeleccionada = nivel;
        
        System.out.println("🎯 Configurando lección con ejercicios reales: " + titulo);
        System.out.println("   📚 Lenguaje: " + lenguaje);
        System.out.println("   📊 Nivel: " + nivel);
        System.out.println("   🎲 Ejercicios cargados: " + ejercicios.size());
        
        // Actualizar estilos visuales
        actualizarEstilosBotones();
        
        // Crear lección con ejercicios específicos
        crearLeccionConEjerciciosEspecificosYGuardarProgreso(ejercicios, titulo, descripcion);
    }
    
    /**
     * 🎲 Crea una lección usando ejercicios específicos desde archivos TXT y guarda progreso
     */
    private void crearLeccionConEjerciciosEspecificosYGuardarProgreso(List<Object> ejercicios, String titulo, String descripcion) {
        try {
            // Convertir ejercicios Object a la lista de ejercicios de selección
            List<EjercicioSeleccion> ejerciciosSeleccion = new ArrayList<>();
            for (Object ejercicio : ejercicios) {
                if (ejercicio instanceof EjercicioSeleccion) {
                    ejerciciosSeleccion.add((EjercicioSeleccion) ejercicio);
                }
            }
            
            if (ejerciciosSeleccion.isEmpty()) {
                System.out.println("⚠️ No se encontraron ejercicios válidos, usando método original");
                crearYMostrarLeccion();
                return;
            }
            
            System.out.println("✅ Iniciando lección directamente con ejercicios reales");
            
            // CAMBIO: Ir directamente a los ejercicios sin crear Leccion intermedia
            mostrarEjerciciosDirectamenteInterno(ejerciciosSeleccion, titulo, descripcion);
            
        } catch (Exception e) {
            System.err.println("❌ Error creando lección con ejercicios específicos: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: usar método original
            crearYMostrarLeccion();
        }
    }
    
    /**
     * 🎮 Muestra ejercicios desde archivos TXT y configura callback para guardar progreso
     * Versión específica para ejercicios desde archivos TXT (List<Object>)
     */
    private void mostrarEjerciciosDesdeTXT(List<Object> ejerciciosObject, String titulo, String descripcion) {
        // Convertir List<Object> a List<EjercicioSeleccion>
        List<EjercicioSeleccion> ejerciciosSeleccion = new ArrayList<>();
        for (Object obj : ejerciciosObject) {
            if (obj instanceof EjercicioSeleccion) {
                ejerciciosSeleccion.add((EjercicioSeleccion) obj);
            }
        }
        
        if (ejerciciosSeleccion.isEmpty()) {
            System.out.println("⚠️ No se encontraron ejercicios válidos en la lista de objetos desde TXT");
            crearYMostrarLeccion(); // Fallback
            return;
        }
        
        System.out.println("✅ Convertidos " + ejerciciosSeleccion.size() + " ejercicios desde TXT");
        
        // Llamar al método interno con la lista convertida
        mostrarEjerciciosDirectamenteInterno(ejerciciosSeleccion, titulo, descripcion);
    }
    
    /**
     * 🎮 Implementación interna para mostrar ejercicios
     */
    private void mostrarEjerciciosDirectamenteInterno(List<EjercicioSeleccion> ejerciciosSeleccion, String titulo, String descripcion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml"));
            Parent root = loader.load();
            Modulo_Ejercicios.Controladores.EjercicioSeleccionController controller = loader.getController();
            
            // Configurar ejercicios
            controller.setEjercicios(ejerciciosSeleccion);
            
            // NUEVO: Configurar callback para cuando termine la lección
            configularCallbackFinalizacion(controller, titulo, descripcion, ejerciciosSeleccion.size());
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Lección: " + titulo);
            stage.show();
            
            cerrarVentanaActual();
            
        } catch (Exception e) {
            System.err.println("❌ Error mostrando ejercicios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 💾 Configura el callback para guardar progreso cuando termine la lección
     */
    private void configularCallbackFinalizacion(Modulo_Ejercicios.Controladores.EjercicioSeleccionController controller, 
                                              String titulo, String descripcion, int cantidadEjercicios) {
        
        System.out.println("🔧 Callback de finalización configurado para: " + titulo);
        System.out.println("📝 Descripción: " + descripcion);
        System.out.println("🎮 Ejercicios esperados: " + cantidadEjercicios);
        
        // ✅ El EjercicioSeleccionController ya fue modificado para llamar automáticamente
        // al método guardarProgresoLeccionEstatico cuando se complete la lección
        // No necesitamos hacer nada más aquí, solo informar que está configurado
        
        System.out.println("✅ Callback configurado correctamente - El progreso se guardará automáticamente");
    }
    
    /**
     * 🧪 MÉTODO DE PRUEBA: Prueba el nuevo sistema TXT
     * Este método demuestra cómo usar el nuevo sistema de archivos TXT
     */
    public void probarSistemaTXT() {
        System.out.println("\n🧪 === PROBANDO NUEVO SISTEMA TXT ===");
        
        // Configurar lección desde archivos TXT
        configurarLeccionDesdeTXT("JAVA", "BASICO");
        
        System.out.println("🧪 === SISTEMA TXT ACTIVADO ===\n");
    }
}
