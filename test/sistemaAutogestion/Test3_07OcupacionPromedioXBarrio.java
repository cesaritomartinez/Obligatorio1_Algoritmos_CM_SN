package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_07OcupacionPromedioXBarrio {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void ocupacionPromedioXBarrioOk_sinEstaciones() {
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_unBarrioAgrupado() {
        // Barrio: Centro → cap total 10, ancladas 6 → 60%
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 5).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Centro", 5).getResultado());

        // 6 bicis válidas
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CE0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CE0002", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CE0003", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CE0004", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CE0005", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CE0006", "Urbana").getResultado());

        // E1: 2 ancladas, E2: 4 ancladas
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CE0001", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CE0002", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CE0003", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CE0004", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CE0005", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CE0006", "E2").getResultado());

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#60", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_dosBarrios_ordenYRedondeo() {
        // Centro: cap 3, ancladas 2 -> 67
        // Parque: cap 2, ancladas 1 -> 50
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("A", "Centro", 3).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("B", "Parque", 2).getResultado());

        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CT0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CT0002", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("PR0001", "Urbana").getResultado());

        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CT0001", "A").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CT0002", "A").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("PR0001", "B").getResultado());

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Orden alfabético: Centro antes que Parque
        assertEquals("Centro#67|Parque#50", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_moviendoEntreBarriosActualiza() {
        // Cordon (cap 2), Malvin (cap 2)
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("C1", "Cordon", 2).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("M1", "Malvin", 2).getResultado());

        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("CD0001", "Urbana").getResultado());

        // Anclo en Cordon → Cordon: 1/2 = 50, Malvin: 0/2 = 0
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CD0001", "C1").getResultado());
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Cordon#50|Malvin#0", retorno.getValorString());

        // Muevo la misma bici a Malvin → Cordon: 0/2 = 0, Malvin: 1/2 = 50
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("CD0001", "M1").getResultado());
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Cordon#0|Malvin#50", retorno.getValorString());
    }
}
