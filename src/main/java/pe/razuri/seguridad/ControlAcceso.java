package pe.razuri.seguridad;

import pe.razuri.modelo.Rol;
import pe.razuri.modelo.Usuario;

public class ControlAcceso {

    public boolean puedeConsultarHistorial(
            Usuario usuario) {

        return usuario != null
                && usuario.isActivo();
    }

    public boolean puedeRegistrarEvolucion(
            Usuario usuario) {

        return usuario != null
                && usuario.isActivo()
                && usuario.getRol()
                    == Rol.PROFESIONAL_SALUD;
    }
}
