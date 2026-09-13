package pe.razuri.servicio;

import pe.razuri.evento.BusEventos;
import pe.razuri.evento.EventoHistorial;
import pe.razuri.excepcion.AccesoDenegadoException;
import pe.razuri.modelo.*;
import pe.razuri.seguridad.ControlAcceso;

public class ServicioHistorial {

    private final ServicioPaciente servicioPaciente;
    private final ControlAcceso controlAcceso;
    private final BusEventos busEventos;

    private int siguienteId = 1;

    public ServicioHistorial(
            ServicioPaciente servicioPaciente,
            ControlAcceso controlAcceso,
            BusEventos busEventos) {

        this.servicioPaciente = servicioPaciente;
        this.controlAcceso = controlAcceso;
        this.busEventos = busEventos;
    }

    public HistorialClinico crearHistorial(
            String dni,
            Usuario usuario) {

        Paciente paciente = servicioPaciente.buscarPorDni(dni);

        if (paciente.tieneHistorial()) {
            return paciente.getHistorial();
        }

        HistorialClinico historial = new HistorialClinico(
                siguienteId++,
                paciente.getIdPaciente()
        );

        paciente.asignarHistorial(historial);

        publicarEvento(
                usuario,
                historial,
                "CREACION_HISTORIAL"
        );

        return historial;
    }

    public HistorialClinico consultarHistorial(
            String dni,
            Usuario usuario) {

        if (!controlAcceso.puedeConsultarHistorial(usuario)) {
            throw new AccesoDenegadoException(
                    "Usuario no autorizado."
            );
        }

        Paciente paciente = servicioPaciente.buscarPorDni(dni);

        if (!paciente.tieneHistorial()) {
            throw new IllegalStateException(
                    "El paciente no posee historial."
            );
        }

        HistorialClinico historial = paciente.getHistorial();

        publicarEvento(
                usuario,
                historial,
                "CONSULTA_HISTORIAL"
        );

        return historial;
    }

    public void registrarEvolucion(
            String dni,
            EvolucionClinica evolucion,
            Usuario usuario) {

        if (!controlAcceso.puedeRegistrarEvolucion(usuario)) {
            throw new AccesoDenegadoException(
                    "Solo un profesional autorizado puede registrar evoluciones."
            );
        }

        HistorialClinico historial = consultarHistorial(
                dni,
                usuario
        );

        historial.agregarEvolucion(evolucion);

        publicarEvento(
                usuario,
                historial,
                "REGISTRO_EVOLUCION"
        );
    }

    private void publicarEvento(
            Usuario usuario,
            HistorialClinico historial,
            String accion) {

        busEventos.publicar(
                new EventoHistorial(
                        usuario.getIdUsuario(),
                        historial.getIdHistorial(),
                        accion
                )
        );
    }
}
