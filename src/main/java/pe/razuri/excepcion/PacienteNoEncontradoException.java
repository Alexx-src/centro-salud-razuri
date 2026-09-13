package pe.razuri.excepcion;

public class PacienteNoEncontradoException extends SistemaClinicoException {
    public PacienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}