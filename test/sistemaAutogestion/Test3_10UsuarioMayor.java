package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_10UsuarioMayor {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void usuarioMayorSinUsuarios() {
        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void usuarioMayorUsuariosSinAlquileresCompletados() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("23456789", "Luis").getResultado());

        // Nadie alquila ni devuelve → todos con 0 alquileresCompletados
        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Con tu implementación actual, en este caso queda "" (ninguno “mayor”)
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void usuarioMayorUnUsuarioConAlquilerCompletado() {
        // Usuario, estación y bici
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Centro").getResultado());

        // Alquila y luego devuelve en la misma estación
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("12345678", "Centro").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.devolverBicicleta("12345678", "Centro").getResultado());

        // Ahora tiene 1 alquiler completado
        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }

    @Test
    public void usuarioMayorEmpatePorCantidad_elMenorCI() {
        // Dos usuarios
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("22222222", "Beto").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("11111111", "Ana").getResultado());

        // Una estación con una bici
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Centro").getResultado());

        // Usuario con CI mayor (22222222) completa 1 alquiler
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("22222222", "Centro").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.devolverBicicleta("22222222", "Centro").getResultado());

        // Usuario con CI menor (11111111) también completa 1 alquiler
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("11111111", "Centro").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.devolverBicicleta("11111111", "Centro").getResultado());

        // Ambos tienen la misma cantidad de alquileresCompletados (1),
        // gana el de CI menor: "11111111"
        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString());
    }
}
