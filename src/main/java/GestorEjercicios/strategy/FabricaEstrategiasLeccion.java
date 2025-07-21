package GestorEjercicios.strategy;

import GestorEjercicios.enums.TipoLeccion;

public class FabricaEstrategiasLeccion {
    
    public static EstrategiaLeccion crearEstrategia(TipoLeccion tipo) {
        switch (tipo) {
            case NORMAL:
                return new EstrategiaLeccionNormal();
            case DIAGNOSTICO:
                return new EstrategiaLeccionDiagnostico();
            case PRUEBA:
                return new EstrategiaLeccionPrueba();
            default:
                throw new IllegalArgumentException("Tipo de lección no soportado: " + tipo);
        }
    }
} 