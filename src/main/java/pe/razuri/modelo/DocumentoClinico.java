/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

import java.time.LocalDateTime;

public abstract class DocumentoClinico {

    private final int idDocumento;
    private final String nombreArchivo;
    private final String ubicacionSegura;
    private final LocalDateTime fechaAdjunto;

    protected DocumentoClinico(
            int idDocumento,
            String nombreArchivo,
            String ubicacionSegura) {

        this.idDocumento = idDocumento;
        this.nombreArchivo = nombreArchivo;
        this.ubicacionSegura = ubicacionSegura;
        this.fechaAdjunto = LocalDateTime.now();
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public LocalDateTime getFechaAdjunto() {
        return fechaAdjunto;
    }

    protected String getUbicacionSegura() {
        return ubicacionSegura;
    }
}