package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_05ListarBicicletasDeEstacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // =========== Casos OK ===========

    @Test
    public void listarBicicletasDeEstacionOk_estacionVacia() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 3).getResultado());

        retorno = s.listarBicicletasDeEstacion("Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString()); // sin bicis => string vacío
    }

    @Test
    public void listarBicicletasDeEstacionOk_ordenAscendente() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Parque", "Parque Rodo", 5).getResultado());

        // Registro bicis en cualquier orden
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("ZZ9999", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AA0001", "Electrica").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("MM5555", "Mountain").getResultado());

        // Asigno a la estación (debe quedar ordenado por código dentro de la estación)
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("ZZ9999", "Parque").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AA0001", "Parque").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("MM5555", "Parque").getResultado());

        retorno = s.listarBicicletasDeEstacion("Parque");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("AA0001|MM5555|ZZ9999", retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_nombreCaseInsensitive() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("CORDON", "Cordon", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "CORDON").getResultado());

        // Si tu buscarEstacion ignora mayúsculas/minúsculas, esto debe funcionar
        retorno = s.listarBicicletasDeEstacion("cordon");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("AB1234", retorno.getValorString());
    }

    // =========== Errores ===========

    @Test
    public void listarBicicletasDeEstacionError01_nombreVacio() {
        retorno = s.listarBicicletasDeEstacion("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void listarBicicletasDeEstacionError02_noExiste() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Cordoba", "Cordoba", 2).getResultado());

        retorno = s.listarBicicletasDeEstacion("NoExiste");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
}
