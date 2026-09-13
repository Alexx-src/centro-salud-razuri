package pe.razuri.servicio;

import pe.razuri.evento.EventoHistorial;
import pe.razuri.evento.EventoListener;
import pe.razuri.modelo.RegistroAuditoria;

import java.util.ArrayList;
import java.util.List;

public class ServicioAuditoria implements EventoListener {

    private final List<RegistroAuditoria> registros = new ArrayList<>();
    private int siguienteIdRegistro = 1;

    @Override
    public void manejar(EventoHistorial evento) {
        // Paradigma Orientado a Eventos: Recibe el evento y crea el objeto de auditoría
        RegistroAuditoria registro = new RegistroAuditoria(
                siguienteIdRegistro++,
                evento.accion(),
                evento.idUsuario(),
                evento.idHistorial()
        );
        registros.add(registro);
    }

    // Paradigma Funcional: Uso de Stream API y filter()
    public List<RegistroAuditoria> filtrarPorUsuario(int idUsuario) {
        return registros.stream()
                .filter(registro -> registro.getIdUsuario() == idUsuario)
                .toList();
    }

    // Paradigma Funcional: Uso de Stream API y map()
    public List<String> obtenerResumenAcciones() {
        return registros.stream()
                .map(registro -> registro.getFechaHora() + " - " + registro.getAccion())
                .toList();
    }
}
