package pe.razuri.excepcion;

public class DniInvalidoException extends SistemaClinicoException {
    public DniInvalidoException(String mensaje) {
        super(mensaje);
    }
}