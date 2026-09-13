/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

import java.time.LocalDateTime;

public class RegistroAuditoria {

    private final int idRegistro;
    private final LocalDateTime fechaHora;
    private final String accion;
    private final int idUsuario;
    private final int idHistorial;

    public RegistroAuditoria(
            int idRegistro,
            String accion,
            int idUsuario,
            int idHistorial) {

        this.idRegistro = idRegistro;
        this.fechaHora = LocalDateTime.now();
        this.accion = accion;
        this.idUsuario = idUsuario;
        this.idHistorial = idHistorial;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getAccion() {
        return accion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdHistorial() {
        return idHistorial;
    }
}
