package pe.razuri;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pe.razuri.evento.BusEventos;
import pe.razuri.excepcion.AccesoDenegadoException;
import pe.razuri.excepcion.DniInvalidoException;
import pe.razuri.excepcion.PacienteNoEncontradoException;
import pe.razuri.modelo.EvolucionClinica;
import pe.razuri.modelo.HistorialClinico;
import pe.razuri.modelo.PersonalGestion;
import pe.razuri.modelo.ProfesionalSalud;
import pe.razuri.seguridad.ControlAcceso;
import pe.razuri.seguridad.SeguridadClave;
import pe.razuri.servicio.ServicioHistorial;
import pe.razuri.servicio.ServicioPaciente;

import static org.junit.jupiter.api.Assertions.*;

class ServicioHistorialTest {

    private ServicioPaciente servicioPaciente;
    private ServicioHistorial servicioHistorial;
    private PersonalGestion personalGestion;
    private ProfesionalSalud profesionalSalud;

    private static final String DNI_VALIDO = "12345678";

    @BeforeEach
    void setUp() {
        servicioPaciente = new ServicioPaciente();
        ControlAcceso controlAcceso = new ControlAcceso();
        BusEventos busEventos = new BusEventos();
        servicioHistorial = new ServicioHistorial(servicioPaciente, controlAcceso, busEventos);

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

    @Test
    void registrarPacienteConDniInvalidoLanzaExcepcion() {
        assertThrows(DniInvalidoException.class, () ->
                servicioPaciente.registrarPaciente("abc123", "Juan", "Calle 1", "999999999")
        );
    }

    @Test
    void crearHistorialParaPacienteInexistenteLanzaExcepcion() {
        assertThrows(PacienteNoEncontradoException.class, () ->
                servicioHistorial.crearHistorial("00000000", personalGestion)
        );
    }

    @Test
    void crearHistorialAsignaIdYQuedaVinculadoAlPaciente() {
        servicioPaciente.registrarPaciente(DNI_VALIDO, "Juan", "Calle 1", "999999999");

        HistorialClinico historial = servicioHistorial.crearHistorial(DNI_VALIDO, personalGestion);

        assertNotNull(historial);
        assertEquals(0, historial.getEvoluciones().size());
    }

    @Test
    void crearHistorialDosVecesRetornaElMismoHistorial() {
        servicioPaciente.registrarPaciente(DNI_VALIDO, "Juan", "Calle 1", "999999999");

        HistorialClinico primero = servicioHistorial.crearHistorial(DNI_VALIDO, personalGestion);
        HistorialClinico segundo = servicioHistorial.crearHistorial(DNI_VALIDO, personalGestion);

        assertSame(primero, segundo);
    }

    @Test
    void registrarEvolucionComoPersonalGestionLanzaAccesoDenegado() {
        servicioPaciente.registrarPaciente(DNI_VALIDO, "Juan", "Calle 1", "999999999");
        servicioHistorial.crearHistorial(DNI_VALIDO, personalGestion);

        EvolucionClinica evolucion = new EvolucionClinica(
                1,
                "Diagnostico",
                "Tratamiento",
                "Notas",
                personalGestion.getIdUsuario()
        );

        assertThrows(AccesoDenegadoException.class, () ->
                servicioHistorial.registrarEvolucion(DNI_VALIDO, evolucion, personalGestion)
        );
    }

    @Test
    void registrarEvolucionComoProfesionalSaludFunciona() {
        servicioPaciente.registrarPaciente(DNI_VALIDO, "Juan", "Calle 1", "999999999");
        servicioHistorial.crearHistorial(DNI_VALIDO, profesionalSalud);

        EvolucionClinica evolucion = new EvolucionClinica(
                1,
                "Diagnostico",
                "Tratamiento",
                "Notas",
                profesionalSalud.getIdUsuario()
        );

        servicioHistorial.registrarEvolucion(DNI_VALIDO, evolucion, profesionalSalud);

        HistorialClinico historial = servicioHistorial.consultarHistorial(DNI_VALIDO, profesionalSalud);
        assertEquals(1, historial.getEvoluciones().size());
    }

    @Test
    void registrarEvolucionVaciaLanzaExcepcion() {
        servicioPaciente.registrarPaciente(DNI_VALIDO, "Juan", "Calle 1", "999999999");
        servicioHistorial.crearHistorial(DNI_VALIDO, profesionalSalud);

        EvolucionClinica evolucionVacia = new EvolucionClinica(
                1,
                "",
                "",
                "",
                profesionalSalud.getIdUsuario()
        );

        assertThrows(IllegalArgumentException.class, () ->
                servicioHistorial.registrarEvolucion(DNI_VALIDO, evolucionVacia, profesionalSalud)
        );
    }

    @Test
    void consultarHistorialInactivoLanzaAccesoDenegado() {
        servicioPaciente.registrarPaciente(DNI_VALIDO, "Juan", "Calle 1", "999999999");
        servicioHistorial.crearHistorial(DNI_VALIDO, personalGestion);

        personalGestion.setActivo(false);

        assertThrows(AccesoDenegadoException.class, () ->
                servicioHistorial.consultarHistorial(DNI_VALIDO, personalGestion)
        );
    }
}
