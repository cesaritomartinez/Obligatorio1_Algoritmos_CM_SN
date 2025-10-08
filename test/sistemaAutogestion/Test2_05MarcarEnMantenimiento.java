package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class Test2_05MarcarEnMantenimiento {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // =========================
    // OK: bici registrada (en depósito) pasa a MANTENIMIENTO
    // =========================
    @Test
    public void marcarEnMantenimientoOk_desdeDeposito() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());

        retorno = s.marcarEnMantenimiento("AB1234", "servicio general");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // si intento de nuevo, ahora debe estar en mantenimiento → ERROR_4
        retorno = s.marcarEnMantenimiento("AB1234", "otra vez");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    // =========================
    // ERROR_1: parámetros vacíos
    // =========================
    @Test
    public void marcarEnMantenimientoError01_parametrosVacios() {
        retorno = s.marcarEnMantenimiento("", "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.marcarEnMantenimiento("ABC123", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================
    // ERROR_2: bici inexistente
    // =========================
    @Test
    public void marcarEnMantenimientoError02_inexistente() {
        retorno = s.marcarEnMantenimiento("ZZZ999", "pinchadura");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    // =========================
    // ERROR_4: ya en mantenimiento
    // =========================
    @Test
    public void marcarEnMantenimientoError04_yaEnMantenimiento() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("CD5678", "Mountain").getResultado());

        assertEquals(Retorno.Resultado.OK,
                s.marcarEnMantenimiento("CD5678", "cambio de frenos").getResultado());

        retorno = s.marcarEnMantenimiento("CD5678", "repetido");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    // =========================
    // ERROR_3: bici alquilada (activar cuando implementes 2.9)
    // =========================
    @Ignore("Activar cuando esté implementado alquilarBicicleta (2.9)")
    @Test
    public void marcarEnMantenimientoError03_alquilada() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E1", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("EF9012", "Electrica").getResultado());

        // 2.8: mover de depósito a estación
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("EF9012", "E1").getResultado());

        // 2.9: alquilar
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("12345678", "E1").getResultado());

        // ahora debería fallar por ALQUILADA
        retorno = s.marcarEnMantenimiento("EF9012", "no debería permitir");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    // =========================
    // OK: estaba anclada → pasa a depósito en MANTENIMIENTO (activar con 2.8)
    // =========================
    //@Ignore("Activar cuando esté implementado asignarBicicletaAEstacion (2.8)")
    @Test
    public void marcarEnMantenimientoOk_desdeEstacion() {
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E2", "Cordón", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("GH3456", "Urbana").getResultado());

        // 2.8: asignar a estación (queda anclada)
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("GH3456", "E2").getResultado());

        // 2.5: debe retirarla de la estación y dejarla en depósito en MANTENIMIENTO
        retorno = s.marcarEnMantenimiento("GH3456", "ajuste general");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Cuando implementes 3.3, podés verificar que aparezca en el depósito con estado MANTENIMIENTO
        // Retorno listado = s.listarBicisEnDeposito();
        // assertTrue(listado.getValorString().contains("GH3456#URBANA#MANTENIMIENTO"));
    }
   
    
}
