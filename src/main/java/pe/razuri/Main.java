package pe.razuri;

import pe.razuri.evento.BusEventos;
import pe.razuri.excepcion.SistemaClinicoException;
import pe.razuri.modelo.*;
import pe.razuri.seguridad.ControlAcceso;
import pe.razuri.seguridad.SeguridadClave;
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

    private static PersonalGestion personalGestion;
    private static ProfesionalSalud profesionalSalud;

    private static int siguienteIdEvolucion = 1;
    private static int siguienteIdDocumento = 1;

    public static void main(String[] args) {

        inicializarUsuariosDemo();

        boolean salir = false;

        System.out.println("==============================================");
        System.out.println(" Sistema Clinico - Centro de Salud Razuri");
        System.out.println("==============================================");

        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();

            try {
                switch (opcion) {
                    case "1" -> registrarPaciente();
                    case "2" -> crearHistorial();
                    case "3" -> registrarEvolucion();
                    case "4" -> consultarHistorial();
                    case "5" -> adjuntarDocumento();
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

    private static void inicializarUsuariosDemo() {
        personalGestion = new PersonalGestion(
                1,
                "gestion01",
                SeguridadClave.generarHash("ClaveSegura01")
        );

        profesionalSalud = new ProfesionalSalud(
                2,
                "medico01",
                SeguridadClave.generarHash("ClaveSegura02"),
                "Medico General"
        );
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("---------- MENU ----------");
        System.out.println("1. Registrar paciente");
        System.out.println("2. Crear historial clinico");
        System.out.println("3. Registrar evolucion clinica (profesional de salud)");
        System.out.println("4. Consultar historial clinico");
        System.out.println("5. Adjuntar documento clinico");
        System.out.println("0. Salir");
        System.out.print("Elige una opcion: ");
    }

    private static Usuario elegirUsuario() {
        System.out.println("Actuar como:");
        System.out.println("  1. Personal de gestion (" + personalGestion.getNombreUsuario() + ")");
        System.out.println("  2. Profesional de salud (" + profesionalSalud.getNombreUsuario() + ")");
        System.out.print("Opcion: ");
        String op = sc.nextLine().trim();
        return "2".equals(op) ? profesionalSalud : personalGestion;
    }

    private static void registrarPaciente() {
        System.out.print("DNI (8 digitos): ");
        String dni = sc.nextLine().trim();
        System.out.print("Nombres: ");
        String nombres = sc.nextLine().trim();
        System.out.print("Direccion: ");
        String direccion = sc.nextLine().trim();
        System.out.print("Contacto: ");
        String contacto = sc.nextLine().trim();

        Paciente paciente = servicioPaciente.registrarPaciente(dni, nombres, direccion, contacto);
        System.out.println("Paciente registrado: " + paciente);
    }

    private static void crearHistorial() {
        System.out.print("DNI del paciente: ");
        String dni = sc.nextLine().trim();

        Usuario usuario = elegirUsuario();

        HistorialClinico historial = servicioHistorial.crearHistorial(dni, usuario);
        System.out.println("Historial clinico listo. ID historial: " + historial.getIdHistorial());
    }

    private static void registrarEvolucion() {
        System.out.print("DNI del paciente: ");
        String dni = sc.nextLine().trim();

        System.out.print("Diagnostico: ");
        String diagnostico = sc.nextLine().trim();
        System.out.print("Tratamiento: ");
        String tratamiento = sc.nextLine().trim();
        System.out.print("Notas de atencion: ");
        String notas = sc.nextLine().trim();

        EvolucionClinica evolucion = new EvolucionClinica(
                siguienteIdEvolucion++,
                diagnostico,
                tratamiento,
                notas,
                profesionalSalud.getIdUsuario()
        );

        servicioHistorial.registrarEvolucion(dni, evolucion, profesionalSalud);
        System.out.println("Evolucion clinica registrada correctamente.");
    }

    private static void consultarHistorial() {
        System.out.print("DNI del paciente: ");
        String dni = sc.nextLine().trim();

        Usuario usuario = elegirUsuario();

        HistorialClinico historial = servicioHistorial.consultarHistorial(dni, usuario);

        System.out.println("Historial clinico #" + historial.getIdHistorial());
        System.out.println("Evoluciones registradas: " + historial.getEvoluciones().size());
        for (EvolucionClinica e : historial.getEvoluciones()) {
            System.out.println("  - Evolucion #" + e.getIdEvolucion()
                    + " | Fecha: " + e.getFechaHora()
                    + " | Profesional ID: " + e.getIdProfesional());
        }

        System.out.println("Documentos adjuntos: " + historial.getDocumentos().size());
        for (DocumentoClinico d : historial.getDocumentos()) {
            System.out.println("  - Documento #" + d.getIdDocumento()
                    + " | " + d.getNombreArchivo()
                    + " | Adjuntado: " + d.getFechaAdjunto());
        }
    }

    private static void adjuntarDocumento() {
        System.out.print("DNI del paciente: ");
        String dni = sc.nextLine().trim();

        Usuario usuario = elegirUsuario();

        HistorialClinico historial = servicioHistorial.consultarHistorial(dni, usuario);

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

        System.out.print("Nombre del archivo: ");
        String nombreArchivo = sc.nextLine().trim();
        System.out.print("Ubicacion segura (ruta): ");
        String ubicacion = sc.nextLine().trim();

        DocumentoClinico documento = DocumentoClinicoFactory.crearDocumento(
                tipo,
                siguienteIdDocumento++,
                nombreArchivo,
                ubicacion
        );

        historial.adjuntarDocumento(documento);
        System.out.println("Documento registrado: " + documento.getNombreArchivo());
    }
}
