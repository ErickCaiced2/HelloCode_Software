package GestorEjercicios.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Sistema de evaluación avanzado con feedback detallado
 */
public class EvaluadorAvanzado {
    
    /**
     * Evalúa una respuesta y proporciona feedback detallado
     */
    public static ResultadoEvaluacion evaluarRespuesta(Ejercicio ejercicio, String respuestaUsuario) {
        String respuestaCorrecta = ejercicio.getRespuestaCorrecta();
        String enunciado = ejercicio.getEnunciado();
        
        // Normalizar respuestas
        String respuestaNormalizada = normalizarRespuesta(respuestaUsuario);
        String correctaNormalizada = normalizarRespuesta(respuestaCorrecta);
        
        boolean esCorrecta = respuestaNormalizada.equals(correctaNormalizada);
        
        if (esCorrecta) {
            return crearResultadoCorrecto(ejercicio);
        } else {
            return crearResultadoIncorrecto(ejercicio, respuestaUsuario, respuestaCorrecta);
        }
    }
    
    /**
     * Normaliza una respuesta para comparación
     */
    private static String normalizarRespuesta(String respuesta) {
        return respuesta.trim()
                .toLowerCase()
                .replaceAll("\\s+", " ") // Múltiples espacios a uno
                .replaceAll("[;,\\s]*$", "") // Eliminar punto y coma y espacios al final
                .replaceAll("^[;,\\s]*", ""); // Eliminar punto y coma y espacios al inicio
    }
    
    /**
     * Crea un resultado para respuesta correcta
     */
    private static ResultadoEvaluacion crearResultadoCorrecto(Ejercicio ejercicio) {
        String mensaje = generarMensajeCorrecto(ejercicio);
        return new ResultadoEvaluacion(true, 100, 30, mensaje, false);
    }
    
    /**
     * Crea un resultado para respuesta incorrecta
     */
    private static ResultadoEvaluacion crearResultadoIncorrecto(Ejercicio ejercicio, String respuestaUsuario, String respuestaCorrecta) {
        String mensaje = generarMensajeIncorrecto(ejercicio, respuestaUsuario, respuestaCorrecta);
        int puntuacion = calcularPuntuacionParcial(respuestaUsuario, respuestaCorrecta);
        return new ResultadoEvaluacion(false, puntuacion, 30, mensaje, false);
    }
    
    /**
     * Genera mensaje para respuesta correcta
     */
    private static String generarMensajeCorrecto(Ejercicio ejercicio) {
        List<String> mensajes = Arrays.asList(
            "¡Excelente! Respuesta correcta.",
            "¡Perfecto! Has acertado.",
            "¡Muy bien! Respuesta correcta.",
            "¡Correcto! Sigue así.",
            "¡Bien hecho! Respuesta acertada."
        );
        
        String mensajeBase = mensajes.get(new Random().nextInt(mensajes.size()));
        
        // Agregar explicación según el tipo de ejercicio
        switch (ejercicio.getTipo()) {
            case COMPLETAR_CODIGO:
                return mensajeBase + "\n\n💡 **Explicación:** Has completado correctamente el código. " +
                       "Recuerda que en programación, la precisión es fundamental.";
            case MULTIPLE_CHOICE:
                return mensajeBase + "\n\n💡 **Explicación:** Has seleccionado la opción correcta. " +
                       "Este tipo de preguntas evalúan tu comprensión conceptual.";
            case ESCRIBIR_FUNCION:
                return mensajeBase + "\n\n💡 **Explicación:** Has escrito la respuesta correcta. " +
                       "La sintaxis y el contenido son importantes en programación.";
            default:
                return mensajeBase;
        }
    }
    
    /**
     * Genera mensaje para respuesta incorrecta
     */
    private static String generarMensajeIncorrecto(Ejercicio ejercicio, String respuestaUsuario, String respuestaCorrecta) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("❌ **Respuesta incorrecta.**\n\n");
        mensaje.append("**Tu respuesta:** ").append(respuestaUsuario).append("\n");
        mensaje.append("**Respuesta correcta:** ").append(respuestaCorrecta).append("\n\n");
        
        // Agregar sugerencias según el tipo de ejercicio
        switch (ejercicio.getTipo()) {
            case COMPLETAR_CODIGO:
                mensaje.append("💡 **Sugerencias:**\n");
                mensaje.append("• Revisa la sintaxis del código\n");
                mensaje.append("• Verifica que las variables estén correctamente declaradas\n");
                mensaje.append("• Asegúrate de que la lógica sea coherente\n");
                mensaje.append("• Revisa los espacios y puntuación\n");
                break;
                
            case MULTIPLE_CHOICE:
                mensaje.append("💡 **Sugerencias:**\n");
                mensaje.append("• Lee cuidadosamente todas las opciones\n");
                mensaje.append("• Elimina las opciones que sabes que son incorrectas\n");
                mensaje.append("• Considera el contexto del lenguaje de programación\n");
                break;
                
            case ESCRIBIR_FUNCION:
                mensaje.append("💡 **Sugerencias:**\n");
                mensaje.append("• Revisa la ortografía y mayúsculas/minúsculas\n");
                mensaje.append("• Verifica que la respuesta sea completa\n");
                mensaje.append("• Considera el contexto del ejercicio\n");
                break;
        }
        
        // Agregar explicación específica según el lenguaje
        mensaje.append("\n📚 **Explicación:**\n");
        mensaje.append(generarExplicacionLenguaje(ejercicio, respuestaCorrecta));
        
        return mensaje.toString();
    }
    
    /**
     * Genera explicación específica según el lenguaje de programación
     */
    private static String generarExplicacionLenguaje(Ejercicio ejercicio, String respuestaCorrecta) {
        switch (ejercicio.getLenguaje()) {
            case JAVA:
                return generarExplicacionJava(ejercicio, respuestaCorrecta);
            case PYTHON:
                return generarExplicacionPython(ejercicio, respuestaCorrecta);
            case JAVASCRIPT:
                return generarExplicacionJavaScript(ejercicio, respuestaCorrecta);
            case CPP:
                return generarExplicacionCpp(ejercicio, respuestaCorrecta);
            default:
                return "Revisa la documentación del lenguaje para más información.";
        }
    }
    
    /**
     * Genera explicación específica para Java
     */
    private static String generarExplicacionJava(Ejercicio ejercicio, String respuestaCorrecta) {
        String titulo = ejercicio.getTitulo().toLowerCase();
        
        if (titulo.contains("hello world")) {
            return "En Java, usamos `System.out.println()` para imprimir en consola. " +
                   "Esta es la forma estándar de mostrar texto en Java.";
        } else if (titulo.contains("variable")) {
            return "En Java, las variables se declaran con su tipo de dato. " +
                   "Para enteros usamos `int`, para texto `String`, etc.";
        } else if (titulo.contains("if")) {
            return "En Java, las condiciones se escriben con `if`. " +
                   "La sintaxis es: `if (condicion) { ... }`";
        } else if (titulo.contains("for")) {
            return "En Java, los bucles for se escriben con `for`. " +
                   "La sintaxis es: `for (inicialización; condición; incremento)`";
        }
        
        return "En Java, es importante respetar la sintaxis exacta y las mayúsculas/minúsculas.";
    }
    
    /**
     * Genera explicación específica para Python
     */
    private static String generarExplicacionPython(Ejercicio ejercicio, String respuestaCorrecta) {
        String titulo = ejercicio.getTitulo().toLowerCase();
        
        if (titulo.contains("hello world")) {
            return "En Python, usamos `print()` para imprimir en consola. " +
                   "Es más simple que en otros lenguajes.";
        } else if (titulo.contains("variable")) {
            return "En Python, no necesitas declarar el tipo de variable. " +
                   "Simplemente asignas el valor directamente.";
        } else if (titulo.contains("if")) {
            return "En Python, las condiciones se escriben con `if`. " +
                   "La sintaxis es: `if condicion:` (con dos puntos al final)";
        } else if (titulo.contains("for")) {
            return "En Python, los bucles for se escriben con `for`. " +
                   "La sintaxis es: `for elemento in secuencia:`";
        }
        
        return "En Python, la indentación es muy importante y no necesitas punto y coma al final.";
    }
    
    /**
     * Genera explicación específica para JavaScript
     */
    private static String generarExplicacionJavaScript(Ejercicio ejercicio, String respuestaCorrecta) {
        String titulo = ejercicio.getTitulo().toLowerCase();
        
        if (titulo.contains("hello world")) {
            return "En JavaScript, usamos `console.log()` para imprimir en consola. " +
                   "Es la forma estándar en navegadores y Node.js.";
        } else if (titulo.contains("variable")) {
            return "En JavaScript moderno (ES6+), usamos `let` o `const` para declarar variables. " +
                   "`var` es la forma antigua.";
        } else if (titulo.contains("if")) {
            return "En JavaScript, las condiciones se escriben con `if`. " +
                   "La sintaxis es: `if (condicion) { ... }`";
        } else if (titulo.contains("for")) {
            return "En JavaScript, los bucles for se escriben con `for`. " +
                   "La sintaxis es: `for (inicialización; condición; incremento)`";
        }
        
        return "En JavaScript, las mayúsculas/minúsculas son importantes y los paréntesis son necesarios.";
    }
    
    /**
     * Genera explicación específica para C++
     */
    private static String generarExplicacionCpp(Ejercicio ejercicio, String respuestaCorrecta) {
        String titulo = ejercicio.getTitulo().toLowerCase();
        
        if (titulo.contains("hello world")) {
            return "En C++, usamos `cout <<` para imprimir en consola. " +
                   "Necesitas incluir `<iostream>` y usar `std::cout` o `using namespace std;`";
        } else if (titulo.contains("variable")) {
            return "En C++, las variables se declaran con su tipo de dato. " +
                   "Para enteros usamos `int`, para texto `string`, etc.";
        } else if (titulo.contains("if")) {
            return "En C++, las condiciones se escriben con `if`. " +
                   "La sintaxis es: `if (condicion) { ... }`";
        } else if (titulo.contains("for")) {
            return "En C++, los bucles for se escriben con `for`. " +
                   "La sintaxis es: `for (inicialización; condición; incremento)`";
        }
        
        return "En C++, es importante respetar la sintaxis exacta y usar punto y coma al final de las declaraciones.";
    }
    
    /**
     * Calcula puntuación parcial para respuestas cercanas
     */
    private static int calcularPuntuacionParcial(String respuestaUsuario, String respuestaCorrecta) {
        String usuario = normalizarRespuesta(respuestaUsuario);
        String correcta = normalizarRespuesta(respuestaCorrecta);
        
        // Si son muy similares, dar puntuación parcial
        if (usuario.contains(correcta) || correcta.contains(usuario)) {
            return 50; // Puntuación parcial
        }
        
        // Verificar si hay palabras en común
        String[] palabrasUsuario = usuario.split("\\s+");
        String[] palabrasCorrecta = correcta.split("\\s+");
        
        int palabrasComunes = 0;
        for (String palabra : palabrasUsuario) {
            for (String palabraCorrecta : palabrasCorrecta) {
                if (palabra.equals(palabraCorrecta)) {
                    palabrasComunes++;
                }
            }
        }
        
        if (palabrasComunes > 0) {
            return Math.min(30, palabrasComunes * 10); // Máximo 30 puntos por palabras en común
        }
        
        return 0; // Sin puntuación
    }
} 