package Comunidad_Modulo.Controladores_GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Optional;

// Importar las clases del modelo y controladores
import Comunidad_Modulo.controladores.ContextoSistema;
import Comunidad_Modulo.modelo.*;
import Modulo_Usuario.Clases.NivelJava;
import Modulo_Usuario.Clases.UsuarioComunidad;
import Comunidad_Modulo.enums.TipoTema;
import Comunidad_Modulo.enums.TipoSolucion;
import Comunidad_Modulo.servicios.ComunidadService;
import MetodosGlobales.MetodosFrecuentes;

/**
 * Controlador para la gestión del foro en la interfaz JavaFX
 * Maneja grupos de discusión, grupos de compartir, hilos y soluciones
 * Implementa los principios SOLID y se integra con los controladores existentes
 */
public class GestionForo_Controller implements Initializable {

    // Referencias FXML a los elementos de la interfaz
    @FXML private TextField txtTituloGrupo;
    @FXML private ComboBox<NivelJava> comboNivelJava;
    @FXML private ComboBox<TipoTema> comboTema;
    @FXML private Button btnCrearGrupoDiscusion;
    @FXML private Button btnCrearGrupoCompartir;

    @FXML private Button btnListarGruposDiscusion;
    @FXML private Button btnListarGruposCompartir;

    // Nuevos elementos del FXML mejorado
    @FXML private ComboBox<String> comboTipoGrupo;
    @FXML private Button btnUnirseGrupo;

    @FXML private ComboBox<String> comboAccionHilo;
    @FXML private Button btnGestionarHilos;

    @FXML private ComboBox<String> comboAccionSolucion;
    @FXML private Button btnGestionarSoluciones;

    @FXML private TextArea txtInformacion;
    @FXML private Button btnVolver;
    @FXML private Label lblEstadoSistema;

    // Elementos antiguos que ya no se usan directamente
    @FXML private TextField txtIndice;
    @FXML private Button btnCrearHilo;
    @FXML private Button btnResponderHilo;
    @FXML private Button btnCompartirSolucion;

    // Controladores de negocio reutilizados del sistema existente
    private ContextoSistema contexto;
    private ComunidadService comunidadService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Inicializando controlador de Gestión de Foro...");

        // Inicializar los componentes del sistema
        inicializarSistema();

        // Configurar la interfaz
        configurarInterfaz();

        // Actualizar información inicial
        actualizarInformacion();

        System.out.println("✅ Controlador de Gestión de Foro inicializado correctamente");
    }

    /**
     * Inicializar el sistema con los componentes existentes
     */
    private void inicializarSistema() {
        // Obtener la instancia del contexto (singleton)
        this.contexto = ContextoSistema.getInstance();

        // Inicializar controladores de negocio
        this.comunidadService = new ComunidadService();
    }

    /**
     * Configurar elementos de la interfaz
     */
    private void configurarInterfaz() {
        // Configurar ComboBox de niveles de Java
        comboNivelJava.setItems(FXCollections.observableArrayList(NivelJava.values()));
        comboNivelJava.setValue(NivelJava.PRINCIPIANTE); // Valor por defecto

        // Configurar ComboBox de temas
        comboTema.setItems(FXCollections.observableArrayList(TipoTema.values()));
        comboTema.setValue(TipoTema.SINTAXIS); // Valor por defecto

        // Configurar ComboBox de tipo de grupo
        comboTipoGrupo.setItems(FXCollections.observableArrayList("Discusión", "Compartir"));
        comboTipoGrupo.setValue("Discusión");

        // Configurar ComboBox de acciones para hilos
        comboAccionHilo.setItems(FXCollections.observableArrayList(
                "Crear Hilo", "Responder Hilo", "Ver Hilos"
        ));
        comboAccionHilo.setValue("Crear Hilo");

        // Configurar ComboBox de acciones para soluciones
        comboAccionSolucion.setItems(FXCollections.observableArrayList(
                "Compartir Solución", "Ver Soluciones"
        ));
        comboAccionSolucion.setValue("Compartir Solución");

        // Configurar área de información como solo lectura
        txtInformacion.setEditable(false);
        txtInformacion.setWrapText(true);

        // Configurar placeholder texts
        txtTituloGrupo.setPromptText("Ingrese el título del grupo");

        // Estilo para el área de información
        txtInformacion.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");

        // Actualizar estado del sistema
        actualizarEstadoSistema();
    }

    /**
     * Método FXML para crear un grupo de discusión
     */
    @FXML
    private void crearGrupoDiscusion() {
        try {
            // Validar datos de entrada
            if (!validarDatosGrupo()) {
                return;
            }

            // Verificar que hay una comunidad activa
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa. Primero debe crear o seleccionar una comunidad.");
                return;
            }

            String titulo = txtTituloGrupo.getText().trim();
            NivelJava nivel = comboNivelJava.getValue();
            TipoTema tema = comboTema.getValue();

            // Crear el grupo directamente usando el foro
            ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
            GrupoDiscusion grupo = foro.crearGrupoDiscusion(titulo, nivel, tema);

            if (grupo != null) {
                // Limpiar campos
                txtTituloGrupo.clear();

                mostrarMensajeExito("✅ Grupo de discusión '" + titulo + "' creado exitosamente");
                actualizarInformacion();
            } else {
                mostrarMensajeError("❌ Error al crear el grupo de discusión");
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error: " + e.getMessage());
            System.err.println("Error al crear grupo de discusión: " + e.getMessage());
        }
    }

    /**
     * Método FXML para crear un grupo de compartir
     */
    @FXML
    private void crearGrupoCompartir() {
        try {
            // Validar datos de entrada
            if (!validarDatosGrupo()) {
                return;
            }

            // Verificar que hay una comunidad activa
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa. Primero debe crear o seleccionar una comunidad.");
                return;
            }

            String titulo = txtTituloGrupo.getText().trim();
            NivelJava nivel = comboNivelJava.getValue();
            TipoTema tema = comboTema.getValue();

            // Crear el grupo directamente usando el foro
            ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
            GrupoCompartir grupo = foro.crearGrupoCompartir(titulo, nivel, tema);

            if (grupo != null) {
                // Limpiar campos
                txtTituloGrupo.clear();

                mostrarMensajeExito("✅ Grupo de compartir '" + titulo + "' creado exitosamente");
                actualizarInformacion();
            } else {
                mostrarMensajeError("❌ Error al crear el grupo de compartir");
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error: " + e.getMessage());
            System.err.println("Error al crear grupo de compartir: " + e.getMessage());
        }
    }

    /**
     * Método FXML para listar grupos de discusión
     */
    @FXML
    private void listarGruposDiscusion() {
        try {
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa.");
                return;
            }

            ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
            List<GrupoDiscusion> grupos = foro.getGruposDiscusion();

            if (grupos.isEmpty()) {
                mostrarMensajeInfo("ℹ️ No hay grupos de discusión creados.");
            } else {
                StringBuilder info = new StringBuilder("=== GRUPOS DE DISCUSIÓN ===\n\n");
                for (int i = 0; i < grupos.size(); i++) {
                    GrupoDiscusion grupo = grupos.get(i);
                    info.append(String.format("%d. %s\n", (i + 1), grupo.toString()));
                    info.append(String.format("   Miembros: %d | Hilos: %d\n",
                            grupo.getMiembros().size(), grupo.getHilos().size()));
                    info.append("   ────────────────────────────────\n");
                }

                mostrarInformacion(info.toString());
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error al listar grupos de discusión: " + e.getMessage());
        }
    }

    /**
     * Método FXML para listar grupos de compartir
     */
    @FXML
    private void listarGruposCompartir() {
        try {
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa.");
                return;
            }

            ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();
            List<GrupoCompartir> grupos = foro.getGruposCompartir();

            if (grupos.isEmpty()) {
                mostrarMensajeInfo("ℹ️ No hay grupos de compartir creados.");
            } else {
                StringBuilder info = new StringBuilder("=== GRUPOS DE COMPARTIR ===\n\n");
                for (int i = 0; i < grupos.size(); i++) {
                    GrupoCompartir grupo = grupos.get(i);
                    info.append(String.format("%d. %s\n", (i + 1), grupo.toString()));
                    info.append(String.format("   Miembros: %d | Soluciones: %d\n",
                            grupo.getMiembros().size(), grupo.getSoluciones().size()));
                    info.append("   ────────────────────────────────\n");
                }

                mostrarInformacion(info.toString());
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error al listar grupos de compartir: " + e.getMessage());
        }
    }

    /**
     * Método FXML para unirse a un grupo - versión mejorada siguiendo lógica de consola
     */
    @FXML
    private void unirseAGrupo() {
        try {
            // Verificar que hay una comunidad activa
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa.");
                return;
            }

            // Obtener usuarios conectados (igual que en consola)
            List<UsuarioComunidad> usuariosConectados = contexto.getComunidadActual().getUsuariosConectados();
            if (usuariosConectados.isEmpty()) {
                mostrarMensajeError("⚠️ No hay usuarios conectados a la comunidad.");
                return;
            }

            // Mostrar usuarios disponibles y pedir selección
            StringBuilder usuariosInfo = new StringBuilder("=== USUARIOS CONECTADOS ===\n\n");
            for (int i = 0; i < usuariosConectados.size(); i++) {
                UsuarioComunidad usuario = usuariosConectados.get(i);
                usuariosInfo.append(String.format("%d. %s (%s)\n",
                        (i + 1), usuario.getNombre(), usuario.getNivelJava()));
            }

            // Solicitar selección del usuario
            TextInputDialog usuarioDialog = new TextInputDialog();
            usuarioDialog.setTitle("Unirse a Grupo");
            usuarioDialog.setHeaderText(usuariosInfo.toString());
            usuarioDialog.setContentText("Seleccione el usuario (número):");

            Optional<String> usuarioResult = usuarioDialog.showAndWait();
            if (!usuarioResult.isPresent()) return;

            try {
                int indiceUsuario = Integer.parseInt(usuarioResult.get().trim()) - 1;
                if (indiceUsuario < 0 || indiceUsuario >= usuariosConectados.size()) {
                    mostrarMensajeError("❌ Selección de usuario inválida.");
                    return;
                }

                UsuarioComunidad usuario = usuariosConectados.get(indiceUsuario);

                // Obtener tipo de grupo seleccionado del ComboBox
                String tipoSeleccionado = comboTipoGrupo.getValue();
                ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();

                if ("Discusión".equals(tipoSeleccionado)) {
                    unirseAGrupoDiscusion(usuario, foro);
                } else if ("Compartir".equals(tipoSeleccionado)) {
                    unirseAGrupoCompartir(usuario, foro);
                } else {
                    mostrarMensajeError("⚠️ Seleccione un tipo de grupo válido.");
                }

            } catch (NumberFormatException e) {
                mostrarMensajeError("❌ Debe ingresar un número válido.");
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error al unirse al grupo: " + e.getMessage());
        }
    }

    /**
     * Lógica específica para unirse a un grupo de discusión
     */
    private void unirseAGrupoDiscusion(UsuarioComunidad usuario, ForoGeneral foro) {
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de discusión disponibles. Primero cree uno.");
            return;
        }

        // Mostrar grupos disponibles
        StringBuilder info = new StringBuilder("=== GRUPOS DE DISCUSIÓN DISPONIBLES ===\n\n");
        for (int i = 0; i < grupos.size(); i++) {
            GrupoDiscusion grupo = grupos.get(i);
            info.append(String.format("%d. %s\n", (i + 1), grupo.toString()));
            info.append(String.format("   Miembros: %d | Hilos: %d\n",
                    grupo.getMiembros().size(), grupo.getHilos().size()));
            info.append("   ────────────────────────────────\n");
        }

        // Solicitar índice del grupo
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Unirse a Grupo de Discusión");
        dialog.setHeaderText(info.toString());
        dialog.setContentText("Seleccione el número del grupo:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) return;

        try {
            int indice = Integer.parseInt(result.get().trim());
            if (indice < 1 || indice > grupos.size()) {
                mostrarMensajeError("❌ Índice inválido. Debe estar entre 1 y " + grupos.size());
                return;
            }

            GrupoDiscusion grupo = grupos.get(indice - 1);

            // Verificar si ya está en el grupo
            if (grupo.getMiembros().contains(usuario)) {
                mostrarMensajeInfo("ℹ️ El usuario ya pertenece a este grupo.");
                return;
            }

            // Unirse al grupo
            comunidadService.procesarUsuarioEnGrupo(usuario, grupo);
            mostrarMensajeExito("✅ Usuario '" + usuario.getNombre() + "' se unió exitosamente al grupo '" + grupo.getTitulo() + "'");
            actualizarInformacion();

        } catch (NumberFormatException e) {
            mostrarMensajeError("❌ Debe ingresar un número válido.");
        }
    }

    /**
     * Lógica específica para unirse a un grupo de compartir
     */
    private void unirseAGrupoCompartir(UsuarioComunidad usuario, ForoGeneral foro) {
        List<GrupoCompartir> grupos = foro.getGruposCompartir();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de compartir disponibles. Primero cree uno.");
            return;
        }

        // Mostrar grupos disponibles
        StringBuilder info = new StringBuilder("=== GRUPOS DE COMPARTIR DISPONIBLES ===\n\n");
        for (int i = 0; i < grupos.size(); i++) {
            GrupoCompartir grupo = grupos.get(i);
            info.append(String.format("%d. %s\n", (i + 1), grupo.toString()));
            info.append(String.format("   Miembros: %d | Soluciones: %d\n",
                    grupo.getMiembros().size(), grupo.getSoluciones().size()));
            info.append("   ────────────────────────────────\n");
        }

        // Solicitar índice del grupo
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Unirse a Grupo de Compartir");
        dialog.setHeaderText(info.toString());
        dialog.setContentText("Seleccione el número del grupo:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) return;

        try {
            int indice = Integer.parseInt(result.get().trim());
            if (indice < 1 || indice > grupos.size()) {
                mostrarMensajeError("❌ Índice inválido. Debe estar entre 1 y " + grupos.size());
                return;
            }

            GrupoCompartir grupo = grupos.get(indice - 1);

            // Verificar si ya está en el grupo
            if (grupo.getMiembros().contains(usuario)) {
                mostrarMensajeInfo("ℹ️ El usuario ya pertenece a este grupo.");
                return;
            }

            // Unirse al grupo
            comunidadService.procesarUsuarioEnGrupoCompartir(usuario, grupo);
            mostrarMensajeExito("✅ Usuario '" + usuario.getNombre() + "' se unió exitosamente al grupo '" + grupo.getTitulo() + "'");
            actualizarInformacion();

        } catch (NumberFormatException e) {
            mostrarMensajeError("❌ Debe ingresar un número válido.");
        }
    }

    /**
     * Método FXML para gestionar hilos de discusión - siguiendo lógica de consola
     */
    @FXML
    private void gestionarHilos() {
        try {
            // Verificar que hay una comunidad activa
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa.");
                return;
            }

            String accion = comboAccionHilo.getValue();
            ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();

            switch (accion) {
                case "Crear Hilo":
                    crearHiloEnGrupo(null, foro); // Pasamos null para que siga lógica de consola
                    break;
                case "Responder Hilo":
                    responderHiloEnGrupo(null, foro); // Pasamos null para que siga lógica de consola
                    break;
                case "Ver Hilos":
                    verHilosEnGrupos(foro);
                    break;
                default:
                    mostrarMensajeError("⚠️ Seleccione una acción válida.");
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error al gestionar hilos: " + e.getMessage());
        }
    }

    /**
     * Método FXML para gestionar soluciones - siguiendo lógica de consola
     */
    @FXML
    private void gestionarSoluciones() {
        try {
            // Verificar que hay una comunidad activa
            if (!contexto.tieneComunidadActiva()) {
                mostrarMensajeError("⚠️ No hay una comunidad activa.");
                return;
            }

            String accion = comboAccionSolucion.getValue();
            ForoGeneral foro = contexto.getComunidadActual().getForoGeneral();

            switch (accion) {
                case "Compartir Solución":
                    compartirSolucionEnGrupo(null, foro); // Pasamos null para que siga lógica de consola
                    break;
                case "Ver Soluciones":
                    verSolucionesEnGrupos(foro);
                    break;
                default:
                    mostrarMensajeError("⚠️ Seleccione una acción válida.");
            }

        } catch (Exception e) {
            mostrarMensajeError("❌ Error al gestionar soluciones: " + e.getMessage());
        }
    }

    /**
     * Crear un hilo en un grupo de discusión específico - siguiendo lógica de consola
     */
    private void crearHiloEnGrupo(UsuarioComunidad usuarioIgnorado, ForoGeneral foro) {
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de discusión disponibles.");
            return;
        }

        // Mostrar todos los grupos de discusión (igual que en consola)
        StringBuilder info = new StringBuilder("=== GRUPOS DE DISCUSIÓN DISPONIBLES ===\n\n");
        for (int i = 0; i < grupos.size(); i++) {
            GrupoDiscusion grupo = grupos.get(i);
            info.append(String.format("%d. %s\n", (i + 1), grupo.toString()));
            info.append(String.format("   Miembros: %d | Hilos: %d\n",
                    grupo.getMiembros().size(), grupo.getHilos().size()));
            info.append("   ────────────────────────────────\n");
        }

        // Seleccionar grupo
        TextInputDialog grupoDialog = new TextInputDialog();
        grupoDialog.setTitle("Crear Hilo");
        grupoDialog.setHeaderText(info.toString());
        grupoDialog.setContentText("Seleccione el grupo (número):");

        Optional<String> grupoResult = grupoDialog.showAndWait();
        if (!grupoResult.isPresent()) return;

        try {
            int indiceGrupo = Integer.parseInt(grupoResult.get().trim()) - 1;
            if (indiceGrupo < 0 || indiceGrupo >= grupos.size()) {
                mostrarMensajeError("❌ Índice de grupo inválido.");
                return;
            }

            GrupoDiscusion grupo = grupos.get(indiceGrupo);
            List<UsuarioComunidad> miembros = grupo.getMiembros();

            if (miembros.isEmpty()) {
                mostrarMensajeError("⚠️ No hay miembros en este grupo.");
                return;
            }

            // Mostrar miembros del grupo para seleccionar autor
            StringBuilder miembrosInfo = new StringBuilder("=== MIEMBROS DEL GRUPO ===\n\n");
            for (int i = 0; i < miembros.size(); i++) {
                UsuarioComunidad miembro = miembros.get(i);
                miembrosInfo.append(String.format("%d. %s (%s)\n",
                        (i + 1), miembro.getNombre(), miembro.getNivelJava()));
            }

            // Seleccionar autor del hilo
            TextInputDialog autorDialog = new TextInputDialog();
            autorDialog.setTitle("Crear Hilo");
            autorDialog.setHeaderText(miembrosInfo.toString());
            autorDialog.setContentText("Seleccione el autor (número):");

            Optional<String> autorResult = autorDialog.showAndWait();
            if (!autorResult.isPresent()) return;

            int indiceAutor = Integer.parseInt(autorResult.get().trim()) - 1;
            if (indiceAutor < 0 || indiceAutor >= miembros.size()) {
                mostrarMensajeError("❌ Índice de autor inválido.");
                return;
            }

            UsuarioComunidad autor = miembros.get(indiceAutor);

            // Solicitar datos del hilo
            TextInputDialog tituloDialog = new TextInputDialog();
            tituloDialog.setTitle("Crear Hilo");
            tituloDialog.setHeaderText("Nuevo Hilo en: " + grupo.getTitulo() + "\nAutor: " + autor.getNombre());
            tituloDialog.setContentText("Título del hilo:");

            Optional<String> tituloResult = tituloDialog.showAndWait();
            if (!tituloResult.isPresent() || tituloResult.get().trim().isEmpty()) {
                return;
            }

            TextInputDialog problemaDialog = new TextInputDialog();
            problemaDialog.setTitle("Crear Hilo");
            problemaDialog.setHeaderText("Descripción del Problema");
            problemaDialog.setContentText("Descripción:");

            Optional<String> problemaResult = problemaDialog.showAndWait();
            if (!problemaResult.isPresent() || problemaResult.get().trim().isEmpty()) {
                return;
            }

            String titulo = tituloResult.get().trim();
            String problema = problemaResult.get().trim();

            // Crear el hilo
            grupo.crearHilo(titulo, problema, autor);
            mostrarMensajeExito("✅ Hilo '" + titulo + "' creado exitosamente por " + autor.getNombre() + " en '" + grupo.getTitulo() + "'");
            actualizarInformacion();

        } catch (NumberFormatException e) {
            mostrarMensajeError("❌ Debe ingresar un número válido.");
        } catch (IllegalArgumentException e) {
            mostrarMensajeError("❌ " + e.getMessage());
        }
    }

    /**
     * Responder a un hilo de discusión específico - siguiendo lógica de consola
     */
    private void responderHiloEnGrupo(UsuarioComunidad usuarioIgnorado, ForoGeneral foro) {
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de discusión disponibles.");
            return;
        }

        // Mostrar todos los grupos con sus hilos (igual que en consola)
        StringBuilder info = new StringBuilder("=== GRUPOS DE DISCUSIÓN DISPONIBLES ===\n\n");
        for (int i = 0; i < grupos.size(); i++) {
            GrupoDiscusion grupo = grupos.get(i);
            info.append(String.format("%d. %s (%d hilos)\n",
                    (i + 1), grupo.getTitulo(), grupo.getHilos().size()));
        }

        // Seleccionar grupo
        TextInputDialog grupoDialog = new TextInputDialog();
        grupoDialog.setTitle("Responder Hilo");
        grupoDialog.setHeaderText(info.toString());
        grupoDialog.setContentText("Seleccione el grupo (número):");

        Optional<String> grupoResult = grupoDialog.showAndWait();
        if (!grupoResult.isPresent()) return;

        try {
            int indiceGrupo = Integer.parseInt(grupoResult.get().trim()) - 1;
            if (indiceGrupo < 0 || indiceGrupo >= grupos.size()) {
                mostrarMensajeError("❌ Índice de grupo inválido.");
                return;
            }

            GrupoDiscusion grupo = grupos.get(indiceGrupo);
            List<HiloDiscusion> hilos = grupo.getHilos();

            if (hilos.isEmpty()) {
                mostrarMensajeError("⚠️ No hay hilos en este grupo.");
                return;
            }

            // Mostrar hilos disponibles
            StringBuilder hilosInfo = new StringBuilder("=== HILOS DISPONIBLES ===\n\n");
            for (int i = 0; i < hilos.size(); i++) {
                HiloDiscusion hilo = hilos.get(i);
                hilosInfo.append(String.format("%d. %s\n", (i + 1), hilo.getTitulo()));
                hilosInfo.append(String.format("   Respuestas: %d | Estado: %s\n",
                        hilo.getRespuestas().size(), hilo.getEstado()));
                hilosInfo.append("   ────────────────────────────────\n");
            }

            // Seleccionar hilo
            TextInputDialog hiloDialog = new TextInputDialog();
            hiloDialog.setTitle("Responder Hilo");
            hiloDialog.setHeaderText(hilosInfo.toString());
            hiloDialog.setContentText("Seleccione el hilo (número):");

            Optional<String> hiloResult = hiloDialog.showAndWait();
            if (!hiloResult.isPresent()) return;

            int indiceHilo = Integer.parseInt(hiloResult.get().trim()) - 1;
            if (indiceHilo < 0 || indiceHilo >= hilos.size()) {
                mostrarMensajeError("❌ Índice de hilo inválido.");
                return;
            }

            HiloDiscusion hilo = hilos.get(indiceHilo);

            // Seleccionar usuario que responde (de todos los conectados)
            List<UsuarioComunidad> usuariosConectados = contexto.getComunidadActual().getUsuariosConectados();
            if (usuariosConectados.isEmpty()) {
                mostrarMensajeError("⚠️ No hay usuarios conectados.");
                return;
            }

            StringBuilder usuariosInfo = new StringBuilder("=== USUARIOS CONECTADOS ===\n\n");
            for (int i = 0; i < usuariosConectados.size(); i++) {
                UsuarioComunidad usuario = usuariosConectados.get(i);
                usuariosInfo.append(String.format("%d. %s (%s)\n",
                        (i + 1), usuario.getNombre(), usuario.getNivelJava()));
            }

            // Seleccionar usuario que responde
            TextInputDialog autorDialog = new TextInputDialog();
            autorDialog.setTitle("Responder Hilo");
            autorDialog.setHeaderText(usuariosInfo.toString());
            autorDialog.setContentText("Seleccione el usuario que responde (número):");

            Optional<String> autorResult = autorDialog.showAndWait();
            if (!autorResult.isPresent()) return;

            int indiceAutor = Integer.parseInt(autorResult.get().trim()) - 1;
            if (indiceAutor < 0 || indiceAutor >= usuariosConectados.size()) {
                mostrarMensajeError("❌ Índice de usuario inválido.");
                return;
            }

            UsuarioComunidad autor = usuariosConectados.get(indiceAutor);

            // Solicitar contenido de la respuesta
            TextInputDialog respuestaDialog = new TextInputDialog();
            respuestaDialog.setTitle("Responder Hilo");
            respuestaDialog.setHeaderText("Respondiendo a: " + hilo.getTitulo() + "\nPor: " + autor.getNombre() +
                    "\n\nProblema: " + hilo.getProblema());
            respuestaDialog.setContentText("Su respuesta:");

            Optional<String> respuestaResult = respuestaDialog.showAndWait();
            if (!respuestaResult.isPresent() || respuestaResult.get().trim().isEmpty()) {
                return;
            }

            String contenido = respuestaResult.get().trim();

            // Procesar la respuesta con moderación (igual que en consola)
            Moderador moderador = contexto.getComunidadActual().getModerador();
            boolean respuestaEnviada = hilo.responder(contenido, autor, moderador);

            if (respuestaEnviada) {
                mostrarMensajeExito("✅ Respuesta agregada exitosamente por " + autor.getNombre());
            } else {
                mostrarMensajeError("🚫 La respuesta no pudo ser enviada (posible contenido inapropiado).");
            }

            actualizarInformacion();

        } catch (NumberFormatException e) {
            mostrarMensajeError("❌ Debe ingresar un número válido.");
        }
    }

    /**
     * Ver hilos en grupos de discusión
     */
    private void verHilosEnGrupos(ForoGeneral foro) {
        List<GrupoDiscusion> grupos = foro.getGruposDiscusion();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de discusión disponibles.");
            return;
        }

        StringBuilder info = new StringBuilder("=== TODOS LOS HILOS DE DISCUSIÓN ===\n\n");
        boolean hayHilos = false;

        for (GrupoDiscusion grupo : grupos) {
            if (!grupo.getHilos().isEmpty()) {
                hayHilos = true;
                info.append("📋 GRUPO: ").append(grupo.getTitulo()).append("\n");
                info.append("   Nivel: ").append(grupo.getNivelJava()).append(" | Tema: ").append(grupo.getTipoTema()).append("\n\n");

                for (int i = 0; i < grupo.getHilos().size(); i++) {
                    HiloDiscusion hilo = grupo.getHilos().get(i);
                    info.append(String.format("   %d. %s\n", (i + 1), hilo.getTitulo()));
                    info.append(String.format("      Estado: %s | Autor: %s\n",
                            hilo.getEstado(), hilo.getAutor().getNombre()));
                    info.append(String.format("      Respuestas: %d\n", hilo.getRespuestas().size()));
                }
                info.append("\n   ════════════════════════════════\n\n");
            }
        }

        if (!hayHilos) {
            mostrarMensajeInfo("ℹ️ No hay hilos de discusión creados.");
        } else {
            mostrarInformacion(info.toString());
        }
    }

    /**
     * Compartir una solución en un grupo específico - siguiendo lógica de consola
     */
    private void compartirSolucionEnGrupo(UsuarioComunidad usuarioIgnorado, ForoGeneral foro) {
        List<GrupoCompartir> grupos = foro.getGruposCompartir();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de compartir disponibles.");
            return;
        }

        // Mostrar todos los grupos de compartir (igual que en consola)
        StringBuilder info = new StringBuilder("=== GRUPOS DE COMPARTIR DISPONIBLES ===\n\n");
        for (int i = 0; i < grupos.size(); i++) {
            GrupoCompartir grupo = grupos.get(i);
            info.append(String.format("%d. %s\n", (i + 1), grupo.toString()));
            info.append(String.format("   Miembros: %d | Soluciones: %d\n",
                    grupo.getMiembros().size(), grupo.getSoluciones().size()));
            info.append("   ────────────────────────────────\n");
        }

        // Seleccionar grupo
        TextInputDialog grupoDialog = new TextInputDialog();
        grupoDialog.setTitle("Compartir Solución");
        grupoDialog.setHeaderText(info.toString());
        grupoDialog.setContentText("Seleccione el grupo (número):");

        Optional<String> grupoResult = grupoDialog.showAndWait();
        if (!grupoResult.isPresent()) return;

        try {
            int indiceGrupo = Integer.parseInt(grupoResult.get().trim()) - 1;
            if (indiceGrupo < 0 || indiceGrupo >= grupos.size()) {
                mostrarMensajeError("❌ Índice de grupo inválido.");
                return;
            }

            GrupoCompartir grupo = grupos.get(indiceGrupo);
            List<UsuarioComunidad> miembros = grupo.getMiembros();

            if (miembros.isEmpty()) {
                mostrarMensajeError("⚠️ No hay miembros en este grupo.");
                return;
            }

            // Mostrar miembros del grupo para seleccionar autor
            StringBuilder miembrosInfo = new StringBuilder("=== MIEMBROS DEL GRUPO ===\n\n");
            for (int i = 0; i < miembros.size(); i++) {
                UsuarioComunidad miembro = miembros.get(i);
                miembrosInfo.append(String.format("%d. %s (%s)\n",
                        (i + 1), miembro.getNombre(), miembro.getNivelJava()));
            }

            // Seleccionar autor de la solución
            TextInputDialog autorDialog = new TextInputDialog();
            autorDialog.setTitle("Compartir Solución");
            autorDialog.setHeaderText(miembrosInfo.toString());
            autorDialog.setContentText("Seleccione el autor (número):");

            Optional<String> autorResult = autorDialog.showAndWait();
            if (!autorResult.isPresent()) return;

            int indiceAutor = Integer.parseInt(autorResult.get().trim()) - 1;
            if (indiceAutor < 0 || indiceAutor >= miembros.size()) {
                mostrarMensajeError("❌ Índice de autor inválido.");
                return;
            }

            UsuarioComunidad autor = miembros.get(indiceAutor);

            // Solicitar datos de la solución
            TextInputDialog tituloDialog = new TextInputDialog();
            tituloDialog.setTitle("Compartir Solución");
            tituloDialog.setHeaderText("Nueva Solución en: " + grupo.getTitulo() + "\nAutor: " + autor.getNombre());
            tituloDialog.setContentText("Título de la solución:");

            Optional<String> tituloResult = tituloDialog.showAndWait();
            if (!tituloResult.isPresent() || tituloResult.get().trim().isEmpty()) {
                return;
            }

            TextInputDialog contenidoDialog = new TextInputDialog();
            contenidoDialog.setTitle("Compartir Solución");
            contenidoDialog.setHeaderText("Contenido de la Solución");
            contenidoDialog.setContentText("Descripción/Código:");

            Optional<String> contenidoResult = contenidoDialog.showAndWait();
            if (!contenidoResult.isPresent() || contenidoResult.get().trim().isEmpty()) {
                return;
            }

            String titulo = tituloResult.get().trim();
            String contenido = contenidoResult.get().trim();

            // Crear y compartir la solución
            Solucion solucion = new Solucion(titulo, contenido, autor, TipoSolucion.CODIGO);
            comunidadService.procesarSolucionEnGrupo(grupo, solucion);
            mostrarMensajeExito("✅ Solución '" + titulo + "' compartida exitosamente por " + autor.getNombre() + " en '" + grupo.getTitulo() + "'");
            actualizarInformacion();

        } catch (NumberFormatException e) {
            mostrarMensajeError("❌ Debe ingresar un número válido.");
        }
    }

    /**
     * Ver soluciones en grupos de compartir
     */
    private void verSolucionesEnGrupos(ForoGeneral foro) {
        List<GrupoCompartir> grupos = foro.getGruposCompartir();

        if (grupos.isEmpty()) {
            mostrarMensajeError("⚠️ No hay grupos de compartir disponibles.");
            return;
        }

        StringBuilder info = new StringBuilder("=== TODAS LAS SOLUCIONES COMPARTIDAS ===\n\n");
        boolean haySoluciones = false;

        for (GrupoCompartir grupo : grupos) {
            if (!grupo.getSoluciones().isEmpty()) {
                haySoluciones = true;
                info.append("🔄 GRUPO: ").append(grupo.getTitulo()).append("\n");
                info.append("   Nivel: ").append(grupo.getNivelJava()).append(" | Tema: ").append(grupo.getTipoTema()).append("\n\n");

                for (int i = 0; i < grupo.getSoluciones().size(); i++) {
                    Solucion solucion = grupo.getSoluciones().get(i);
                    info.append(String.format("   %d. %s\n", (i + 1), solucion.getTitulo()));
                    info.append(String.format("      Autor: %s | Tipo: %s\n",
                            solucion.getAutor().getNombre(), solucion.getTipoSolucion()));
                    info.append(String.format("      Contenido: %.50s%s\n",
                            solucion.getContenido(),
                            solucion.getContenido().length() > 50 ? "..." : ""));
                }
                info.append("\n   ════════════════════════════════\n\n");
            }
        }

        if (!haySoluciones) {
            mostrarMensajeInfo("ℹ️ No hay soluciones compartidas.");
        } else {
            mostrarInformacion(info.toString());
        }
    }

    /**
     * Actualiza el estado del sistema en la etiqueta
     */
    private void actualizarEstadoSistema() {
        StringBuilder estado = new StringBuilder();

        if (contexto.tieneComunidadActiva()) {
            Comunidad_Modulo.modelo.Comunidad comunidad = contexto.getComunidadActual();
            estado.append("✅ Comunidad: ").append(comunidad.getNombre());

            int usuariosConectados = comunidad.getUsuariosConectados().size();
            if (usuariosConectados > 0) {
                estado.append(" | ✅ ").append(usuariosConectados).append(" usuario");
                if (usuariosConectados > 1) {
                    estado.append("s");
                }
                estado.append(" conectado");
                if (usuariosConectados > 1) {
                    estado.append("s");
                }
            } else {
                estado.append(" | ⚠️ Sin usuarios conectados");
            }
        } else {
            estado.append("⚠️ Sin comunidad activa");
        }

        lblEstadoSistema.setText(estado.toString());
    }

    /**
     * Método FXML para volver al menú principal
     */
    @FXML
    private void volver() {
        try {
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            MetodosFrecuentes.cambiarVentana(stage, "/Modulo_Comunidad/Views/Comunidad.fxml");
        } catch (Exception e) {
            System.err.println("Error al volver: " + e.getMessage());
        }
    }

    /**
     * Valida los datos de entrada para crear un grupo
     */
    private boolean validarDatosGrupo() {
        String titulo = txtTituloGrupo.getText().trim();

        if (titulo.isEmpty()) {
            mostrarMensajeError("⚠️ El título del grupo no puede estar vacío.");
            return false;
        }

        if (titulo.length() < 3) {
            mostrarMensajeError("⚠️ El título debe tener al menos 3 caracteres.");
            return false;
        }

        if (comboNivelJava.getValue() == null) {
            mostrarMensajeError("⚠️ Debe seleccionar un nivel de Java.");
            return false;
        }

        if (comboTema.getValue() == null) {
            mostrarMensajeError("⚠️ Debe seleccionar un tema.");
            return false;
        }

        return true;
    }

    /**
     * Actualiza la información mostrada en la interfaz
     */
    private void actualizarInformacion() {
        StringBuilder info = new StringBuilder();

        info.append("=== INFORMACIÓN DEL FORO ===\n\n");

        if (contexto.tieneComunidadActiva()) {
            Comunidad_Modulo.modelo.Comunidad comunidad = contexto.getComunidadActual();
            ForoGeneral foro = comunidad.getForoGeneral();

            info.append("📌 Comunidad: ").append(comunidad.getNombre()).append("\n");
            info.append("👥 Usuarios conectados: ").append(comunidad.getUsuariosConectados().size()).append("\n");

            // Mostrar lista de usuarios conectados si los hay
            if (!comunidad.getUsuariosConectados().isEmpty()) {
                info.append("   ");
                for (int i = 0; i < comunidad.getUsuariosConectados().size() && i < 3; i++) {
                    UsuarioComunidad usuario = comunidad.getUsuariosConectados().get(i);
                    info.append(usuario.getNombre());
                    if (i < comunidad.getUsuariosConectados().size() - 1 && i < 2) {
                        info.append(", ");
                    }
                }
                if (comunidad.getUsuariosConectados().size() > 3) {
                    info.append(" y ").append(comunidad.getUsuariosConectados().size() - 3).append(" más...");
                }
                info.append("\n");
            }

            info.append("\n📋 Grupos de Discusión: ").append(foro.getGruposDiscusion().size()).append("\n");
            info.append("🔄 Grupos de Compartir: ").append(foro.getGruposCompartir().size()).append("\n\n");

            // Mostrar estadísticas de actividad
            int totalHilos = 0;
            int totalSoluciones = 0;

            for (GrupoDiscusion grupo : foro.getGruposDiscusion()) {
                totalHilos += grupo.getHilos().size();
            }

            for (GrupoCompartir grupo : foro.getGruposCompartir()) {
                totalSoluciones += grupo.getSoluciones().size();
            }

            info.append("📊 Actividad del Foro:\n");
            info.append("   • Hilos de discusión: ").append(totalHilos).append("\n");
            info.append("   • Soluciones compartidas: ").append(totalSoluciones).append("\n");

        } else {
            info.append("⚠️ No hay comunidad activa\n");
            info.append("Primero debe crear o seleccionar una comunidad");
        }

        txtInformacion.setText(info.toString());

        // Actualizar también el estado del sistema
        actualizarEstadoSistema();
    }

    /**
     * Muestra información general en el área de texto
     */
    private void mostrarInformacion(String mensaje) {
        txtInformacion.setText(mensaje);
    }

    /**
     * Muestra un mensaje de éxito con color verde
     */
    private void mostrarMensajeExito(String mensaje) {
        txtInformacion.setStyle("-fx-text-fill: green; -fx-background-color: #f0fff0; -fx-border-color: #90ee90;");
        txtInformacion.setText(mensaje);

        // Restaurar estilo después de 3 segundos
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            txtInformacion.setStyle("-fx-text-fill: black; -fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
            actualizarInformacion();
        }));
        timeline.play();
    }

    /**
     * Muestra un mensaje de error con color rojo
     */
    private void mostrarMensajeError(String mensaje) {
        txtInformacion.setStyle("-fx-text-fill: red; -fx-background-color: #fff0f0; -fx-border-color: #ffcccc;");
        txtInformacion.setText(mensaje);

        // Restaurar estilo después de 4 segundos
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
            txtInformacion.setStyle("-fx-text-fill: black; -fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
            actualizarInformacion();
        }));
        timeline.play();
    }

    /**
     * Muestra un mensaje de información con color azul
     */
    private void mostrarMensajeInfo(String mensaje) {
        txtInformacion.setStyle("-fx-text-fill: blue; -fx-background-color: #f0f8ff; -fx-border-color: #add8e6;");
        txtInformacion.setText(mensaje);

        // Restaurar estilo después de 3 segundos
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            txtInformacion.setStyle("-fx-text-fill: black; -fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
            actualizarInformacion();
        }));
        timeline.play();
    }
}
