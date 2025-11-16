package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_11DeshacerUltimosRetiros {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    private void prepararAlquileres(int cantidad) {
        // Prepara 'cantidad' alquileres apilados en historicoAlquileres
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", cantidad).getResultado());

        for (int i = 0; i < cantidad; i++) {
            String ci = String.format("1000000%d", i);
            String codigo = String.format("BI%04d", i);

            assertEquals(Retorno.Resultado.OK,
                    s.registrarUsuario(ci, "User" + i).getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.registrarBicicleta(codigo, "Urbana").getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.asignarBicicletaAEstacion(codigo, "Centro").getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.alquilarBicicleta(ci, "Centro").getResultado());
        }
    }

    @Test
    public void deshacerUltimosRetirosError01_nNoValido() {
        retorno = s.deshacerUltimosRetiros(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.deshacerUltimosRetiros(-1);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void deshacerUltimosRetirosSinAlquileres() {
        // No hay alquileres en el histórico → OK con cadena vacía
        retorno = s.deshacerUltimosRetiros(3);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void deshacerUltimosRetirosOk_conAlgunosRetiros() {
        prepararAlquileres(3); // 3 retiros apilados

        retorno = s.deshacerUltimosRetiros(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // No conozco el formato exacto de formatoDeshacer(), pero al menos
        // verifico que devuelve algo no vacío.
        String lista = retorno.getValorString();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
