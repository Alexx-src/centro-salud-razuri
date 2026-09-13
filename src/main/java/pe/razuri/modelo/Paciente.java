package pe.razuri.modelo;

import pe.razuri.excepcion.DniInvalidoException;

public class Paciente {

    private final int idPaciente;
    private final String dni;

    private String nombres;
    private String direccion;
    private String contacto;

    private HistorialClinico historial;

    public Paciente(
            int idPaciente,
            String dni,
            String nombres,
            String direccion,
            String contacto) {

        validarDni(dni);

        this.idPaciente = idPaciente;
        this.dni = dni;
        this.nombres = nombres;
        this.direccion = direccion;
        this.contacto = contacto;
    }

    private void validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new DniInvalidoException(
                    "El DNI debe contener exactamente 8 dígitos."
            );
        }
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public String getDni() {
        return dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacío."
            );
        }
        this.nombres = nombres;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException(
                    "La dirección no puede estar vacía."
            );
        }
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public boolean tieneHistorial() {
        return historial != null;
    }

    public HistorialClinico getHistorial() {
        return historial;
    }

    public void asignarHistorial(HistorialClinico historial) {
        if (this.historial != null) {
            throw new IllegalStateException(
                    "El paciente ya posee historial clínico."
            );
        }
        this.historial = historial;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + idPaciente +
                ", dni=****" +
                dni.substring(4) +
                '}';
    }
}
