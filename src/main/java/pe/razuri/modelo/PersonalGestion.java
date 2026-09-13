/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

public class PersonalGestion extends Usuario {

    public PersonalGestion(
            int idUsuario,
            String nombreUsuario,
            String passwordHash) {

        super(
                idUsuario,
                nombreUsuario,
                passwordHash,
                Rol.PERSONAL_GESTION
        );
    }
}