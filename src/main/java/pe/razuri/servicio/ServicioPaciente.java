package pe.razuri.servicio;

import pe.razuri.modelo.Paciente;
import pe.razuri.excepcion.PacienteNoEncontradoException;

import java.util.HashMap;
import java.util.Map;

public class ServicioPaciente {

    private final Map<String, Paciente> pacientes = new HashMap<>();
    private int siguienteId = 1;

    public Paciente registrarPaciente(String dni, String nombres, String direccion, String contacto) {
        if (pacientes.containsKey(dni)) {
            throw new IllegalArgumentException("Ya existe un paciente con ese DNI.");
        }

        Paciente paciente = new Paciente(siguienteId++, dni, nombres, direccion, contacto);
        pacientes.put(dni, paciente);
        return paciente;
    }

    public Paciente buscarPorDni(String dni) {
        Paciente paciente = pacientes.get(dni);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("Paciente no encontrado.");
        }
        return paciente;
    }
}
