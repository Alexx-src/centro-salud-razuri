/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialClinico {

    private final int idHistorial;
    private final int idPaciente;
    private final LocalDateTime fechaCreacion;

    private boolean activo;

    private final List<EvolucionClinica> evoluciones;
    private final List<DocumentoClinico> documentos;

    public HistorialClinico(
            int idHistorial,
            int idPaciente) {

        this.idHistorial = idHistorial;
        this.idPaciente = idPaciente;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;

        this.evoluciones = new ArrayList<>();
        this.documentos = new ArrayList<>();
    }

    public void agregarEvolucion(
            EvolucionClinica evolucion) {

        if (!evolucion.validarContenido()) {
            throw new IllegalArgumentException(
                    "La evolución no contiene información."
            );
        }

        evoluciones.add(evolucion);
    }

    public void adjuntarDocumento(
            DocumentoClinico documento) {

        documentos.add(documento);
    }

    public List<EvolucionClinica> getEvoluciones() {
        return Collections.unmodifiableList(
                evoluciones
        );
    }

    public List<DocumentoClinico> getDocumentos() {
        return Collections.unmodifiableList(
                documentos
        );
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public int getIdPaciente() {
        return idPaciente;
    }
}