package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_03RegistrarUsuario {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void registrarUsuarioOk() {
        retorno = s.registrarUsuario("12345678", "Usuario01");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarUsuarioError01() {
        // cédula vacía / solo espacios / null
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarUsuario("", "Nombre").getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarUsuario("   ", "Nombre").getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarUsuario(null, "Nombre").getResultado());

        // nombre vacío / solo espacios / null
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarUsuario("12345678", "").getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarUsuario("12345678", "   ").getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarUsuario("12345678", null).getResultado());
    }

    @Test
    public void registrarUsuarioError02() {
        // Formato CI inválido (no exactamente 8 dígitos)
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarUsuario("1234567",  "U").getResultado()); // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarUsuario("123456789","U").getResultado()); // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarUsuario("12A45678","U").getResultado()); // letra
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarUsuario("12 45678","U").getResultado()); // espacio interno
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarUsuario("1234-678","U").getResultado()); // guion
    }

    @Test
    public void registrarUsuarioError03() {
        // Duplicado (misma cédula ya registrada)
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario("12345678", "Usuario01").getResultado());
        retorno = s.registrarUsuario("12345678", "OtroNombre");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
}

