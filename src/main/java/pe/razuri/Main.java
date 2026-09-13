/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pe.razuri;

import pe.razuri.evento.BusEventos;
import pe.razuri.modelo.*;
import pe.razuri.seguridad.ControlAcceso;
import pe.razuri.seguridad.SeguridadClave;
import pe.razuri.servicio.ServicioHistorial;
import pe.razuri.servicio.ServicioPaciente;

public class Main {

    public static void main(String[] args) {

        ServicioPaciente servicioPaciente =
                new ServicioPaciente();

        ControlAcceso controlAcceso =
                new ControlAcceso();

        BusEventos busEventos =
                new BusEventos();

        ServicioHistorial servicioHistorial =
                new ServicioHistorial(
                        servicioPaciente,
                        controlAcceso,
                        busEventos
                );

        String hash =
                SeguridadClave.generarHash(
                        "ClaveSegura01"
                );

        PersonalGestion personal =
                new PersonalGestion(
                        1,
                        "gestion01",
                        hash
                );

        ProfesionalSalud medico =
                new ProfesionalSalud(
                        2,
                        "medico01",
                        SeguridadClave.generarHash(
                                "ClaveSegura02"
                        ),
                        "Médico"
                );

        try {

            Paciente paciente =
                    servicioPaciente
                            .registrarPaciente(
                                    "12345678",
                                    "Paciente de prueba",
                                    "Dirección protegida",
                                    "Contacto protegido"
                            );

            System.out.println(
                    "Paciente registrado: "
                            + paciente
            );

            servicioHistorial.crearHistorial(
                    paciente.getDni(),
                    personal
            );

            EvolucionClinica evolucion =
                    new EvolucionClinica(
                            1,
                            "Información clínica reservada",
                            "Tratamiento reservado",
                            "Nota de atención reservada",
                            medico.getIdUsuario()
                    );

            servicioHistorial
                    .registrarEvolucion(
                            paciente.getDni(),
                            evolucion,
                            medico
                    );

            DocumentoClinico documento =
                    DocumentoClinicoFactory
                            .crearDocumento(
                                    TipoDocumento.LABORATORIO,
                                    1,
                                    "laboratorio001.pdf",
                                    "/datos/clinicos/001"
                            );

            System.out.println(
                    "Documento registrado: "
                            + documento.getNombreArchivo()
            );

        } catch (Exception e) {

            System.out.println(
                    "La operación no pudo completarse."
            );
        }
    }
}