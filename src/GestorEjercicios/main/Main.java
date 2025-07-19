import model.*;
import filtros.*;
import enums.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int vidas = 3;
        int experiencia = 0;

        List<Ejercicio> disponibles = new ArrayList<>();

        disponibles.add(crearEjercicio(1, "Hello World", "Escribe el código en Java que imprima 'Hello World'.", "System.out.println(\"Hello World\");"));
        disponibles.add(crearEjercicio(2, "Condicionales", "¿Qué palabra clave se usa para una condición en Java?", "if"));
        disponibles.add(crearEjercicio(3, "While Loop", "¿Qué palabra clave se usa para un ciclo condicional en Java?", "while"));
        disponibles.add(crearEjercicio(4, "Funciones", "¿Cómo se define una función en Java? Usa la palabra clave.", "void"));
        disponibles.add(crearEjercicio(5, "Clases", "¿Qué palabra clave se usa para declarar una clase en Java?", "class"));
        disponibles.add(crearEjercicio(6, "Entrada de datos", "¿Qué clase se usa comúnmente para recibir datos del usuario en consola?", "Scanner"));
        disponibles.add(crearEjercicio(7, "Operadores", "¿Cuál es el operador de igualdad en Java?", "=="));
        disponibles.add(crearEjercicio(8, "Arreglos", "¿Qué símbolo se usa para declarar un arreglo?", "[]"));

        List<FiltroEjercicio> filtros = Arrays.asList(
            new FiltroPorLenguaje(LenguajeProgramacion.JAVA)
        );

        GestorLecciones gestorLecciones = new GestorLecciones();
        Leccion leccion = gestorLecciones.generarLeccionPersonalizada("Repaso Java", filtros, disponibles);

        System.out.println("Iniciando Lección: " + leccion.obtenerResumen());
        Scanner sc = new Scanner(System.in);

        int correctos = 0;
        for (DetalleLeccion detalle : leccion.getEjerciciosResueltos()) {
            Ejercicio ejercicio = detalle.getEjercicio();
            System.out.println("\nEjercicio: " + ejercicio.getTitulo());
            System.out.println(ejercicio.getEnunciado());
            System.out.print("Tu respuesta: ");
            String respuesta = sc.nextLine();
            detalle.resolverEjercicio(respuesta);
            ResultadoEvaluacion r = detalle.getResultado();
            if (r.isCorrecto()) correctos++;
            System.out.println("Resultado: " + r.getObtenerMensaje());
        }

        boolean leccionAprobada = ResultadoEvaluacion.evaluarLeccion(leccion.getEjerciciosResueltos().size(), correctos);
        if (leccionAprobada) {
            experiencia += 50;
            System.out.println("\n¡Lección completada con éxito! Experiencia ganada: 50 XP");
        } else {
            vidas -= 1;
            System.out.println("\nNo superaste la lección. Pierdes una vida.");
        }

        System.out.println("Resumen Final -> Ejercicios correctos: " + correctos + "/" + leccion.getEjerciciosResueltos().size());
        System.out.println("Estado: " + (leccionAprobada ? "APROBADO" : "REPROBADO"));
        System.out.println("Experiencia total: " + experiencia + " XP");
        System.out.println("Vidas restantes: " + vidas);
    }

    public static Ejercicio crearEjercicio(int id, String titulo, String enunciado, String respuesta) {
        Ejercicio e = new Ejercicio(id, titulo, NivelDificultad.BASICO, LenguajeProgramacion.JAVA, TipoEjercicio.COMPLETAR_CODIGO, List.of("java", "basico"));
        e.setEnunciado(enunciado);
        e.setRespuestaCorrecta(respuesta);
        return e;
    }
}
