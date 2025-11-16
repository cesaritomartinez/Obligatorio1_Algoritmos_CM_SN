package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_09AlquilarBicicleta {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void alquilarBicicletaOk_conBiciDisponible() {
        // Usuario y estación válidos, con bici disponible → OK
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Centro").getResultado());

        retorno = s.alquilarBicicleta("12345678", "Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void alquilarBicicletaOk_sinBicis_enQueueEspera() {
        // No hay bicis en la estación, pero igualmente devuelve OK (usuario queda en cola)
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("23456789", "Luis").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Cordón", "Cordón", 2).getResultado());

        retorno = s.alquilarBicicleta("23456789", "Cordón");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void alquilarBicicletaError01_parametrosVacios() {
        retorno = s.alquilarBicicleta("", "Centro");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void alquilarBicicletaError02_usuarioInexistente() {
        // Estación registrada, pero usuario no existe → ERROR_2
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());

        retorno = s.alquilarBicicleta("12345678", "Centro");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void alquilarBicicletaError03_estacionInexistente() {
        // Usuario existe, estación no → ERROR_3
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());

        retorno = s.alquilarBicicleta("12345678", "NoExiste");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void alquilarBicicletaError04_usuarioYaTieneBici() {
        // Usuario ya tiene bici alquilada → ERROR_4
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Centro").getResultado());

        // Primer alquiler OK
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("12345678", "Centro").getResultado());

        // Segundo alquiler (ya tiene bici) → ERROR_4
        retorno = s.alquilarBicicleta("12345678", "Centro");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
}
