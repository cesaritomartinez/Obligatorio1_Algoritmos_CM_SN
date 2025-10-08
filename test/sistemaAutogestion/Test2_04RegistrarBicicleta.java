package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_04RegistrarBicicleta {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void registrarBicicletaOk() {
        retorno = s.registrarBicicleta("ABC123", "Urbana");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError01() {
        retorno = s.registrarBicicleta("", "Urbana"); // parámetro vacío
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError02() {
        retorno = s.registrarBicicleta("ABC12", "Urbana"); // 5 caracteres (inválido)
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError03() {
        retorno = s.registrarBicicleta("XYZ789", "Patineta"); // tipo no permitido
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError04() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("ZZZ999", "Electrica").getResultado());
        retorno = s.registrarBicicleta("ZZZ999", "Electrica"); // código duplicado
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
    
}

