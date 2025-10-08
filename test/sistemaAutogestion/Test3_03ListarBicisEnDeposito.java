package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_03ListarBicisEnDeposito {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // =========================
    // Depósito vacío → OK y cadena vacía
    // =========================
    @Test
    public void listarVacio_okCadenaVacia() {
        retorno = s.listarBicisEnDeposito();
        assertEquals("Esperaba OK con depósito vacío", Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Esperaba string vacío", "", retorno.getValorString());
    }

    // =========================
    // Una bici en depósito → formato codigo#tipo#estado (Disponible)
    // =========================
    @Test
    public void listarUnaBiciDisponible_okFormato() {
        assertEquals("Registrar AB1234",
                Retorno.Resultado.OK,
                s.registrarBicicleta("ab1234", "Urbana").getResultado());

        retorno = s.listarBicisEnDeposito();

        assertEquals("Esperaba OK al listar", Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Formato incorrecto",
                "AB1234#URBANA#Disponible",
                retorno.getValorString());
    }

    // =========================
    // Dos bicis → respeta orden de ingreso
    // =========================
    @Test
    public void listarDosBicis_okOrdenIngreso() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AER345", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("utr112", "Electrica").getResultado());

        retorno = s.listarBicisEnDeposito();

        assertEquals("Esperaba OK al listar", Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Debe respetar orden de ingreso",
                "AER345#URBANA#Disponible|UTR112#ELECTRICA#Disponible",
                retorno.getValorString());
    }

    // =========================
    // Bici en mantenimiento → muestra "Mantenimiento"
    // =========================
    @Test
    public void listarUnaBici_enMantenimiento() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("xyz789", "Mountain").getResultado());

        // marcar en mantenimiento desde depósito
        assertEquals("Marcar en mantenimiento debe devolver OK",
                Retorno.Resultado.OK,
                s.marcarEnMantenimiento("XYZ789", "servicio general").getResultado());

        retorno = s.listarBicisEnDeposito();

        assertEquals("Esperaba OK al listar", Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("XYZ789#MOUNTAIN#Mantenimiento", retorno.getValorString());
    }
    
}
