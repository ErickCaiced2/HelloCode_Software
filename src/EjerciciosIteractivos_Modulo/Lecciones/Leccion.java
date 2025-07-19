package EjerciciosIteractivos_Modulo.Lecciones;

import java.util.ArrayList;

import EjerciciosIteractivos_Modulo.Logic.EjercicioBase;

public class Leccion {
    ArrayList<EjercicioBase> listaEjercicios;

    //Atributos que sugerimos tenga una lección
    private String nombre; //De que va la lección en general?


    public Leccion(String nombre) {
        this.nombre = nombre;
        this.listaEjercicios = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumEjercicios() {
        return listaEjercicios.size();
    }

    public EjercicioBase getEjercicio(int numEjercicio) {
        return listaEjercicios.get(numEjercicio);
    }

    public boolean addEjercicio(EjercicioBase nuevoEjercicio) {
        return listaEjercicios.add(nuevoEjercicio);
    }
}
