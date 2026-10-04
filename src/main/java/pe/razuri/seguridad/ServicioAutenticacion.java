package pe.razuri.seguridad;

import pe.razuri.modelo.PersonalGestion;
import pe.razuri.modelo.ProfesionalSalud;
import pe.razuri.modelo.Rol;
import pe.razuri.modelo.Usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServicioAutenticacion {

    private static final int MAX_INTENTOS = 3;

    private final Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioActual;
    private int siguienteId = 1;

    public void inicializarDemo() {
        PersonalGestion gestion = new PersonalGestion(
                siguienteId++,
                "gestion01",
                SeguridadClave.generarHash("ClaveSegura01")
        );
        usuarios.put(gestion.getNombreUsuario(), gestion);

        ProfesionalSalud medico = new ProfesionalSalud(
                siguienteId++,
                "medico01",
                SeguridadClave.generarHash("ClaveSegura02"),
                "Medico General"
        );
        usuarios.put(medico.getNombreUsuario(), medico);
    }

    public boolean login(String nombreUsuario, String clave) {
        int intentos = 0;

        while (intentos < MAX_INTENTOS) {
            Usuario u = usuarios.get(nombreUsuario);
            if (u != null && SeguridadClave.verificarClave(clave, u.getPasswordHash())) {
                usuarioActual = u;
                return true;
            }
            intentos++;
        }
        return false;
    }

    public void registrarUsuario(String nombreUsuario, String clave, String rol, String especialidad) {
        validarRegistro(nombreUsuario, clave, rol, especialidad);

        String hash = SeguridadClave.generarHash(clave);
        Usuario nuevo;

        if ("1".equals(rol)) {
            nuevo = new PersonalGestion(siguienteId++, nombreUsuario, hash);
        } else {
            nuevo = new ProfesionalSalud(siguienteId++, nombreUsuario, hash, especialidad);
        }

        usuarios.put(nombreUsuario, nuevo);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public int getMaxIntentos() {
        return MAX_INTENTOS;
    }

    public Usuario buscarPorId(int id) {
        return usuarios.values().stream()
                .filter(u -> u.getIdUsuario() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean cambiarUsuario(String nombreUsuario, String clave) {
        Usuario u = usuarios.get(nombreUsuario);
        if (u != null && SeguridadClave.verificarClave(clave, u.getPasswordHash())) {
            usuarioActual = u;
            return true;
        }
        return false;
    }

    public Collection<Usuario> getUsuarios() {
        return usuarios.values();
    }

    public void registrarProfesionalSalud(String nombreUsuario, String clave, String especialidad, Usuario solicitante) {
        if (solicitante == null || solicitante.getRol() != Rol.PERSONAL_GESTION) {
            throw new pe.razuri.excepcion.AccesoDenegadoException("Solo personal de gestion puede registrar profesionales.");
        }
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (usuarios.containsKey(nombreUsuario)) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("La clave es obligatoria.");
        }
        if (especialidad == null || especialidad.isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }

        String hash = SeguridadClave.generarHash(clave);
        ProfesionalSalud nuevo = new ProfesionalSalud(siguienteId++, nombreUsuario, hash, especialidad);
        usuarios.put(nombreUsuario, nuevo);
    }

    private void validarRegistro(String nombreUsuario, String clave, String rol, String especialidad) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (usuarios.containsKey(nombreUsuario)) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("La clave es obligatoria.");
        }
        if (!"1".equals(rol) && !"2".equals(rol)) {
            throw new IllegalArgumentException("Rol invalido. Use 1 (Gestion) o 2 (Profesional).");
        }
        if ("2".equals(rol) && (especialidad == null || especialidad.isBlank())) {
            throw new IllegalArgumentException("La especialidad es obligatoria para profesional de salud.");
        }
    }
}