package pe.razuri.excepcion;

public class AccesoDenegadoException extends SistemaClinicoException {
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}