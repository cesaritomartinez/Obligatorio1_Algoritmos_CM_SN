package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_07EliminarEstacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void eliminarEstacionOk() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());

        retorno = s.eliminarEstacion("Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError01() {
        retorno = s.eliminarEstacion(""); // nombre vacío
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError02() {
        retorno = s.eliminarEstacion("NoExiste"); // estación inexistente
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError03_estacionConBicis() {
        // Registro estación y una bici, la asigno a la estación → quedan pendientes (tiene bicis)
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Parque", "Parque Rodo", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Parque").getResultado());

        retorno = s.eliminarEstacion("Parque");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionOk_trasVaciarConMantenimiento() {
        // Dejo la estación con una bici y luego la "vacío" marcando mantenimiento (mueve la bici a depósito)
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Cordón", "Cordón", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("CD5678", "Electrica").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("CD5678", "Cordón").getResultado());

        // Vaciar pendientes: la bici sale de la estación al depósito y queda en MANTENIMIENTO
        assertEquals(Retorno.Resultado.OK,
                s.marcarEnMantenimiento("CD5678", "freno").getResultado());

        // Ahora debería poder eliminar la estación
        retorno = s.eliminarEstacion("Cordón");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
}

