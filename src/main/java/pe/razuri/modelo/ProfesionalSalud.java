/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

/**
 *
 * @author GRLL
 */
public class ProfesionalSalud extends Usuario {

    private final String tipoProfesional;

    public ProfesionalSalud(
            int idUsuario,
            String nombreUsuario,
            String passwordHash,
            String tipoProfesional) {

        super(
                idUsuario,
                nombreUsuario,
                passwordHash,
                Rol.PROFESIONAL_SALUD
        );

        this.tipoProfesional = tipoProfesional;
    }

    public String getTipoProfesional() {
        return tipoProfesional;
    }
}