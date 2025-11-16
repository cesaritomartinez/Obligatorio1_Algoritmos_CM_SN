package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_09UsuariosEnEspera {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void usuariosEnEsperaSinUsuarios() {
        // Estación sin bicis ni usuarios en cola → cadena vacía
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());

        retorno = s.usuariosEnEspera("Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void usuariosEnEsperaConUsuariosEncolados() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 1).getResultado());

        // Dos usuarios
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("23456789", "Luis").getResultado());

        // No asignamos bicis a la estación, por lo que nunca hay disponibilidad:
        // cada intento de alquiler encola al usuario en colaAlquiler.
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("12345678", "Centro").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("23456789", "Centro").getResultado());

        retorno = s.usuariosEnEspera("Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678|23456789", retorno.getValorString());
    }
}
