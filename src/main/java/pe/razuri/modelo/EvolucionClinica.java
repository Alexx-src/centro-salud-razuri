/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

import java.time.LocalDateTime;

public class EvolucionClinica {

    private final int idEvolucion;
    private final LocalDateTime fechaHora;
    private final String diagnostico;
    private final String tratamiento;
    private final String notasAtencion;
    private final int idProfesional;

    public EvolucionClinica(
            int idEvolucion,
            String diagnostico,
            String tratamiento,
            String notasAtencion,
            int idProfesional) {

        this.idEvolucion = idEvolucion;
        this.fechaHora = LocalDateTime.now();
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.notasAtencion = notasAtencion;
        this.idProfesional = idProfesional;
    }

    public boolean validarContenido() {
        return diagnostico != null && !diagnostico.isBlank()
                || tratamiento != null && !tratamiento.isBlank()
                || notasAtencion != null && !notasAtencion.isBlank();
    }

    public int getIdEvolucion() {
        return idEvolucion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getIdProfesional() {
        return idProfesional;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getNotasAtencion() {
        return notasAtencion;
    }
}