/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

public abstract class Usuario {

    private final int idUsuario;
    private final String nombreUsuario;
    private String passwordHash;
    private boolean activo;
    private final Rol rol;

    protected Usuario(
            int idUsuario,
            String nombreUsuario,
            String passwordHash,
            Rol rol) {

        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = true;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    protected void setPasswordHash(
            String passwordHash) {

        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
