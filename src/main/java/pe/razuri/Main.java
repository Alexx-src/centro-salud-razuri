package pe.razuri;

import pe.razuri.evento.BusEventos;
import pe.razuri.excepcion.AccesoDenegadoException;
import pe.razuri.excepcion.SistemaClinicoException;
import pe.razuri.modelo.*;
import pe.razuri.seguridad.ControlAcceso;
import pe.razuri.seguridad.ServicioAutenticacion;
import pe.razuri.servicio.ServicioHistorial;
import pe.razuri.servicio.ServicioPaciente;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final ServicioPaciente servicioPaciente = new ServicioPaciente();
    private static final ControlAcceso controlAcceso = new ControlAcceso();
    private static final BusEventos busEventos = new BusEventos();
    private static final ServicioHistorial servicioHistorial =
            new ServicioHistorial(servicioPaciente, controlAcceso, busEventos);

    private static final ServicioAutenticacion auth = new ServicioAutenticacion();

    private static int siguienteIdEvolucion = 1;
    private static int siguienteIdDocumento = 1;

    public static void main(String[] args) {

        auth.inicializarDemo();

        System.out.println("==============================================");
        System.out.println(" Sistema Clinico - Centro de Salud Razuri");
        System.out.println("==============================================");

        if (!realizarLogin()) {
            return;
        }

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();

            if (opcion.isBlank()) {
                continue;
            }

            try {
                switch (opcion) {
                    case "1" -> registrarPaciente();
                    case "2" -> crearHistorial();
                    case "3" -> registrarEvolucion();
                    case "4" -> consultarHistorial();
                    case "5" -> adjuntarDocumento();
                    case "6" -> registrarProfesionalSalud();
                    case "7" -> cambiarUsuario();
                    case "0" -> salir = true;
                    default -> System.out.println("Opcion no valida.");
                }
            } catch (SistemaClinicoException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("La operacion no pudo completarse.");
            }
        }

        System.out.println("Saliendo del sistema. Hasta luego.");
    }

    private static boolean realizarLogin() {
        for (int intento = 1; intento <= auth.getMaxIntentos(); intento++) {
            System.out.println("\n--- INICIO DE SESION (Intento " + intento + "/" + auth.getMaxIntentos() + ") ---");
            System.out.print("Usuario: ");
            String usuario = sc.nextLine().trim();
            System.out.print("Clave: ");
            String clave = sc.nextLine().trim();

            if (validarObligatorio(usuario, "Usuario") && validarObligatorio(clave, "Clave")) {
                if (auth.login(usuario, clave)) {
                    Usuario u = auth.getUsuarioActual();
                    System.out.println("Bienvenido, " + u.getNombreUsuario() + " (" + u.getRol() + ")");
                    return true;
                }
            }
            System.out.println("Credenciales invalidas. Intentos restantes: " + (auth.getMaxIntentos() - intento));
        }
        System.out.println("Demasiados intentos fallidos. Saliendo...");
        return false;
    }

    private static void cambiarUsuario() {
        System.out.println("\n--- CAMBIAR USUARIO ---");
        Usuario actual = auth.getUsuarioActual();
        System.out.println("Usuario actual: " + actual.getNombreUsuario() + " (" + actual.getRol() + ")");
        System.out.println("Usuarios disponibles:");
        for (Usuario u : auth.getUsuarios()) {
            System.out.println("  - " + u.getNombreUsuario() + " (" + u.getRol() + ")");
        }
        System.out.print("Nuevo usuario: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Clave: ");
        String clave = sc.nextLine().trim();

        if (validarObligatorio(nombre, "Usuario") && validarObligatorio(clave, "Clave")) {
            if (auth.cambiarUsuario(nombre, clave)) {
                Usuario u = auth.getUsuarioActual();
                System.out.println("Ahora actuando como: " + u.getNombreUsuario() + " (" + u.getRol() + ")");
            } else {
                System.out.println("Credenciales invalidas.");
            }
        }
    }

    private static void mostrarMenu() {
        Usuario actual = auth.getUsuarioActual();
        boolean esMedico = actual.getRol() == Rol.PROFESIONAL_SALUD;
        boolean esGestion = actual.getRol() == Rol.PERSONAL_GESTION;

        System.out.println();
        System.out.println("---------- MENU (Usuario: " + actual.getNombreUsuario() + " | " + actual.getRol() + ") ----------");
        System.out.println("1. Registrar paciente");
        System.out.println("2. Crear historial clinico");
        if (esMedico) {
            System.out.println("3. Registrar evolucion clinica");
        }
        System.out.println("4. Consultar historial clinico");
        System.out.println("5. Adjuntar documento clinico");
        if (esGestion) {
            System.out.println("6. Registrar profesional de salud");
        }
        System.out.println("7. Cambiar usuario");
        System.out.println("0. Salir");
        System.out.print("Elige una opcion: ");
    }

    private static void registrarPaciente() {
        String dni = leerDni();
        String nombres = leerNombre();
        String direccion = leerObligatorio("Direccion: ");
        String contacto = leerTelefono();

        Paciente paciente = servicioPaciente.registrarPaciente(dni, nombres, direccion, contacto);
        System.out.println("Paciente registrado: " + paciente);
    }

    private static void crearHistorial() {
        String dni = leerObligatorio("DNI del paciente: ");
        HistorialClinico historial = servicioHistorial.crearHistorial(dni, auth.getUsuarioActual());
        System.out.println("Historial clinico listo. ID historial: " + historial.getIdHistorial());
    }

    private static void registrarEvolucion() {
        String dni = leerObligatorio("DNI del paciente: ");
        String diagnostico = leerObligatorio("Diagnostico: ");
        String tratamiento = leerObligatorio("Tratamiento: ");
        String notas = leerOpcional("Notas de atencion (opcional): ");

        EvolucionClinica evolucion = new EvolucionClinica(
                siguienteIdEvolucion++,
                diagnostico,
                tratamiento,
                notas,
                auth.getUsuarioActual().getIdUsuario()
        );

        servicioHistorial.registrarEvolucion(dni, evolucion, auth.getUsuarioActual());
        System.out.println("Evolucion clinica registrada correctamente.");

        if (sc.hasNextLine()) {
            sc.nextLine();
        }
    }

    private static void consultarHistorial() {
        String dni = leerObligatorio("DNI del paciente: ");

        HistorialClinico historial = servicioHistorial.consultarHistorial(dni, auth.getUsuarioActual());

        System.out.println("Historial clinico #" + historial.getIdHistorial());
        System.out.println("Evoluciones registradas: " + historial.getEvoluciones().size());
        for (EvolucionClinica e : historial.getEvoluciones()) {
            Usuario prof = auth.buscarPorId(e.getIdProfesional());
            String especialidad = "";
            if (prof instanceof ProfesionalSalud) {
                especialidad = " | " + ((ProfesionalSalud) prof).getTipoProfesional();
            }
            System.out.println("  - Evolucion #" + e.getIdEvolucion()
                    + " | Fecha: " + e.getFechaHora()
                    + " | Profesional: " + prof.getNombreUsuario() + especialidad);
            System.out.println("      Diagnostico: " + e.getDiagnostico());
            System.out.println("      Tratamiento: " + e.getTratamiento());
            System.out.println("      Notas: " + e.getNotasAtencion());
        }

        System.out.println("Documentos adjuntos: " + historial.getDocumentos().size());
        for (DocumentoClinico d : historial.getDocumentos()) {
            System.out.println("  - Documento #" + d.getIdDocumento()
                    + " | " + d.getNombreArchivo()
                    + " | Adjuntado: " + d.getFechaAdjunto());
        }
    }

    private static void adjuntarDocumento() {
        String dni = leerObligatorio("DNI del paciente: ");
        HistorialClinico historial = servicioHistorial.consultarHistorial(dni, auth.getUsuarioActual());

        System.out.println("Tipo de documento:");
        System.out.println("  1. Laboratorio");
        System.out.println("  2. Emergencia");
        System.out.println("  3. Otro");
        System.out.print("Opcion: ");
        String tipoOp = sc.nextLine().trim();

        TipoDocumento tipo = switch (tipoOp) {
            case "1" -> TipoDocumento.LABORATORIO;
            case "2" -> TipoDocumento.EMERGENCIA;
            default -> TipoDocumento.OTRO;
        };

        String nombreArchivo = leerObligatorio("Nombre del archivo: ");
        String ubicacion = leerObligatorio("Ubicacion segura (ruta): ");

        DocumentoClinico documento = DocumentoClinicoFactory.crearDocumento(
                tipo,
                siguienteIdDocumento++,
                nombreArchivo,
                ubicacion
        );

        historial.adjuntarDocumento(documento);
        System.out.println("Documento registrado: " + documento.getNombreArchivo());
    }

    private static void registrarProfesionalSalud() {
        System.out.println("\n--- REGISTRAR PROFESIONAL DE SALUD ---");
        System.out.print("Nombre de usuario: ");
        String nombreUsuario = sc.nextLine().trim();
        System.out.print("Clave: ");
        String clave = sc.nextLine().trim();
        System.out.print("Especialidad: ");
        String especialidad = sc.nextLine().trim();

        try {
            auth.registrarProfesionalSalud(nombreUsuario, clave, especialidad, auth.getUsuarioActual());
            System.out.println("Profesional de salud registrado correctamente.");
        } catch (AccesoDenegadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String leerObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (!input.isBlank()) {
                return input;
            }
            System.out.println("Este campo es obligatorio.");
        }
    }

    private static String leerOpcional(String mensaje) {
        System.out.print(mensaje);
        String input = sc.nextLine().trim();
        return input.isBlank() ? "" : input;
    }

    private static boolean validarObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            System.out.println(campo + " es obligatorio.");
            return false;
        }
        return true;
    }

    private static String leerDni() {
        while (true) {
            System.out.print("DNI (8 digitos): ");
            String input = sc.nextLine().trim();
            if (input.isBlank()) {
                System.out.println("El DNI es obligatorio.");
                continue;
            }
            if (!input.matches("\\d{8}")) {
                System.out.println("El DNI debe contener exactamente 8 digitos numericos.");
                continue;
            }
            return input;
        }
    }

    private static String leerNombre() {
        while (true) {
            System.out.print("Nombres: ");
            String input = sc.nextLine().trim();
            if (input.isBlank()) {
                System.out.println("El nombre es obligatorio.");
                continue;
            }
            if (!input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                System.out.println("El nombre solo puede contener letras y espacios.");
                continue;
            }
            return input;
        }
    }

    private static String leerTelefono() {
        while (true) {
            System.out.print("Contacto (9 digitos): ");
            String input = sc.nextLine().trim();
            if (input.isBlank()) {
                System.out.println("El telefono es obligatorio.");
                continue;
            }
            if (!input.matches("\\d{9}")) {
                System.out.println("El telefono debe contener exactamente 9 digitos numericos.");
                continue;
            }
            return input;
        }
    }
}