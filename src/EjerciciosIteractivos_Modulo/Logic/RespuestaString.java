package EjerciciosIteractivos_Modulo.Logic;

public class RespuestaString implements Respuesta {
    private String respuesta;

    public RespuestaString(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public Object getRespuesta() {
        return respuesta;
    }
}
