package pe.razuri.evento;

import java.util.ArrayList;
import java.util.List;

public class BusEventos {

    private final List<EventoListener> listeners = new ArrayList<>();

    public void suscribir(EventoListener listener) {
        listeners.add(listener);
    }

    public void publicar(EventoHistorial evento) {
        listeners.forEach(listener -> listener.manejar(evento));
    }
}
