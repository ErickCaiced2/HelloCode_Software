package GestorEjercicios.model;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoEjercicio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para integrar ejercicios del Modulo_Ejercicios con GestorEjercicios
 */
public class AdaptadorEjercicios {
    
    /**
     * Carga ejercicios de completar código desde la base de datos
     */
    public static List<Ejercicio> cargarEjerciciosCompletarCodigo() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Modulo_Ejercicios/data/DB_EjerciciosCompletarCodigo.txt"))) {
            String linea;
            int id = 1000; // IDs diferentes para evitar conflictos
            
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length >= 5) {
                    String instruccion = partes[0];
                    String codigoIncompleto = partes[1];
                    String respuesta = partes[2];
                    String nivel = partes[3];
                    String lenguaje = partes[4];
                    
                    NivelDificultad dificultad = convertirNivel(nivel);
                    LenguajeProgramacion lenguajeProg = convertirLenguaje(lenguaje);
                    
                    Ejercicio ejercicio = new Ejercicio(
                        id++,
                        "Completar Código: " + instruccion.substring(0, Math.min(30, instruccion.length())) + "...",
                        dificultad,
                        lenguajeProg,
                        TipoEjercicio.COMPLETAR_CODIGO,
                        List.of("completar_codigo", lenguaje.toLowerCase())
                    );
                    
                    ejercicio.setEnunciado(instruccion + "\n\n" + codigoIncompleto);
                    ejercicio.setRespuestaCorrecta(respuesta);
                    
                    ejercicios.add(ejercicio);
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando ejercicios de completar código: " + e.getMessage());
        }
        
        return ejercicios;
    }
    
    /**
     * Carga ejercicios de selección múltiple desde la base de datos
     */
    public static List<Ejercicio> cargarEjerciciosSeleccion() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Modulo_Ejercicios/data/DB_EjerciciosSeleccion.txt"))) {
            String linea;
            int id = 2000; // IDs diferentes para evitar conflictos
            
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length >= 5) {
                    String pregunta = partes[0];
                    String opciones = partes[1];
                    String respuesta = partes[2];
                    String nivel = partes[3];
                    String lenguaje = partes[4];
                    
                    NivelDificultad dificultad = convertirNivel(nivel);
                    LenguajeProgramacion lenguajeProg = convertirLenguaje(lenguaje);
                    
                    Ejercicio ejercicio = new Ejercicio(
                        id++,
                        "Selección Múltiple: " + pregunta.substring(0, Math.min(30, pregunta.length())) + "...",
                        dificultad,
                        lenguajeProg,
                        TipoEjercicio.MULTIPLE_CHOICE,
                        List.of("seleccion_multiple", lenguaje.toLowerCase())
                    );
                    
                    ejercicio.setEnunciado(pregunta + "\n\nOpciones:\n" + opciones);
                    ejercicio.setRespuestaCorrecta(respuesta);
                    
                    ejercicios.add(ejercicio);
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando ejercicios de selección: " + e.getMessage());
        }
        
        return ejercicios;
    }
    
    /**
     * Convierte el nivel de dificultad del formato del archivo al enum
     */
    private static NivelDificultad convertirNivel(String nivel) {
        switch (nivel.toUpperCase()) {
            case "BASICO": return NivelDificultad.BASICO;
            case "INTERMEDIO": return NivelDificultad.INTERMEDIO;
            case "AVANZADO": return NivelDificultad.AVANZADO;
            default: return NivelDificultad.BASICO;
        }
    }
    
    /**
     * Convierte el lenguaje del formato del archivo al enum
     */
    private static LenguajeProgramacion convertirLenguaje(String lenguaje) {
        switch (lenguaje.toUpperCase()) {
            case "JAVA": return LenguajeProgramacion.JAVA;
            case "PYTHON": return LenguajeProgramacion.PYTHON;
            case "JAVASCRIPT": return LenguajeProgramacion.JAVASCRIPT;
            case "CPP": return LenguajeProgramacion.CPP;
            default: return LenguajeProgramacion.JAVA;
        }
    }
    
    /**
     * Obtiene todos los ejercicios disponibles combinando los básicos y avanzados
     */
    public static List<Ejercicio> obtenerTodosLosEjercicios() {
        List<Ejercicio> todos = new ArrayList<>();
        
        // Agregar ejercicios básicos (los que ya tienes)
        todos.addAll(obtenerEjerciciosBasicos());
        
        // Agregar ejercicios avanzados del otro módulo
        todos.addAll(cargarEjerciciosCompletarCodigo());
        todos.addAll(cargarEjerciciosSeleccion());
        
        return todos;
    }
    
    /**
     * Obtiene ejercicios básicos (los que ya tienes hardcodeados)
     */
    private static List<Ejercicio> obtenerEjerciciosBasicos() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        
        // Java Básico
        ejercicios.add(crearEjercicioBasico(1, "Hello World Java", NivelDificultad.BASICO, 
            "Escribe el código en Java que imprima 'Hello World'.", 
            "System.out.println(\"Hello World\");", LenguajeProgramacion.JAVA));
        
        ejercicios.add(crearEjercicioBasico(2, "Variables Java", NivelDificultad.BASICO, 
            "¿Qué palabra clave se usa para declarar una variable entera en Java?", 
            "int", LenguajeProgramacion.JAVA));
        
        // Python Básico
        ejercicios.add(crearEjercicioBasico(13, "Hello World Python", NivelDificultad.BASICO, 
            "Escribe el código en Python que imprima 'Hello World'.", 
            "print(\"Hello World\")", LenguajeProgramacion.PYTHON));
        
        ejercicios.add(crearEjercicioBasico(14, "Variables Python", NivelDificultad.BASICO, 
            "¿Qué palabra clave se usa para declarar una variable en Python?", 
            "ninguna", LenguajeProgramacion.PYTHON));
        
        // JavaScript Básico
        ejercicios.add(crearEjercicioBasico(25, "Hello World JS", NivelDificultad.BASICO, 
            "Escribe el código en JavaScript que imprima 'Hello World'.", 
            "console.log(\"Hello World\")", LenguajeProgramacion.JAVASCRIPT));
        
        ejercicios.add(crearEjercicioBasico(26, "Variables JS", NivelDificultad.BASICO, 
            "¿Qué palabra clave se usa para declarar una variable en JavaScript (ES6)?", 
            "let", LenguajeProgramacion.JAVASCRIPT));
        
        // C++ Básico
        ejercicios.add(crearEjercicioBasico(37, "Hello World C++", NivelDificultad.BASICO, 
            "Escribe el código en C++ que imprima 'Hello World'.", 
            "cout << \"Hello World\";", LenguajeProgramacion.CPP));
        
        return ejercicios;
    }
    
    private static Ejercicio crearEjercicioBasico(int id, String titulo, NivelDificultad dificultad, 
                                                 String enunciado, String respuesta, LenguajeProgramacion lenguaje) {
        Ejercicio ejercicio = new Ejercicio(
            id, titulo, dificultad, lenguaje, TipoEjercicio.ESCRIBIR_FUNCION, 
            List.of("basico", lenguaje.toString().toLowerCase())
        );
        ejercicio.setEnunciado(enunciado);
        ejercicio.setRespuestaCorrecta(respuesta);
        return ejercicio;
    }
} 