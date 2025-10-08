package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_06RepararBicicleta {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void repararBicicletaOk() {
        // Prepara: registro y paso a mantenimiento
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.marcarEnMantenimiento("AB1234", "freno").getResultado());

        // Reparo → debe quedar OK
        retorno = s.repararBicicleta("AB1234");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void repararBicicletaError01() {
        // Código vacío → ERROR_1
        retorno = s.repararBicicleta("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void repararBicicletaError02() {
        // Bici inexistente → ERROR_2
        retorno = s.repararBicicleta("ZZZ999");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void repararBicicletaError03() {
        // Registrada pero NO en mantenimiento → ERROR_3
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("CD5678", "Electrica").getResultado());

        retorno = s.repararBicicleta("CD5678");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void repararBicicletaError03_yaReparada() {
        // Pasa a mantenimiento, repara OK, segundo reparar debe dar ERROR_3
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("EF0001", "Mountain").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.marcarEnMantenimiento("EF0001", "cadena").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.repararBicicleta("EF0001").getResultado());

        retorno = s.repararBicicleta("EF0001"); // ya no está en mantenimiento
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
       
    
}
